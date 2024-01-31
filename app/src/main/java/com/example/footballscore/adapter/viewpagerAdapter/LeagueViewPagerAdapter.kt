package com.example.footballscore.adapter.viewpagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.footballscore.competitions.matches.MatchesOfLeagueFragment
import com.example.footballscore.competitions.standings.StandingsFragment
import com.example.footballscore.competitions.top_score.TopScoreFragment

class LeagueViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, competitionId : Int, competitionImage : String,
                                competitionName : String) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var competitionId: Int
    private val numberPages = 3
    private var competitionImage : String
    private var competitionName : String

    init {
        this.competitionId = competitionId
        this.competitionImage = competitionImage
        this.competitionName  = competitionName
    }
    override fun getItemCount(): Int {
        return numberPages
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return StandingsFragment(competitionId, competitionImage, competitionName)
            }
            1 ->{
                return MatchesOfLeagueFragment()
            }
            2->{
                return TopScoreFragment()
            }
        }
        return StandingsFragment(competitionId, competitionImage, competitionName)
    }
}