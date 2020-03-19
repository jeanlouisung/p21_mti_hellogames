package com.example.hellogames

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val baseURL = "https://androidlessonsapi.herokuapp.com/api/game/"

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(jsonConverter).build()

        val service: WebService = retrofit.create(WebService::class.java)

        val originalIntent = intent
        val id = originalIntent.getIntExtra("ID", 1)

        val wsCallback: Callback<GameDetail> = object : Callback<GameDetail> {
            override fun onFailure(call: Call<GameDetail>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<GameDetail>, response: Response<GameDetail>) {
                Log.d("TAG", "WebService call" + response.code());
                if (response.code() == 200) {
                    // We got our data !
                    val responseData = response.body()
                    if (responseData != null) {
                        Glide.with(applicationContext).load(responseData.picture).into(image_game)
                        val t : TextView = findViewById(game_name.id) as TextView
                        t.text = responseData.name
                        val t2 = findViewById(game_type.id) as TextView
                        t2.text = responseData.type
                        val t3 = findViewById(nb_player.id) as TextView
                        t3.text = responseData.players.toString()
                        val t4 = findViewById(year_game.id) as TextView
                        t4.text = responseData.year.toString()
                        val t5 = findViewById(resume_game.id) as TextView
                        t5.text = responseData.description_en

                        url_more.setOnClickListener {
                            val browser = Intent(Intent.ACTION_VIEW, Uri.parse(responseData.url))
                            startActivity(browser)
                        }
                    }
                }
            }
        }

        service.detailGame(id).enqueue(wsCallback)
    }
}
