package oasis.team.econg.econg.retrofit

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonElement
import oasis.team.econg.econg.data.PostLogin
import oasis.team.econg.econg.data.PostRegister
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {


    //API4 상품 조회
    @GET("/app/projects")
    fun showProjects(@Header("Authorization") auth: String): Call<JsonElement>

    //API5 특정 프로젝트 조회
    @GET("/app/projects/{projectId}")
    fun showDetailProject(
        @Header("Authorization") auth: String,
        @Path("projectId") projectId: Long): Call<JsonElement>

}