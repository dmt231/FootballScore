package com.example.footballscore.competitions.top_score

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.footballscore.databinding.MatchesOfLeagueLayoutBinding
import com.example.footballscore.databinding.TopScoreLayoutBinding

class TopScoreFragment : Fragment() {
    private lateinit var viewBinding : TopScoreLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = TopScoreLayoutBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

}