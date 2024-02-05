package com.example.footballscore.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.footballscore.databinding.SquadLayoutBinding

class SquadFragment(clubId : Int) : Fragment() {
    private lateinit var viewBinding : SquadLayoutBinding
    private var clubId : Int
    init {
        this.clubId = clubId
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = SquadLayoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}