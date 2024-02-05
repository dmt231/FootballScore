package com.example.footballscore.adapter.adapterForMatchOfTeam

import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.databinding.NestedMatchLayoutForTeamBinding
import com.example.footballscore.matches.matchModel.Matche
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

class adapterMatchOfTeam(listMatch : ArrayList<Matche>) : RecyclerView.Adapter<ViewHolderMatchTeam>() {
    private val listMatch : ArrayList<Matche>
    init {
        this.listMatch = listMatch
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMatchTeam {
        val binding = NestedMatchLayoutForTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderMatchTeam(binding)
    }

    override fun getItemCount(): Int {
        return listMatch.size
    }

    override fun onBindViewHolder(holder: ViewHolderMatchTeam, position: Int) {
        val model = listMatch[position]
        loadWithPlaceholder(holder.viewBinding.imageHome, model.homeTeam!!.crest)
        loadWithPlaceholder(holder.viewBinding.imageAway, model.awayTeam!!.crest)
        holder.viewBinding.nameHome.text = model.homeTeam.shortName
        holder.viewBinding.nameAway.text = model.awayTeam.shortName
        loadWithPlaceholder(holder.viewBinding.imageLeague, model.competition.emblem)
        if(getDateByYearMonthDay(model.utcDate!!) == getDateRecent()){
            when(model.status){
                "FINISHED" -> {
                    val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                    val awayScore = model.score!!.fullTime.away.toString().toDouble().toInt()
                    holder.viewBinding.apply {
                        statusMatch.text = "FT"
                        timeMatch.text = getDateByHourMinutes(model.utcDate)
                        scoreHome.text = homeScore.toString()
                        scoreAway.text = awayScore.toString()
                        scoreHome.visibility = View.VISIBLE
                        scoreAway.visibility = View.VISIBLE
                    }
                }
                "TIMED" -> {
                    holder.viewBinding.apply {
                        timeMatch.text = getDateByHourMinutes(model.utcDate)
                        statusMatch.text = "-"
                        scoreHome.visibility = View.GONE
                        scoreAway.visibility = View.GONE
                    }

                }
                "SCHEDULED" -> {
                    holder.viewBinding.timeMatch.text = getDateByDayMonthYear(model.utcDate)
                    holder.viewBinding.statusMatch.text = getDateByHourMinutes(model.utcDate)
                    holder.viewBinding.scoreHome.visibility = View.GONE
                    holder.viewBinding.scoreAway.visibility = View.GONE
                }
                "IN_PLAY" -> {
                    val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                    val awayScore = model.score!!.fullTime.away.toString().toDouble().toInt()
                    holder.viewBinding.apply {
                        statusMatch.text = "In Play"
                        timeMatch.text = getDateByHourMinutes(model.utcDate)
                        scoreHome.text = homeScore.toString()
                        scoreAway.text = awayScore.toString()
                        scoreHome.visibility = View.VISIBLE
                        scoreAway.visibility = View.VISIBLE
                    }
                }
                "POSTPONED" -> {
                    holder.viewBinding.apply {
                        scoreHome.visibility = View.GONE
                        scoreAway.visibility = View.GONE
                        scoreHome.text = "-"
                        scoreAway.text = "-"
                    }

                }
            }
        }else{
            when(model.status){
                "FINISHED" -> {
                    val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                    val awayScore = model.score.fullTime.away.toString().toDouble().toInt()
                    holder.viewBinding.apply {
                        statusMatch.text = "FT"
                        timeMatch.text = getDateByDayMonthYear(model.utcDate)
                        scoreHome.visibility = View.VISIBLE
                        scoreAway.visibility = View.VISIBLE
                        scoreHome.text = homeScore.toString()
                        scoreAway.text = awayScore.toString()
                    }
                }
                "TIMED" -> {
                    holder.viewBinding.timeMatch.text = getDateByDayMonthYear(model.utcDate)
                    holder.viewBinding.statusMatch.text = getDateByHourMinutes(model.utcDate)
                    holder.viewBinding.scoreHome.visibility = View.GONE
                    holder.viewBinding.scoreAway.visibility = View.GONE
                }
                "SCHEDULED" -> {
                    holder.viewBinding.timeMatch.text = getDateByDayMonthYear(model.utcDate)
                    holder.viewBinding.statusMatch.text = getDateByHourMinutes(model.utcDate)
                    holder.viewBinding.scoreHome.visibility = View.GONE
                    holder.viewBinding.scoreAway.visibility = View.GONE
                }
                "POSTPONED" -> {
                    holder.viewBinding.apply {
                        scoreHome.text = "-"
                        scoreAway.text = "-"
                        scoreHome.visibility = View.GONE
                        scoreAway.visibility = View.GONE
                    }
                }
                "IN_PLAY" -> {
                    holder.viewBinding.statusMatch.text = "In Play"
                    holder.viewBinding.timeMatch.text = getDateByHourMinutes(model.utcDate)
                    val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                    val awayScore = model.score!!.fullTime.away.toString().toDouble().toInt()
                    holder.viewBinding.apply {
                        scoreHome.visibility = View.VISIBLE
                        scoreAway.visibility = View.VISIBLE
                        scoreHome.text = homeScore.toString()
                        scoreAway.text = awayScore.toString()
                    }

                }
            }
        }

    }
    private fun loadWithPlaceholder(imageView: ImageView, url: String) {
        if (url.endsWith(".svg")) {
            loadSvgImage(imageView, url)
        } else {
            // If it's not an SVG, use Coil as usual
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
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
    private fun getDateRecent(): String {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, day)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(selectedDate.time)
    }
    private fun getDateByYearMonthDay(dateString : String) : String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val matchTime = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(matchTime)
    }
    private fun getDateByDayMonthYear(dateString : String) : String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val matchTime = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return outputFormat.format(matchTime)
    }
    private fun getDateByHourMinutes(dateString : String) : String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val matchTime = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return outputFormat.format(matchTime)
    }
}
class ViewHolderMatchTeam(viewBinding : NestedMatchLayoutForTeamBinding) : RecyclerView.ViewHolder(viewBinding.root){
    var viewBinding : NestedMatchLayoutForTeamBinding
    init {
        this.viewBinding = viewBinding
    }
}