package com.example.footballscore.competitions.top_score

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
import com.example.footballscore.adapter.adapterForLeague.AdapterTopScorers
import com.example.footballscore.competitions.standings.StandingsModels
import com.example.footballscore.competitions.standings.Table
import com.example.footballscore.competitions.top_score.topScoreModel.Scorer
import com.example.footballscore.competitions.top_score.topScoreModel.TopScoreModel
import com.example.footballscore.databinding.MatchesOfLeagueLayoutBinding
import com.example.footballscore.databinding.TopScoreLayoutBinding
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

class TopScoreFragment(competitionId: Int, competitionImage : String, competitionName : String) : Fragment() {
    private lateinit var viewBinding : TopScoreLayoutBinding
    private lateinit var apiInterface: ApiInterface
    private val season  =  arrayOf<String>("2020-2021","2021-2022","2022-2023","2023-2024")
    private lateinit var adapter: AdapterTopScorers
    private lateinit var listScorers: ArrayList<Scorer>
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
        viewBinding = TopScoreLayoutBinding.inflate(inflater,container,false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        setUpData()
        setUpSpinner()
        setUpRecyclerView()
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
                        viewBinding.recyclerViewTopScores.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2021-2022" ->{
                        getStandingDataBySeason(2021)
                        viewBinding.recyclerViewTopScores.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2022-2023" ->{
                        getStandingDataBySeason(2022)
                        viewBinding.recyclerViewTopScores.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2023-2024" -> {
                        getStandingDataBySeason(2023)
                        viewBinding.recyclerViewTopScores.visibility = View.GONE
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
        viewBinding.topScoresLeagueName.text = competitionName
        loadWithPlaceholder(viewBinding.imageLeagueTopScores, competitionImage)
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
        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(requireContext(),
            R.layout.layout_select_season,arrayList)
        viewBinding.spinnerSeason.adapter = arrayAdapter

        val defaultSelectionIndex = arrayList.indexOf("2023-2024")
        viewBinding.spinnerSeason.setSelection(defaultSelectionIndex)
    }
    private fun setUpRecyclerView() {
        viewBinding.recyclerViewTopScores.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewTopScores.layoutManager = layout
        listScorers = ArrayList()
        adapter = AdapterTopScorers(listScorers)
        viewBinding.recyclerViewTopScores.adapter = adapter
    }
    private fun getStandingDataBySeason(season : Int) {
        Log.d("idLeague", idLeague.toString())
        val call = apiInterface.getTopScorersByLeague(idLeague, season)
        call.enqueue(object : Callback<TopScoreModel> {
            override fun onResponse(call: Call<TopScoreModel>, response: Response<TopScoreModel>) {
                    listScorers.clear()
                    val result = response.body()!!.scorers
                    for(scorer in result){
                        listScorers.add(scorer)
                    }
                    adapter.notifyDataSetChanged()
                    viewBinding.recyclerViewTopScores.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<TopScoreModel>, t: Throwable) {
                Log.d("Error : ", t.message.toString())
            }

        })
    }
}