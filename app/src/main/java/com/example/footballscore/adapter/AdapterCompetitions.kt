package com.example.footballscore.adapter

import android.graphics.drawable.PictureDrawable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.competitions.list_competition.Competition
import com.example.footballscore.databinding.EachItemLeaguesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdapterCompetitions(listCompetitions : ArrayList<Competition>, onClickListener: OnClickListener) : RecyclerView.Adapter<ViewHolderListMatch>() {
    private var listCompetition: ArrayList<Competition>
    private var statusDropdown = false
    private var onClickListener : OnClickListener
    init {
        this.listCompetition = listCompetitions
        this.onClickListener = onClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderListMatch {
        var binding = EachItemLeaguesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderListMatch(binding)
    }

    override fun getItemCount(): Int {
        return listCompetition.size
    }

    override fun onBindViewHolder(holder: ViewHolderListMatch, position: Int) {
        val competition = listCompetition[position]
        val leagueImage = competition.emblem
        loadWithPlaceholder(holder.viewBinding.eachItemLeaguesImage, leagueImage)
        holder.viewBinding.leaguesName.text = competition.name
        holder.viewBinding.dropDownButton.setOnClickListener {
           if(!statusDropdown){
               statusDropdown = true
               onClickListener.onClickListener(competition.id)
               holder.viewBinding.dropDownButton.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
               holder.viewBinding.childRecyclerView.visibility = View.VISIBLE
           }
            else{
               statusDropdown = false
               holder.viewBinding.dropDownButton.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
               holder.viewBinding.childRecyclerView.visibility = View.INVISIBLE
           }
        }
    }
    private fun loadWithPlaceholder(imageView: ImageView, url: String) {
        if (url.endsWith(".svg")) {
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

    interface OnClickListener{
        fun onClickListener(competitionId : Int)
    }

}
class ViewHolderListMatch(viewBinding : EachItemLeaguesBinding) : RecyclerView.ViewHolder(viewBinding.root){
    var viewBinding : EachItemLeaguesBinding
    init {
        this.viewBinding = viewBinding
    }
}