package com.example.footballscore.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.PictureDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.databinding.RecylerviewHorizontalBinding
import com.example.footballscore.matches.matchModel.Matche
import coil.load

import coil.transform.CircleCropTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterMatchHorizontal(listMatch: ArrayList<Matche>) :
    RecyclerView.Adapter<AdapterMatchHorizontal.ViewHolderHorizontal>() {
    private var listMatch: ArrayList<Matche>

    init {
        this.listMatch = listMatch
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHorizontal {
        var binding =
            RecylerviewHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderHorizontal(binding)
    }

    override fun getItemCount(): Int {
        return listMatch.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolderHorizontal, position: Int) {
        val match = listMatch[position]
        val leagueImage = match.competition.emblem
        val homeImage = match.homeTeam.crest
        val awayImage = match.awayTeam.crest
        Log.d("Home : ",match.homeTeam.crest)
        Log.d("Away : ",match.awayTeam.crest)
        loadWithPlaceholder(holder.viewBinding.LeagueImage, leagueImage)
        loadWithPlaceholder(holder.viewBinding.HomeImage, homeImage)
        loadWithPlaceholder(holder.viewBinding.AwayImage, awayImage)
        when(match.status){
            "FINISHED" -> {
                holder.viewBinding.statusMatch.text = "FT"
                val homeScore = match.score.fullTime.home
                val awayScore = match.score.fullTime.away
                val finalScore = "$homeScore-$awayScore"
                holder.viewBinding.score.text = finalScore}
            "TIMED" -> {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val matchTime = inputFormat.parse(match.utcDate)
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val matchTimeString = outputFormat.format(matchTime)
                holder.viewBinding.statusMatch.text= matchTimeString
                holder.viewBinding.score.visibility = View.GONE
            }
            "IN_PLAY" -> {
                holder.viewBinding.statusMatch.text = "In Play"
                val homeScore = match.score.fullTime.home
                val awayScore = match.score.fullTime.away
                val finalScore = "$homeScore-$awayScore"
                holder.viewBinding.score.text = finalScore
            }
        }
    }


    private fun loadWithPlaceholder(imageView: ImageView, url: String) {
        if (url.endsWith(".svg")) {
            Log.d("URL end ", url)
            loadSvgImage(imageView, url)
        } else {
            // If it's not an SVG, use Coil as usual
            imageView.load(url) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.football_club)
                transformations()
            }
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



    class ViewHolderHorizontal(binding: RecylerviewHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var viewBinding: RecylerviewHorizontalBinding

        init {
            this.viewBinding = binding
        }
    }
}
