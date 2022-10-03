package oasis.team.econg.econg.retrofit

import com.google.gson.JsonElement
import oasis.team.econg.econg.data.PostLogin
import oasis.team.econg.econg.data.PostRegister
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {
    //API1 회원가입
    @POST("/app/register")
    @Headers("content-type: application/json")
    fun signUp(@Body jsonparams: PostRegister) : Call<JsonElement>

    //API2 로그인
    @POST("/app/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun signIn(@Body jsonparams: PostLogin): Call<JsonElement>

    //API4 상품 조회
    @GET("/app/projects")
    fun showProducts(@Header("Authorization") auth: String): Call<JsonElement>
}