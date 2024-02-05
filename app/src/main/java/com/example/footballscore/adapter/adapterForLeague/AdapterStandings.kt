package com.example.footballscore.adapter.adapterForLeague

import android.graphics.Color
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
import com.example.footballscore.competitions.standings.Table
import com.example.footballscore.databinding.LayoutEachPlaceOfStandingBinding
import com.example.footballscore.databinding.LayoutGroupStageStandingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdapterStandings(standings: ArrayList<Table>, onClickListener: OnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var standings: ArrayList<Table>
    private val headerView = 0
    private val itemView = 1
    private val nameGroup = 2
    private val onClickListener : OnClickListener
    init {
        this.standings = standings
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            nameGroup -> {
                val binding = LayoutGroupStageStandingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderGroupName(binding)
            }
            else -> {
                val binding = LayoutEachPlaceOfStandingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderStanding(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return standings.size
    }

    override fun getItemViewType(position: Int): Int {
        //return if (standings[position].position == 0) headerView else itemView
        if (standings[position].group != null) {
            return nameGroup
        } else if (standings[position].position == 0) {
            return headerView
        } else {
            return itemView
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            nameGroup -> {
                val nameGroupHolder = holder as ViewHolderGroupName
                nameGroupHolder.viewBinding.nameGroup.text = standings[position].group
            }
            headerView -> {
                val standingHolder = holder as ViewHolderStanding
                standingHolder.viewBinding.imageTeam.visibility = View.GONE
            }
            itemView -> {
                val standingHolder = holder as ViewHolderStanding
                val model = standings[position]
                //Change textColor to Black
               standingHolder.viewBinding.place.setTextColor(Color.BLACK)
               standingHolder.viewBinding.teamName.setTextColor(Color.BLACK)
               standingHolder.viewBinding.play.setTextColor(Color.BLACK)
               standingHolder.viewBinding.win.setTextColor(Color.BLACK)
               standingHolder.viewBinding.draw.setTextColor(Color.BLACK)
               standingHolder.viewBinding.lose.setTextColor(Color.BLACK)
               standingHolder.viewBinding.diff.setTextColor(Color.BLACK)
               standingHolder.viewBinding.points.setTextColor(Color.BLACK)

                //Binding club standing
                standingHolder.viewBinding.place.text = model.position.toString()
                standingHolder.viewBinding.imageTeam.visibility = View.INVISIBLE
                loadWithPlaceholder(holder.viewBinding.imageTeam, model.team!!.crest)
                standingHolder.viewBinding.teamName.text = model.team!!.shortName
                standingHolder.viewBinding.play.text = model.playedGames.toString()
                standingHolder.viewBinding.win.text = model.won.toString()
                standingHolder.viewBinding.lose.text = model.lost.toString()
                standingHolder.viewBinding.draw.text = model.draw.toString()
                standingHolder.viewBinding.diff.text = model.goalDifference.toString()
                standingHolder.viewBinding.points.text = model.points.toString()

                standingHolder.viewBinding.constraintLayout.setOnClickListener {
                    onClickListener.onClick(model.team!!.id)
                }
            }
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
    interface OnClickListener {
        fun onClick(teamId : Int)
    }
}

class ViewHolderStanding(viewBinding: LayoutEachPlaceOfStandingBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    var viewBinding: LayoutEachPlaceOfStandingBinding

    init {
        this.viewBinding = viewBinding
    }
}

class ViewHolderGroupName(viewBinding: LayoutGroupStageStandingBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    var viewBinding: LayoutGroupStageStandingBinding

    init {
        this.viewBinding = viewBinding
    }
}