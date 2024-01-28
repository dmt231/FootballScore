package com.example.footballscore.competitions.standings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballscore.adapter.adapterForLeague.AdapterStandings
import com.example.footballscore.competitions.list_competition.ListCompetitions
import com.example.footballscore.databinding.LayoutStandingBinding
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StandingsFragment(competitionId: Int) : Fragment() {
    private lateinit var viewBinding: LayoutStandingBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var standingAdapter: AdapterStandings
    private lateinit var listStandings: ArrayList<Table>
    private val idLeague: Int

    init {
        idLeague = competitionId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LayoutStandingBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        listStandings = ArrayList()
        listStandings.add(Table())
        setUpRecyclerView()
        getStandingData()
        return viewBinding.root
    }

    private fun getStandingData() {
        val call = apiInterface.getStandingsRecentForLeague(idLeague)
        call.enqueue(object : Callback<StandingsModels> {
            override fun onResponse(
                call: Call<StandingsModels>,
                response: Response<StandingsModels>
            ) {
                val result = response.body()!!.standings[0].table
                for (place in result) {
                    val position = listStandings.size
                    listStandings.add(position, place)
                    standingAdapter.notifyItemChanged(position)
                }
            }

            override fun onFailure(call: Call<StandingsModels>, t: Throwable) {
                Log.d("Error : ", t.message.toString())
            }

        })
    }

    private fun setUpRecyclerView() {
        viewBinding.recyclerViewStandings.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewStandings.layoutManager = layout
        standingAdapter = AdapterStandings(listStandings)
        viewBinding.recyclerViewStandings.adapter = standingAdapter
    }
}