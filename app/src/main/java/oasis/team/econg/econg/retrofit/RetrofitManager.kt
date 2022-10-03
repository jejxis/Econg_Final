package oasis.team.econg.econg.retrofit

import com.google.gson.JsonElement
import oasis.team.econg.econg.data.PostRegister
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Response


class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
    }

    //http call
    //get retrofit interface
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)


}