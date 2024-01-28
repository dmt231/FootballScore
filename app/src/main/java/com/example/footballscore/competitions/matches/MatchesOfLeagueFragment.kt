package com.example.footballscore.competitions.matches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.footballscore.databinding.MatchesOfLeagueLayoutBinding

class MatchesOfLeagueFragment : Fragment() {
    private lateinit var viewBinding : MatchesOfLeagueLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchesOfLeagueLayoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

}