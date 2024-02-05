package com.example.footballscore.teams

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R

import com.example.footballscore.adapter.viewpagerAdapter.clubAdapter

import com.example.footballscore.databinding.TeamFragmentLayoutBinding
import com.example.footballscore.model.ApiClient
import com.example.footballscore.model.ApiInterface
import com.example.footballscore.teams.teamsModel.Team
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class TeamFragment : Fragment(){
    private lateinit var  viewBinding : TeamFragmentLayoutBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var viewpageAdapter : clubAdapter
    private  var idTeam : Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = TeamFragmentLayoutBinding.inflate(inflater, container, false)
        apiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        getData()
        setUpTabLayoutViewPager()
        loadClubData()
        viewBinding.btnBack.setOnClickListener {
            onBack()
        }
        return viewBinding.root
    }

    private fun onBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun loadClubData() {
        val call = apiInterface.getTeamsById(idTeam!!)
        call.enqueue(object : Callback<Team>{
            override fun onResponse(call: Call<Team>, response: Response<Team>) {
                val team = response.body()
                viewBinding.apply {
                    clubName.text = team!!.name
                    nation.text = team!!.area.name
                }
                loadWithPlaceholder(viewBinding.detailClubImage, team!!.crest)
                loadWithPlaceholder(viewBinding.nationImage, team!!.area.flag)
            }
            override fun onFailure(call: Call<Team>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getData() {
        val bundle = arguments
        if(bundle != null){
            idTeam = bundle["teamId"] as Int
        }
    }
    private fun loadWithPlaceholder(imageView: ImageView, url: String) {
        if (url.endsWith(".svg")) {
            loadSvgImage(imageView, url)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
    private fun loadSvgImage(imageView: ImageView, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream: InputStream = openConnection(url)
                val svg: SVG = SVG.getFromInputStream(inputStream)

                withContext(Dispatchers.Main) {
                    val drawable = PictureDrawable(svg.renderToPicture())
                    imageView.setImageDrawable(drawable)
                    imageView.visibility = View.VISIBLE
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.football_club)
                }
            } catch (e: SVGParseException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.football_club)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun openConnection(url: String): InputStream {
        val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        return connection.inputStream
    }
    private fun setUpTabLayoutViewPager(){
        viewBinding.tabLayoutClub.addTab(viewBinding.tabLayoutClub.newTab().setText("Matches"))
        viewBinding.tabLayoutClub.addTab(viewBinding.tabLayoutClub.newTab().setText("Squad"))
        viewBinding.tabLayoutClub.tabGravity = TabLayout.GRAVITY_FILL
        viewpageAdapter = clubAdapter(requireActivity().supportFragmentManager, lifecycle,idTeam!!)
        viewBinding.viewPager.adapter = viewpageAdapter

        viewBinding.tabLayoutClub.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewBinding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        viewBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewBinding.tabLayoutClub.selectTab(viewBinding.tabLayoutClub.getTabAt(position))
            }
        })
    }
}