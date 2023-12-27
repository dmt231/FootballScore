package com.example.footballscore.matches

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.footballscore.competitions.competion_match.Competition_Match

import com.example.footballscore.databinding.MatchesLayoutBinding
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MatchesFragment: Fragment() {
    private lateinit var  viewBinding : MatchesLayoutBinding
    private lateinit var  apiInterface : ApiInterface
    private var date : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchesLayoutBinding.inflate(inflater, container, false)
        getCompetitionMatch()
        viewBinding.calendarPick.setOnClickListener{
            openDatePicker()
        }
        return viewBinding.root
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
            date = formattedDate
        }, year,month, day )
        datePickerDialog.show()
    }
    private fun setDate(){
        val currentDate = Calendar.getInstance()
        val month = currentDate.get(Calendar.DAY_OF_MONTH)
        viewBinding.txtDay.text = month.toString()
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