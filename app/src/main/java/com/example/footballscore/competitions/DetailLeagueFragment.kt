package com.example.footballscore.competitions

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.example.footballscore.R
import com.example.footballscore.adapter.viewpagerAdapter.LeagueViewPagerAdapter
import com.example.footballscore.databinding.DetailLeagueLayoutBinding
import com.google.android.material.tabs.TabLayout

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DetailLeagueFragment : Fragment() {
    private lateinit var viewBinding : DetailLeagueLayoutBinding
    private lateinit var viewpageAdapter : LeagueViewPagerAdapter
    private lateinit var leagueName : String
    private lateinit var leagueImage : String
    private var leagueId : Int? = null
    private val season  =  arrayOf<String>("2020-2021","2021-2022","2022-2023","2023-2024")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DetailLeagueLayoutBinding.inflate(inflater, container, false)
        viewBinding.layoutConstraint.setOnClickListener {

        }
        viewBinding.btnBack.setOnClickListener {
            onBackToLeague()
        }
        getBundle()
        loadData()
        setUpSpinner()
        setUpTabLayoutViewPager()
        return viewBinding.root
    }

    private fun onBackToLeague() {
        requireFragmentManager().popBackStack()
    }

    private fun getBundle(){
        val bundle = arguments
        if(bundle != null){
            leagueId = bundle["idLeague"] as Int
            leagueName = bundle["nameLeague"] as String
            leagueImage = bundle["imageLeague"] as String
        }
    }
    private fun loadData(){
        if(leagueId != null && leagueName != null && leagueImage != null){
            viewBinding.detailLeagueName.text = leagueName
            loadWithPlaceholder(viewBinding.detailLeagueImage, leagueImage)
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
    private fun setUpSpinner(){
        val arrayList = season.toList()
        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.layout_select_season,arrayList)
        viewBinding.spinnerSeason.adapter = arrayAdapter

        val defaultSelectionIndex = arrayList.indexOf("2023-2024")
        viewBinding.spinnerSeason.setSelection(defaultSelectionIndex)
    }
    private fun setUpTabLayoutViewPager(){
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Standings"))
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Matches"))
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Top Score"))
        viewBinding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        viewpageAdapter = LeagueViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle,leagueId!!)
        viewBinding.viewPager.adapter = viewpageAdapter

        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
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
                viewBinding.tabLayout.selectTab(viewBinding.tabLayout.getTabAt(position))
            }
        })
    }

}