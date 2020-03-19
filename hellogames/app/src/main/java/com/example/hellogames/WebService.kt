package com.example.hellogames

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.hellogames.Game as GameObj

interface WebService {
    @GET("list")
    fun listGames(): Call<List<GameObj>>

    @GET("details")
    fun detailGame(@Query("game_id") game_id : Int): Call<GameDetail>
}