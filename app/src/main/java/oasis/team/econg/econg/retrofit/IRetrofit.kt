package oasis.team.econg.econg.retrofit

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonElement
import oasis.team.econg.econg.data.OrderForPay
import oasis.team.econg.econg.data.PostLogin
import oasis.team.econg.econg.data.PostRegister
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    //API3 프로젝트 등록
    @POST("app/projects")
    fun openProject()

    //API4 상품 조회
    @GET("/app/projects")
    fun showProjects(@Header("Authorization") auth: String): Call<JsonElement>

    //API4 상품 조회 - 마감 임박 상품
    @GET("/app/projects")
    fun showConditionedProjects(@Header("Authorization") auth: String, @Query("type") type: String): Call<JsonElement>

    //API5 특정 프로젝트 조회
    @GET("/app/projects/{projectId}")
    fun showDetailProject(
        @Header("Authorization") auth: String,
        @Path("projectId") projectId: Long): Call<JsonElement>

    //API11 상품 주문 화면 가져오기
    @GET("/app/orders/pay")
    fun showProjectOrder(@Header("Authorization") auth: String, @Query("rewardId") rewardId: Long): Call<JsonElement>

    //API12 상품 주문
    @POST("/app/orders/pay")
    fun payOrder(@Header("Authorization") auth: String, @Body param : OrderForPay): Call<JsonElement>

}