package com.example.footballscore.competitions.matches

import android.annotation.SuppressLint
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.adapter.adapterForLeague.AdapterMatch
import com.example.footballscore.adapter.adapterForLeague.AdapterStandings
import com.example.footballscore.competitions.competion_match.Competition_Match
import com.example.footballscore.competitions.competion_match.Match_Of_Competition
import com.example.footballscore.competitions.standings.StandingsModels
import com.example.footballscore.competitions.standings.Table
import com.example.footballscore.databinding.MatchesOfLeagueLayoutBinding
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

class MatchesOfLeagueFragment(competitionId: Int, competitionImage : String, competitionName : String) : Fragment() {
    private lateinit var viewBinding : MatchesOfLeagueLayoutBinding
    private lateinit var adapterMath : AdapterMatch
    private lateinit var listMatch : ArrayList<Match_Of_Competition>
    private lateinit var apiInterface: ApiInterface
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
        viewBinding = MatchesOfLeagueLayoutBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        listMatch = ArrayList()
        setUpRecyclerView()
        setUpSpinner()
        setUpData()
        viewBinding.spinnerSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                when(parentView.getItemAtPosition(position).toString()){
                    "2020-2021" ->{
                        getMatchOfLeagueBySeason(2020)
                        viewBinding.recyclerViewMatches.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2021-2022" ->{
                        getMatchOfLeagueBySeason(2021)
                        viewBinding.recyclerViewMatches.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2022-2023" ->{
                        getMatchOfLeagueBySeason(2022)
                        viewBinding.recyclerViewMatches.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                    }
                    "2023-2024" -> {
                        Log.d("2023-2024 Selected ",  "True")
                        getMatchOfLeagueBySeason(2023)
                        viewBinding.recyclerViewMatches.visibility = View.GONE
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
        viewBinding.matchesLeagueName.text = competitionName
        loadWithPlaceholder(viewBinding.imageLeagueMatches, competitionImage)
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
    private fun setUpRecyclerView() {
        viewBinding.recyclerViewMatches.setHasFixedSize(false)
        val layout = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewMatches.layoutManager = layout
        adapterMath = AdapterMatch(listMatch)
        viewBinding.recyclerViewMatches.adapter = adapterMath
    }
    private fun setUpSpinner(){
        val arrayList = season.toList()
        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(requireContext(),
            R.layout.layout_select_season,arrayList)
        viewBinding.spinnerSeason.adapter = arrayAdapter

        val defaultSelectionIndex = arrayList.indexOf("2023-2024")
        viewBinding.spinnerSeason.setSelection(defaultSelectionIndex)
        if(idLeague == 2000 || idLeague == 2001){
            viewBinding.spinnerSeason.visibility = View.GONE
        }
    }
    private fun getMatchOfLeagueBySeason(season : Int) {
        Log.d("idLeague", idLeague.toString())
        val call = apiInterface.getMatchFromCompetitionBySeason(idLeague, season)
        call.enqueue(object : Callback<Competition_Match>{
            override fun onResponse(
                call: Call<Competition_Match>,
                response: Response<Competition_Match>
            ) {
                listMatch.clear()
                val roundRecent = response.body()!!.matches[0].season!!.currentMatchday
                val result = response.body()!!.matches
                var roundNumber = 1;
                val roundMatch = Match_Of_Competition()
                roundMatch.round = roundNumber
                listMatch.add(roundMatch)
                for(match in result){
                    if(match.matchday!! > roundNumber){
                        roundNumber++
                        val roundMatchNext = Match_Of_Competition()
                        roundMatchNext.round = roundNumber
                        listMatch.add(roundMatchNext)
                        listMatch.add(match)
                    }
                    else{
                        listMatch.add(match)
                    }
                }
                var scrollPosition = 0;
                for(match in listMatch){
                    if(match.round == roundRecent){
                       scrollPosition = listMatch.indexOf(match) + 6
                        break
                    }
                }
                adapterMath.notifyDataSetChanged()
                viewBinding.recyclerViewMatches.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // Loại bỏ lắng nghe sau khi layout hoàn thành
                        viewBinding.recyclerViewMatches.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        // Cuộn RecyclerView đến vị trí mong muốn
                        viewBinding.recyclerViewMatches.scrollToPosition(scrollPosition)
                    }
                })
                viewBinding.recyclerViewMatches.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<Competition_Match>, t: Throwable) {
                Log.d("Failure ", "True")
            }

        })
    }
}