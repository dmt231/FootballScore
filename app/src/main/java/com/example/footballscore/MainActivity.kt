package com.example.footballscore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.footballscore.competitions.LeagueFragment
import com.example.footballscore.databinding.ActivityMainBinding
import com.example.footballscore.matches.MatchesFragment
import com.example.footballscore.teams.TeamFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        onChangedToMatches()
        onSetUpBottomBar()
    }
    @SuppressLint("CommitTransaction")
    private fun onChangedToMatches(){
        val matches = MatchesFragment()
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainLayout, matches)
        fragmentTransaction.commit()
    }
    private fun onChangedToLeagues(){
        val leagues = LeagueFragment()
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainLayout, leagues)
        fragmentTransaction.commit()
    }
    private fun onChangedToTeams(){
        val teams = TeamFragment()
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainLayout, teams)
        fragmentTransaction.commit()
    }
    private fun onSetUpBottomBar(){
        viewBinding.bottomBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.matchesIcon ->{
                    onChangedToMatches()
                    viewBinding.bottomBar.menu.findItem(R.id.matchesIcon).isChecked=true
                }
                R.id.leaguesIcon ->{
                    onChangedToLeagues()
                    viewBinding.bottomBar.menu.findItem(R.id.leaguesIcon).isChecked=true
                }
                R.id.teamsIcon ->{
                    onChangedToTeams()
                    viewBinding.bottomBar.menu.findItem(R.id.teamsIcon).isChecked=true
                }
            }
            false
        }
    }
}