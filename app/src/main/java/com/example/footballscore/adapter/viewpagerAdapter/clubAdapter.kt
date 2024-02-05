package com.example.footballscore.adapter.viewpagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.footballscore.teams.MatchOfClubFragment
import com.example.footballscore.teams.SquadFragment

class clubAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle, clubId : Int) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var clubId: Int
    private val numberPages = 2


    init {
        this.clubId = clubId
    }
    override fun getItemCount(): Int {
        return numberPages
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return MatchOfClubFragment(clubId)
            }
            1 ->{
                return SquadFragment(clubId)
            }
        }
        return MatchOfClubFragment(clubId)
    }
}