package com.example.footballscore.competitions.standings

import android.annotation.SuppressLint
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.adapter.adapterForLeague.AdapterStandings
import com.example.footballscore.databinding.LayoutStandingBinding
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
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


class StandingsFragment(competitionId: Int, competitionImage : String, competitionName : String) : Fragment() {
    private lateinit var viewBinding: LayoutStandingBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var standingAdapter: AdapterStandings
    private lateinit var listStandings: ArrayList<Table>
    private val season  =  arrayOf<String>("2020-2021","2021-2022","2022-2023","2023-2024")
    private val idLeague: Int
    private val competitionName: String
    private val competitionImage: String
    init {
        idLeague = competitionId
        this.competitionImage = competitionImage
        this.competitionName = competitionName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LayoutStandingBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        listStandings = ArrayList()
        setUpRecyclerView()
        if(idLeague == 2000 || idLeague == 2001){
            getStandingDataRecent()
        }
        setUpData()
        setUpSpinner()
        viewBinding.spinnerSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                when(parentView.getItemAtPosition(position).toString()){
                    "2020-2021" ->{
                        getStandingDataBySeason(2020)
                        viewBinding.recyclerViewStandings.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2021-2022" ->{
                        getStandingDataBySeason(2021)
                        viewBinding.recyclerViewStandings.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2022-2023" ->{
                        getStandingDataBySeason(2022)
                        viewBinding.recyclerViewStandings.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2023-2024" -> {
                        Log.d("2023-2024 Selected ",  "True")
                        getStandingDataBySeason(2023)
                        viewBinding.recyclerViewStandings.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                }


            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Xử lý sự kiện khi không có mục nào được chọn trong Spinner
            }
        }
        return viewBinding.root
    }

    private fun setUpData() {
        viewBinding.standingsLeagueName.text = competitionName
        loadWithPlaceholder(viewBinding.imageLeagueStanding, competitionImage)
    }
    private fun loadWithPlaceholder(imageView: ImageView, url: String) {
        if (url.endsWith(".svg")) {
            loadSvgImage(imageView, url)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
    private fun loadSvgImage(imageView: ImageView, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream: InputStream = openConnection(url)
                val svg: SVG = SVG.getFromInputStream(inputStream)

                withContext(Dispatchers.Main) {
                    val drawable = PictureDrawable(svg.renderToPicture())
                    imageView.setImageDrawable(drawable)
                    imageView.visibility = View.VISIBLE
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.football_club)
                }
            } catch (e: SVGParseException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.football_club)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun openConnection(url: String): InputStream {
        val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        return connection.inputStream
    }
    private fun setUpSpinner(){
        val arrayList = season.toList()
        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.layout_select_season,arrayList)
        viewBinding.spinnerSeason.adapter = arrayAdapter

        val defaultSelectionIndex = arrayList.indexOf("2023-2024")
        viewBinding.spinnerSeason.setSelection(defaultSelectionIndex)
        if(idLeague == 2000 || idLeague == 2001){
            viewBinding.spinnerSeason.visibility = View.GONE
        }
    }
    private fun getStandingDataRecent() {
        val call = apiInterface.getStandingsRecentForLeague(idLeague)
        call.enqueue(object : Callback<StandingsModels> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<StandingsModels>,
                response: Response<StandingsModels>
            ) {
                val size = response.body()!!.standings.size
                if(size > 1){
                    for(standing in response.body()!!.standings){
                        val groupName = Table()
                        groupName.group = standing.group.toString()
                        listStandings.add(groupName)
                        val title = Table()
                        title.position = 0
                        listStandings.add(title)
                        for(place in standing.table){
                            listStandings.add(place)
                        }
                    }
                }else{
                    val result = response.body()!!.standings[0].table
                    val title = Table()
                    title.position = 0
                    listStandings.add(title)
                    for (place in result) {
                        listStandings.add(place)
                    }
                }
                viewBinding.progressBar.visibility = View.GONE
                standingAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<StandingsModels>, t: Throwable) {
                Log.d("Error : ", t.message.toString())
            }
        })
    }
    private fun getStandingDataBySeason(season : Int) {
        Log.d("idLeague", idLeague.toString())
        val call = apiInterface.getStandingsBySeasonForLeague(idLeague, season)
        call.enqueue(object : Callback<StandingsModels> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<StandingsModels>,
                response: Response<StandingsModels>
            ) {
                listStandings.clear()
                if(response.body()!!.competition.type == "CUP"){
                    for(standing in response.body()!!.standings){
                        val groupName = Table()
                        groupName.group = standing.group.toString()
                        listStandings.add(groupName)
                        val title = Table()
                        title.position = 0
                        listStandings.add(title)
                        for(place in standing.table){
                            listStandings.add(place)
                        }
                    }
                }
                else if (response.body()!!.competition.type == "LEAGUE"){
                    for(standing in response.body()!!.standings){
                        if(standing.type == "TOTAL"){
                            val result = standing.table
                            val title = Table()
                            title.position = 0
                            listStandings.add(title)
                            for (place in result) {
                                listStandings.add(place)
                            }
                        }
                    }
                }
                viewBinding.recyclerViewStandings.visibility = View.VISIBLE
                standingAdapter.notifyDataSetChanged()
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