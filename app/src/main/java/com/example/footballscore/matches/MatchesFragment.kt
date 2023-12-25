package com.example.footballscore.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.footballscore.databinding.MatchesLayoutBinding

class MatchesFragment: Fragment() {
    private lateinit var  viewBinding : MatchesLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchesLayoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}