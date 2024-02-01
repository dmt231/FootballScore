package com.example.footballscore.adapter.adapterForLeague

import android.annotation.SuppressLint
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.competitions.competion_match.Match_Of_Competition
import com.example.footballscore.databinding.NestedMatchLayoutBinding
import com.example.footballscore.databinding.RoundLayoutBinding
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

class AdapterMatch(listMatch : ArrayList<Match_Of_Competition>) : RecyclerView.Adapter<ViewHolder>() {
    private val listMatch : ArrayList<Match_Of_Competition>
    private val roundView : Int = 0
    private val matchView : Int = 1
    init {
        this.listMatch = listMatch
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            roundView -> {
                val binding = RoundLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderRoundName(binding)
            }
            else -> {
                val binding = NestedMatchLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderMatch(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return listMatch.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)){
            roundView ->{
                val roundViewHolder = holder as ViewHolderRoundName
                roundViewHolder.viewBinding.nameRound.text = "Round " + listMatch[position].round.toString()
            }
            matchView -> {
                val matchViewHolder = holder as ViewHolderMatch
                val model = listMatch[position]
                loadWithPlaceholder(holder.viewBinding.imageHome, model.homeTeam!!.crest)
                loadWithPlaceholder(holder.viewBinding.imageAway, model.awayTeam!!.crest)
                matchViewHolder.viewBinding.nameHome.text = model.homeTeam.name
                matchViewHolder.viewBinding.nameAway.text = model.awayTeam.name
                if(getDateByYearMonthDay(model.utcDate!!) == getDateRecent()){
                    when(model.status){
                        "FINISHED" -> {
                            matchViewHolder.viewBinding.statusMatch.text = "FT"
                            matchViewHolder.viewBinding.timeMatch.text = getDateByHourMinutes(model.utcDate)
                            val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                            val awayScore = model.score!!.fullTime.away.toString().toDouble().toInt()
                            matchViewHolder.viewBinding.scoreHome.text = homeScore.toString()
                            matchViewHolder.viewBinding.scoreAway.text = awayScore.toString()
                        }
                        "TIMED" -> {
                            matchViewHolder.viewBinding.timeMatch.text = getDateByHourMinutes(model.utcDate)
                            matchViewHolder.viewBinding.statusMatch.text = "-"
                        }
                        "IN_PLAY" -> {
                            matchViewHolder.viewBinding.statusMatch.text = "In Play"
                            matchViewHolder.viewBinding.timeMatch.text = getDateByHourMinutes(model.utcDate)
                            val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                            val awayScore = model.score!!.fullTime.away.toString().toDouble().toInt()
                            matchViewHolder.viewBinding.scoreHome.text = homeScore.toString()
                            matchViewHolder.viewBinding.scoreAway.text = awayScore.toString()
                        }
                        "POSTPONED" -> {
                            matchViewHolder.viewBinding.scoreHome.text = "-"
                            matchViewHolder.viewBinding.scoreAway.text = "-"
                        }
                    }
                }else{
                    when(model.status){
                        "FINISHED" -> {
                            matchViewHolder.viewBinding.statusMatch.text = "FT"
                            matchViewHolder.viewBinding.timeMatch.text = getDateByDayMonthYear(model.utcDate)
                            val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                            val awayScore = model.score.fullTime.away.toString().toDouble().toInt()
                            matchViewHolder.viewBinding.scoreHome.text = homeScore.toString()
                            matchViewHolder.viewBinding.scoreAway.text = awayScore.toString()
                        }
                        "TIMED" -> {
                            matchViewHolder.viewBinding.timeMatch.text = getDateByDayMonthYear(model.utcDate)
                            matchViewHolder.viewBinding.statusMatch.text = getDateByHourMinutes(model.utcDate)
                        }
                        "POSTPONED" -> {
                            matchViewHolder.viewBinding.scoreHome.text = "-"
                            matchViewHolder.viewBinding.scoreAway.text = "-"
                        }
                        "IN_PLAY" -> {
                            matchViewHolder.viewBinding.statusMatch.text = "In Play"
                            holder.viewBinding.timeMatch.text = getDateByHourMinutes(model.utcDate)
                            val homeScore = model.score!!.fullTime.home.toString().toDouble().toInt()
                            val awayScore = model.score!!.fullTime.away.toString().toDouble().toInt()
                            matchViewHolder.viewBinding.scoreHome.text = homeScore.toString()
                            matchViewHolder.viewBinding.scoreAway.text = awayScore.toString()
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(listMatch[position].round == null){
            matchView
        }else{
            roundView
        }
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
class ViewHolderMatch(viewBinding: NestedMatchLayoutBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    var viewBinding: NestedMatchLayoutBinding

    init {
        this.viewBinding = viewBinding
    }
}

class ViewHolderRoundName(viewBinding: RoundLayoutBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    var viewBinding: RoundLayoutBinding

    init {
        this.viewBinding = viewBinding
    }
}