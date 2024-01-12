package com.example.footballscore.adapter

import android.graphics.drawable.PictureDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.competitions.list_competition.Competition
import com.example.footballscore.databinding.EachLeagueLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdapterLeague(listCompetitions : ArrayList<Competition>) : RecyclerView.Adapter<AdapterLeagueViewHolder>() {
    private var listCompetition : ArrayList<Competition>
    init {
        this.listCompetition = listCompetitions
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterLeagueViewHolder {
        val viewBinding = EachLeagueLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterLeagueViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
       return listCompetition.size
    }

    override fun onBindViewHolder(holder: AdapterLeagueViewHolder, position: Int) {
        val league = listCompetition[position]
        Log.d("League : ", league.name)
        holder.viewBinding.leaguesName.text = league.name
        loadWithPlaceholder(holder.viewBinding.eachItemLeaguesImage, league.emblem)
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
}
class AdapterLeagueViewHolder(viewBinding : EachLeagueLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root){
    var viewBinding : EachLeagueLayoutBinding
    init {
        this.viewBinding = viewBinding
    }
}