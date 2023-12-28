package com.example.footballscore.matches

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballscore.adapter.AdapterMatchHorizontal
import com.example.footballscore.competitions.competion_match.Competition_Match

import com.example.footballscore.databinding.MatchesLayoutBinding
import com.example.footballscore.matches.matchModel.Match
import com.example.footballscore.matches.matchModel.Matche
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MatchesFragment: Fragment() {
    private lateinit var  viewBinding : MatchesLayoutBinding
    private lateinit var  apiInterface : ApiInterface
    private lateinit var listMatchHorizontal : ArrayList<Matche>
    private lateinit var adapterHorizontal : AdapterMatchHorizontal
    private var dateString : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchesLayoutBinding.inflate(inflater, container, false)
        listMatchHorizontal = ArrayList()
        //getCompetitionMatch()
        setDate()
        onSetUpRecyclerViewHorizontal()
        getMatchRecent()
        viewBinding.calendarPick.setOnClickListener{
            openDatePicker()
        }
        return viewBinding.root
    }


    private fun getMatchRecent() {
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatter.parse(dateString!!) ///Đổi từ string sang date

        // Ngày liền trước
        val calendarPrevious = Calendar.getInstance()
        calendarPrevious.time = date
        calendarPrevious.add(Calendar.DAY_OF_YEAR, -1)
        val previousDate = calendarPrevious.time
        val previousDateString = formatter.format(previousDate)

        // Ngày liền sau
        val calendarNext = Calendar.getInstance()
        calendarNext.time = date
        calendarNext.add(Calendar.DAY_OF_YEAR, 1)
        val nextDate = calendarNext.time
        val nextDateString = formatter.format(nextDate)

        val call = apiInterface.getMatchRecentFor2day(previousDateString, nextDateString)
        call.enqueue(object : Callback<Match>{
            override fun onResponse(call: Call<Match>, response: Response<Match>) {
               val resultMatch = response.body()!!
                for (match in resultMatch.matches) {
                    // Lấy số thứ tự của phần tử trong danh sách
                    val position = listMatchHorizontal.size
                    // Thêm phần tử vào danh sách
                    listMatchHorizontal.add(match)
                    adapterHorizontal.notifyItemInserted(position)
                }

            }

            override fun onFailure(call: Call<Match>, t: Throwable) {
                Log.d("Failed", t.message.toString())
            }

        })
    }

    private fun onSetUpRecyclerViewHorizontal(){
        viewBinding.recyclerViewMatchHorizontal.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.recyclerViewMatchHorizontal.layoutManager = layout
        adapterHorizontal = AdapterMatchHorizontal(listMatchHorizontal)
        viewBinding.recyclerViewMatchHorizontal.adapter = adapterHorizontal
    }


    private fun openDatePicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(i, i2, i3)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = sdf.format(selectedDate.time)
            dateString = formattedDate
            if(i3.toString().length < 2){
                viewBinding.txtDay.text = "0$i3"
            }
            else {
                viewBinding.txtDay.text = i3.toString()
            }

        }, year,month, day )
        datePickerDialog.show()
    }
    @SuppressLint("SetTextI18n")
    private fun setDate(){
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year,month,day)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = sdf.format(selectedDate.time)
        dateString = formattedDate
        Toast.makeText(requireContext(), dateString.toString(), Toast.LENGTH_SHORT).show()
        if(day.toString().length < 2){
            viewBinding.txtDay.text = "0$day"
        }
        else {
            viewBinding.txtDay.text = day.toString()
        }
    }

    private fun getCompetitionMatch(){
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        val call = apiInterface.getMatchFromCompetitionByDate(2021,"2023-12-26", "2023-12-27" )
        call.enqueue(object: Callback<Competition_Match>{
            override fun onResponse(
                call: Call<Competition_Match>,
                response: Response<Competition_Match>
            ) {
                if(response != null){
                    val listMatch = response.body()!!.matches
                    for(match in listMatch){
                        Log.d("Info : ", match.homeTeam.name + match.score.fullTime.home + ":" + match.awayTeam.name + match.score.fullTime.away)
                    }
                }
            }

            override fun onFailure(call: Call<Competition_Match>, t: Throwable) {
                Log.d("Failed", t.message.toString())
            }

        })
    }
}