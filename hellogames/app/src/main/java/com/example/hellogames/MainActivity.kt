package com.example.hellogames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import kotlin.random.Random
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseURL = "https://androidlessonsapi.herokuapp.com/api/game/"

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(jsonConverter).build()

        val service: WebService = retrofit.create(WebService::class.java)

        val explicitIntent = Intent(this, DetailActivity::class.java)

        val wsCallback: Callback<List<Game>> = object : Callback<List<Game>> {
            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                Log.d("TAG", "WebService call" + response.code());
                if (response.code() == 200) {
                    // We got our data !
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d("TAG", "WebService success : " + responseData.size)
                        val randint1 = Random.nextInt(responseData.size)
                        val randint2 = Random.nextInt(responseData.size)
                        val randint3 = Random.nextInt(responseData.size)
                        val randint4 = Random.nextInt(responseData.size)

                        Glide.with(applicationContext).load(responseData[randint1].picture).into(image1)
                        Glide.with(applicationContext).load(responseData[randint2].picture).into(image2)
                        Glide.with(applicationContext).load(responseData[randint3].picture).into(image3)
                        Glide.with(applicationContext).load(responseData[randint4].picture).into(image4)

                        val image1View : ImageView = findViewById(R.id.image1)
                        val image2View : ImageView = findViewById(R.id.image2)
                        val image3View : ImageView = findViewById(R.id.image3)
                        val image4View : ImageView = findViewById(R.id.image4)

                        var id = responseData[randint1].id;

                        image1View.setOnClickListener {
                            id = responseData[randint1].id
                            explicitIntent.putExtra("ID", id)

                            startActivity(explicitIntent);
                        }

                        image2View.setOnClickListener {
                            id = responseData[randint2].id
                            explicitIntent.putExtra("ID", id)

                            startActivity(explicitIntent);
                        }

                        image3View.setOnClickListener {
                            id = responseData[randint3].id
                            explicitIntent.putExtra("ID", id)

                            startActivity(explicitIntent);
                        }

                        image4View.setOnClickListener {
                            id = responseData[randint4].id
                            explicitIntent.putExtra("ID", id)

                            startActivity(explicitIntent);
                        }
                    }
                }
            }
        }

        service.listGames().enqueue(wsCallback)
    }
}
