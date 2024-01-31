package com.example.footballscore.model
import com.example.footballscore.competitions.competion_match.Competition_Match
import com.example.footballscore.competitions.list_competition.ListCompetitions
import com.example.footballscore.competitions.standings.StandingsModels
import com.example.footballscore.matches.matchModel.Match
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    // Get Competition_Match
    @GET("competitions/{id}/matches")
    @Headers("X-Auth-Token:fba80be2ea3b4d5d8b9e9c3d4f10a2ed")
    fun getMatchFromCompetition(@Path("id") competitionId : Int, @Query("matchday") matchDay : Int) : Call<Competition_Match>
    @GET("competitions/{id}/matches")
    @Headers("X-Auth-Token:fba80be2ea3b4d5d8b9e9c3d4f10a2ed")
    fun getMatchFromCompetitionByDate(@Path("id") competitionId : Int, @Query("dateFrom") dateFrom : String, @Query("dateTo")  dateTo : String ) : Call<Competition_Match>
    @GET("competitions/{id}/matches")
    @Headers("X-Auth-Token:fba80be2ea3b4d5d8b9e9c3d4f10a2ed")
    fun getMatchFromCompetitionInPlay(@Path("id") competitionId : Int, @Query("status") status : String) : Call<Competition_Match>

    @GET("matches")
    @Headers("X-Auth-Token:fba80be2ea3b4d5d8b9e9c3d4f10a2ed")
    fun getMatchRecentFor2day(@Query("dateFrom") dateFrom : String, @Query("dateTo")  dateTo : String ) : Call<Match>

    @GET("competitions")
    @Headers("X-Auth-Token:fba80be2ea3b4d5d8b9e9c3d4f10a2ed")
    fun getListCompetitions() : Call<ListCompetitions>

    @GET("competitions/{id}/standings")
    @Headers("X-Auth-Token:fba80be2ea3b4d5d8b9e9c3d4f10a2ed")
    fun getStandingsRecentForLeague(@Path("id") competitionId : Int) : Call<StandingsModels>

    @GET("competitions/{id}/standings")
    @Headers("X-Auth-Token:fba80be2ea3b4d5d8b9e9c3d4f10a2ed")
    fun getStandingsBySeasonForLeague(@Path("id") competitionId : Int, @Query("season") season : Int) : Call<StandingsModels>
}
