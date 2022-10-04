package oasis.team.econg.econg.interfaceModel

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import oasis.team.econg.econg.data.Login
import oasis.team.econg.econg.data.PostLogin
import oasis.team.econg.econg.data.PostRegister
import oasis.team.econg.econg.samplePreference.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIS {

    @POST("/app/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postLogin(
        @Body jsonparams: PostLogin
    ): Call<Login>

    @POST("/app/register")
    @Headers("content-type: application/json")
    fun postRegister(
        @Body jsonparams: PostRegister
    ): Call<String>


    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "https://isileeserver.shop" // 주소

        fun create(): APIS {
            val okHttpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
            val gson : Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}