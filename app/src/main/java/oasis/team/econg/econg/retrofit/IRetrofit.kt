package oasis.team.econg.econg.retrofit

import com.google.gson.JsonElement
import oasis.team.econg.econg.data.PostLogin
import oasis.team.econg.econg.data.PostRegister
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {


    //API4 상품 조회
    @GET("/app/projects")
    fun showProjects(@Header("Authorization") auth: String): Call<JsonElement>


}