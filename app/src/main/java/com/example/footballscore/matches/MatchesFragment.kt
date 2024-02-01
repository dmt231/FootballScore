package com.example.footballscore.matches

import android.annotation.SuppressLint
import android.app.DatePickerDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballscore.adapter.AdapterCompetitions
import com.example.footballscore.adapter.AdapterMatchHorizontal
import com.example.footballscore.competitions.competion_match.Competition_Match
import com.example.footballscore.competitions.competion_match.Match_Of_Competition
import com.example.footballscore.competitions.list_competition.Competition
import com.example.footballscore.competitions.list_competition.ListCompetitions

import com.example.footballscore.databinding.MatchesLayoutBinding
import com.example.footballscore.matches.matchModel.Match
import com.example.footballscore.matches.matchModel.Matche
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList

class MatchesFragment: Fragment() {
    private lateinit var  viewBinding : MatchesLayoutBinding
    private lateinit var  apiInterface : ApiInterface
    private lateinit var listMatchHorizontal : ArrayList<Matche>
    private lateinit var adapterHorizontal : AdapterMatchHorizontal
    private lateinit var listCompetitions : ArrayList<Competition>
    private lateinit var mainAdapter : AdapterCompetitions
    private var dateString : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchesLayoutBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        listMatchHorizontal = ArrayList()
        listCompetitions = ArrayList()
        setDate()
        onSetUpRecyclerViewHorizontal()
        onSetUpMainRecyclerView()
        getMatchRecent()
        getListCompetition()
        viewBinding.calendarPick.setOnClickListener{
            openDatePicker()
        }
        return viewBinding.root
    }

    private fun getListCompetition() {
        val call = apiInterface.getListCompetitions()
        call.enqueue(object : Callback<ListCompetitions>{
            override fun onResponse(
                call: Call<ListCompetitions>,
                response: Response<ListCompetitions>
            ) {
               val result = response.body()!!.competitions
                for(league in result){
                    Log.d("League Name : ", league.name)
                    listCompetitions.add(league)
                    mainAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ListCompetitions>, t: Throwable) {
                Log.d("Failed", t.message.toString())
            }

        })
    }

    private fun onSetUpMainRecyclerView() {
        viewBinding.RecyclerViewCategories.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.RecyclerViewCategories.layoutManager = layout
        mainAdapter = AdapterCompetitions(listCompetitions, object : AdapterCompetitions.OnClickListener{
            override fun onClickListener(competitionId: Int, position : Int) {
                Log.d("dateString", dateString.toString())
                getCompetitionMatch(competitionId, dateString!!, object : OnCallBackFromAPI{
                    override fun callBack(listMatch: ArrayList<Match_Of_Competition>) {
                        mainAdapter.updateListMatch(listMatch, position)
                    }

                })
            }
        })

        viewBinding.RecyclerViewCategories.adapter = mainAdapter
    }


    private fun getMatchRecent() {

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
        if(day.toString().length < 2){
            viewBinding.txtDay.text = "0$day"
        }
        else {
            viewBinding.txtDay.text = day.toString()
        }
    }

    private fun getCompetitionMatch(competitionId : Int, date : String, onCallBackFromAPI: OnCallBackFromAPI){
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        val call = apiInterface.getMatchFromCompetitionByDate(competitionId,date,date)
        call.enqueue(object: Callback<Competition_Match>{
            override fun onResponse(
                call: Call<Competition_Match>,
                response: Response<Competition_Match>
            ) {
                val listMatch = response.body()!!.matches
                for(match in listMatch){
                    Log.d("Info : ", match.homeTeam!!.name + match.score!!.fullTime.home + ":" + match.awayTeam!!.name + match.score.fullTime.away)
                }
                onCallBackFromAPI.callBack(listMatch)
            }

            override fun onFailure(call: Call<Competition_Match>, t: Throwable) {
                Log.d("Failed", t.message.toString())
            }

        })
    }
    private interface OnCallBackFromAPI{
        fun callBack(listMatch : ArrayList<Match_Of_Competition>)
    }
}