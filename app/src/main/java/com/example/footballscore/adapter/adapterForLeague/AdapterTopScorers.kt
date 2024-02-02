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
import com.example.footballscore.competitions.top_score.topScoreModel.Scorer
import com.example.footballscore.databinding.LayoutEachScorersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdapterTopScorers(listScorers : ArrayList<Scorer>) : RecyclerView.Adapter<ViewHolderTopScores>() {
    private var listScorers : ArrayList<Scorer>
    init {
        this.listScorers = listScorers
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTopScores {
        val viewBinding = LayoutEachScorersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTopScores(viewBinding)
    }

    override fun getItemCount(): Int {
        return listScorers.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderTopScores, position: Int) {
        val model = listScorers[position]
        holder.viewBinding.position.text = (position + 1).toString()
        loadWithPlaceholder(holder.viewBinding.clubImage, model.team.crest)
        holder.viewBinding.playerName.text = model.player.name
        holder.viewBinding.positionPlayer.text = model.player.section
        holder.viewBinding.goals.text = model.goals.toString()

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
class ViewHolderTopScores(viewBinding : LayoutEachScorersBinding) : RecyclerView.ViewHolder(viewBinding.root){
    var viewBinding : LayoutEachScorersBinding
    init {
        this.viewBinding = viewBinding
    }
}