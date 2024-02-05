package com.example.footballscore.teams

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.adapter.adapterForMatchOfTeam.adapterMatchOfTeam
import com.example.footballscore.adapter.adapterPlayer.AdapterPlayer
import com.example.footballscore.databinding.SquadLayoutBinding
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import com.example.footballscore.teams.playerModel.PlayerModel
import com.example.footballscore.teams.teamsModel.Squad
import com.example.footballscore.teams.teamsModel.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class SquadFragment(clubId : Int) : Fragment() {
    private lateinit var viewBinding : SquadLayoutBinding
    private lateinit var apiInterface: ApiInterface
    private var clubId : Int
    private var managerName : String? = null
    private var managerBirthday : String? =null
    private var nationCodeManager : String? = null
    private var listForward : ArrayList<Squad>? =null
    private var listMidfielder : ArrayList<Squad>? =null
    private var listDefender : ArrayList<Squad>? =null
    private var listGoalkeeper : ArrayList<Squad>? =null
    private var adapterForward  : AdapterPlayer ? =null
    private var adapterMidfield : AdapterPlayer ? =null
    private var adapterDefense: AdapterPlayer ? =null
    private var adapterGoalkeep: AdapterPlayer ? =null
    init {
        this.clubId = clubId
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = SquadLayoutBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        listForward = ArrayList()
        listDefender = ArrayList()
        listMidfielder = ArrayList()
        listDefender = ArrayList()
        setUpRecyclerViewForward()
        setUpRecyclerViewMidfielder()
        setUpRecyclerViewDefense()
        setUpRecyclerViewGoalkeeper()
        getTeamData()
        return viewBinding.root
    }
    private fun getTeamData(){
        val call = apiInterface.getTeamsById(clubId)
        call.enqueue(object : Callback<Team>{
            override fun onResponse(call: Call<Team>, response: Response<Team>) {
                val result = response.body()
                managerName = result!!.coach.name
                managerBirthday = result!!.coach.dateOfBirth
                nationCodeManager = result!!.coach.nationality
                setUpDataForManager()
                val squad = result.squad
                for(player in squad){
                    when(player.position){
                        "Offence" ->{
                            listForward!!.add(player)
                        }
                        "Midfield" -> {
                            listMidfielder!!.add(player)
                        }
                        "Defence" -> {
                            listDefender!!.add(player)
                        }
                        "Goalkeeper" -> {
                            listGoalkeeper!!.add(player)
                        }
                    }
                }
                adapterForward!!.notifyDataSetChanged()
                adapterMidfield!!.notifyDataSetChanged()
                adapterDefense!!.notifyDataSetChanged()
                adapterGoalkeep!!.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<Team>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setUpDataForManager(){
        viewBinding.nameCoach.text = managerName
        viewBinding.nationCoachName.text = nationCodeManager
        viewBinding.Birthday.text = managerBirthday
    }
    private fun setUpRecyclerViewForward() {
        viewBinding.recyclerViewForward.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewForward.layoutManager = layout
        listForward = ArrayList()
        adapterForward = AdapterPlayer(listForward!!)
        viewBinding.recyclerViewForward.adapter = adapterForward
    }
    private fun setUpRecyclerViewMidfielder() {
        viewBinding.recyclerViewMidfield.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewMidfield.layoutManager = layout
        listMidfielder = ArrayList()
        adapterMidfield = AdapterPlayer(listMidfielder!!)
        viewBinding.recyclerViewMidfield.adapter = adapterMidfield
    }
    private fun setUpRecyclerViewDefense() {
        viewBinding.recyclerViewDefender.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewDefender.layoutManager = layout
        listDefender = ArrayList()
        adapterDefense = AdapterPlayer(listDefender!!)
        viewBinding.recyclerViewDefender.adapter = adapterDefense
    }
    private fun setUpRecyclerViewGoalkeeper() {
        viewBinding.recyclerViewGoalKeeper.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewGoalKeeper.layoutManager = layout
        listGoalkeeper = ArrayList()
        adapterGoalkeep = AdapterPlayer(listGoalkeeper!!)
        viewBinding.recyclerViewGoalKeeper.adapter = adapterGoalkeep
    }
}