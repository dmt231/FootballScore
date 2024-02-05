package com.example.footballscore.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballscore.R
import com.example.footballscore.adapter.adapterForLeague.AdapterStandings
import com.example.footballscore.adapter.adapterForMatchOfTeam.adapterMatchOfTeam
import com.example.footballscore.databinding.MatchOfClubBinding
import com.example.footballscore.matches.matchModel.Match
import com.example.footballscore.matches.matchModel.Matche
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MatchOfClubFragment(clubId : Int) : Fragment() {
    private lateinit var viewBinding : MatchOfClubBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var matchAdapter : adapterMatchOfTeam
    private lateinit var listMatch : ArrayList<Matche>
    private val season  =  arrayOf("2020-2021","2021-2022","2022-2023","2023-2024")
    private var clubId : Int
    init {
        this.clubId = clubId
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchOfClubBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        viewBinding.spinnerSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                when(parentView.getItemAtPosition(position).toString()){
                    "2020-2021" ->{
                        getMatchOfClubBySeason(2020)
                        viewBinding.recyclerViewMatch.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2021-2022" ->{
                        getMatchOfClubBySeason(2021)
                        viewBinding.recyclerViewMatch.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2022-2023" ->{
                        getMatchOfClubBySeason(2022)
                        viewBinding.recyclerViewMatch.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2023-2024" -> {
                        getMatchOfClubBySeason(2023)
                        viewBinding.recyclerViewMatch.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                }


            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Xử lý sự kiện khi không có mục nào được chọn trong Spinner
            }
        }
        setUpRecyclerView()
        setUpSpinner()
        return viewBinding.root
    }
    private fun setUpSpinner(){
        val arrayList = season.toList()
        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(requireContext(),
            R.layout.layout_select_season,arrayList)
        viewBinding.spinnerSeason.adapter = arrayAdapter

        val defaultSelectionIndex = arrayList.indexOf("2023-2024")
        viewBinding.spinnerSeason.setSelection(defaultSelectionIndex)
    }
    private fun setUpRecyclerView() {
        viewBinding.recyclerViewMatch.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewMatch.layoutManager = layout
        listMatch = ArrayList()
        matchAdapter = adapterMatchOfTeam(listMatch)
        viewBinding.recyclerViewMatch.adapter = matchAdapter
    }
    private fun getMatchOfClubBySeason(season : Int){
        val call = apiInterface.getMatchForSeasonByTeamsById(clubId, season)
        call.enqueue(object : retrofit2.Callback<Match>{
            override fun onResponse(call: Call<Match>, response: Response<Match>) {
                listMatch.clear()
                val result = response.body()!!.matches
                for(match in result){
                    listMatch.add(match)
                }
                matchAdapter.notifyDataSetChanged()
                viewBinding.recyclerViewMatch.visibility = View.VISIBLE
                viewBinding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<Match>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}