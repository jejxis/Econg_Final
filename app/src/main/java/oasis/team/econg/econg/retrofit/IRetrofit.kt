package oasis.team.econg.econg.retrofit

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonElement
import oasis.team.econg.econg.data.*
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    //API3 프로젝트 등록
    @POST("/app/projects") //@Headers("content-type: application/json")
    fun openProject(@Header("Authorization") auth: String, @Body param: ProjectForOpen): Call<JsonElement>

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

    //API6
    @POST("/app/favorites")
    fun postFavorite(@Header("Authorization") auth: String, @Body jsonparams: PostFavorite): Call<JsonElement>

    //API7
    @GET("/app/favorites")
    fun getFavorites(@Header("Authorization") auth: String): Call<JsonElement>


    //API8 프로젝트 커뮤니티 등록
    @POST("/app/projects/{projectId}/communities")
    fun postProjectCommunity(@Header("Authorization") auth: String,
                             @Path("projectId") projectId: Long, @Body content: String):Call<JsonElement>

    //API9 프로젝트 커뮤니티 조회
    @GET("/app/projects/{projectId}/communities")
    fun showProjectCommunities(@Header("Authorization") auth: String, @Path("projectId") projectId: Long):Call<JsonElement>

    //API10
    @GET("/app/recent-users")
    fun getRecentUsers(@Header("Authorization") auth: String): Call<JsonElement>

    //API11 상품 주문 화면 가져오기
    @GET("/app/orders/pay")
    fun showProjectOrder(@Header("Authorization") auth: String, @Query("rewardId") rewardId: Long): Call<JsonElement>

    //API12 상품 주문
    @POST("/app/orders/pay")
    fun payOrder(@Header("Authorization") auth: String, @Body param : OrderForPay): Call<JsonElement>

    //API13 자신의 사용정보 조회
    @GET("/app/profiles")
    fun showMyInfo(@Header("Authorization") auth: String): Call<JsonElement>

    //API14 유저 정보
    @GET("/app/profiles/{userId}")
    fun getUserProfile(@Header("Authorization") auth: String, @Path("userId") userId:Long): Call<JsonElement>

    //API15
    @POST("/app/follows")
    fun postFollow(@Header("Authorization") auth: String, @Body jsonparams: Follow): Call<JsonElement>


    //API16 내 팔로잉
    @GET("/app/followings")
    fun getMyFollowings(@Header("Authorization") auth: String): Call<JsonElement>

    //API17 내 팔로워
    @GET("/app/followers")
    fun getMyFollowers(@Header("Authorization") auth: String): Call<JsonElement>

    //API18 특정 유저 팔로잉
    @GET("/app/users/{userId}/followings")
    fun getUserFollowings(@Header("Authorization") auth: String, @Path("userId")userId: Long): Call<JsonElement>

    //API19 특정 유저 팔로워
    @GET("/app/users/{userId}/followers")
    fun getUserFollowers(@Header("Authorization") auth: String, @Path("userId")userId: Long): Call<JsonElement>

    //API20
    @GET("/app/communities")
    fun getMyCommunities(@Header("Authorization") auth: String): Call<JsonElement>

    //API 22
    @GET("/app/orders/detail/{orderId}")
    fun getDetailOrderInfo(@Header("Authorization") auth: String,  @Path("orderId")orderId: Long): Call<JsonElement>

    //API 23
    @GET("/app/orders")
    fun getOrderedProjects(@Header("Authorization") auth: String): Call<JsonElement>

    //API 24
    @GET("/app/users/post-projects/{userId}")
    fun getUserOpenedProjects(@Header("Authorization") auth: String,  @Path("userId") userId:Long): Call<JsonElement>

}