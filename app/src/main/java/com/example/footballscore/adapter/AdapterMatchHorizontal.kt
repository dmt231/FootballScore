package com.example.footballscore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballscore.databinding.RecylerviewHorizontalBinding
import com.example.footballscore.matches.matchModel.Matche

class AdapterMatchHorizontal(listMatch: ArrayList<Matche>) :
    RecyclerView.Adapter<ViewHolderHorizontal>() {
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

    override fun onBindViewHolder(holder: ViewHolderHorizontal, position: Int) {
        val match = listMatch[position]
        val leagueImage = match.competition.emblem
        val homeImage = match.homeTeam.crest
        val awayImage = match.awayTeam.crest
        Glide.with(holder.viewBinding.LeagueImage)
            .load(leagueImage)
            .into(holder.viewBinding.LeagueImage)
        Glide.with(holder.viewBinding.HomeImage)
            .load(homeImage)
            .into(holder.viewBinding.HomeImage)
        Glide.with(holder.viewBinding.AwayImage)
            .load(awayImage)
            .into(holder.viewBinding.AwayImage)
    }
}

class ViewHolderHorizontal(binding: RecylerviewHorizontalBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var viewBinding: RecylerviewHorizontalBinding

    init {
        this.viewBinding = binding
    }
}