package com.example.footballscore.adapter

import android.graphics.drawable.PictureDrawable
import android.opengl.Visibility
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.competitions.competion_match.Match_Of_Competition
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
    private var onClickListener : OnClickListener
    private var listMatch : ArrayList<Match_Of_Competition> = ArrayList()
    init {
        this.listCompetition = listCompetitions
        this.onClickListener = onClickListener
    }
    fun updateListMatch(listMatchResult : ArrayList<Match_Of_Competition>, position: Int){
        this.listMatch.clear()
        for(match in listMatchResult){
            this.listMatch.add(match)
            Log.d("Match : ", match.homeTeam.name + match.score.fullTime.home + ":" + match.awayTeam.name + match.score.fullTime.away)
        }
        notifyItemChanged(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderListMatch {
        val binding = EachItemLeaguesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderListMatch(binding)
    }

    override fun getItemCount(): Int {
        return listCompetition.size
    }

    override fun onBindViewHolder(holder: ViewHolderListMatch, position: Int) {
        Log.d("Holder Position", position.toString())
        val competition = listCompetition[position]
        val leagueImage = competition.emblem
        loadWithPlaceholder(holder.viewBinding.eachItemLeaguesImage, leagueImage)
        holder.viewBinding.leaguesName.text = competition.name
        val statusDropdown = competition.hasChildMatch
        if(statusDropdown){
            Log.d("RecyclerView Child", "true")
            holder.viewBinding.expandableLayout.visibility = View.VISIBLE
            holder.viewBinding.dropDownButton.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            val adapter = AdapterMatchOfCompetition(listMatch)
            val layout = LinearLayoutManager(holder.itemView.context)
            holder.viewBinding.childRecyclerView.layoutManager = layout
            holder.viewBinding.childRecyclerView.setHasFixedSize(true)
            holder.viewBinding.childRecyclerView.adapter = adapter
        }else{
            holder.viewBinding.expandableLayout.visibility = View.GONE
            holder.viewBinding.dropDownButton.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
        }
        holder.viewBinding.dropDownButton.setOnClickListener {
           if(!statusDropdown){
               competition.hasChildMatch = true
               onClickListener.onClickListener(competition.id, holder.adapterPosition)
           }
            else{
               competition.hasChildMatch = false
               notifyItemChanged(holder.adapterPosition)
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
        fun onClickListener(competitionId : Int, position: Int)
    }

}
class ViewHolderListMatch(viewBinding : EachItemLeaguesBinding) : RecyclerView.ViewHolder(viewBinding.root){
    var viewBinding : EachItemLeaguesBinding
    init {
        this.viewBinding = viewBinding
    }
}