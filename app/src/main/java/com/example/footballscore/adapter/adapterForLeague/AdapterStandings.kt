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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdapterStandings(standings : ArrayList<Table>) : RecyclerView.Adapter<ViewHolderStanding>() {
    private  var standings : ArrayList<Table>
    init {
        this.standings = standings
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStanding {
        var binding = LayoutEachPlaceOfStandingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderStanding(binding)
    }

    override fun getItemCount(): Int {
        return standings.size
    }

    override fun onBindViewHolder(holder: ViewHolderStanding, position: Int) {
       val model = standings[position]
        if(model.team == null){
            holder.viewBinding.imageTeam.visibility = View.GONE
        }
        else{
            //Change textColor to Black
           holder.viewBinding.place.setTextColor(Color.BLACK)
           holder.viewBinding.teamName.setTextColor(Color.BLACK)
           holder.viewBinding.play.setTextColor(Color.BLACK)
           holder.viewBinding.win.setTextColor(Color.BLACK)
           holder.viewBinding.draw.setTextColor(Color.BLACK)
           holder.viewBinding.lose.setTextColor(Color.BLACK)
           holder.viewBinding.diff.setTextColor(Color.BLACK)
           holder.viewBinding.points .setTextColor(Color.BLACK)
            //Binding club standing
            holder.viewBinding.place.text = model.position.toString()
            loadWithPlaceholder(holder.viewBinding.imageTeam, model.team!!.crest)
            holder.viewBinding.teamName.text = model.team!!.shortName
            holder.viewBinding.play.text = model.playedGames.toString()
            holder.viewBinding.win.text = model.won.toString()
            holder.viewBinding.lose.text = model.lost.toString()
            holder.viewBinding.draw.text = model.draw.toString()
            holder.viewBinding.diff.text = model.goalDifference.toString()
            holder.viewBinding.points.text = model.points.toString()
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
}
class ViewHolderStanding(viewBinding : LayoutEachPlaceOfStandingBinding) : RecyclerView.ViewHolder(viewBinding.root){
    var viewBinding : LayoutEachPlaceOfStandingBinding
    init {
        this.viewBinding = viewBinding
    }
}