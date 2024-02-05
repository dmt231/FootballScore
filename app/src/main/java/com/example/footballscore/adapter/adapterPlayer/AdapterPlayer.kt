package com.example.footballscore.adapter.adapterPlayer

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
import com.example.footballscore.databinding.LayoutPlayerBinding
import com.example.footballscore.teams.playerModel.PlayerModel
import com.example.footballscore.teams.teamsModel.Squad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdapterPlayer(listPlayer : ArrayList<Squad>) : RecyclerView.Adapter<ViewHolderPlayer>(){
    private var listPlayer : ArrayList<Squad>
    init {
        this.listPlayer = listPlayer
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPlayer {
        val viewBinding = LayoutPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderPlayer(viewBinding)
    }

    override fun getItemCount(): Int {
        return listPlayer.size
    }

    override fun onBindViewHolder(holder: ViewHolderPlayer, position: Int) {
        val model = listPlayer[position]
        holder.viewBinding.apply {
            namePlayer.text = model.name
            age.text = model.dateOfBirth
            nationPlayerName.text = model.nationality
        }
    }
}
class ViewHolderPlayer(viewBinding : LayoutPlayerBinding) : RecyclerView.ViewHolder(viewBinding.root){
    var viewBinding : LayoutPlayerBinding
    init {
        this.viewBinding = viewBinding
    }
}