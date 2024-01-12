package com.example.footballscore.competitions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballscore.adapter.AdapterLeague
import com.example.footballscore.competitions.list_competition.Competition
import com.example.footballscore.competitions.list_competition.ListCompetitions
import com.example.footballscore.databinding.LeagueLayoutBinding
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeagueFragment : Fragment() {
    private lateinit var  viewBinding : LeagueLayoutBinding
    private lateinit var adapterLeague : AdapterLeague
    private lateinit var  apiInterface : ApiInterface
    private lateinit var listLeague : ArrayList<Competition>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LeagueLayoutBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        listLeague = ArrayList()
        onSetUpRecyclerView()
        getDataFromLeague()
        return viewBinding.root
    }

    private fun getDataFromLeague() {
        val call = apiInterface.getListCompetitions()
        call.enqueue(object : Callback<ListCompetitions>{
            override fun onResponse(
                call: Call<ListCompetitions>,
                response: Response<ListCompetitions>
            ) {
               val result = response.body()!!.competitions
                for(league in result){
                    val position = listLeague.size
                    listLeague.add(league)
                    Log.d("League : ", league.name)
                }
                adapterLeague.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ListCompetitions>, t: Throwable) {
               Log.d("Error :", "Call Failed")
            }

        })
    }

    private fun onSetUpRecyclerView() {
        viewBinding.leagueRecyclerView.setHasFixedSize(true)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.leagueRecyclerView.layoutManager = layout
        adapterLeague = AdapterLeague(listLeague)
        viewBinding.leagueRecyclerView.adapter = adapterLeague
    }

}