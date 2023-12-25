package com.example.footballscore.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.footballscore.databinding.MatchesLayoutBinding
import com.example.footballscore.databinding.TeamsLayoutBinding

class TeamFragment : Fragment(){
    private lateinit var  viewBinding : TeamsLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = TeamsLayoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}