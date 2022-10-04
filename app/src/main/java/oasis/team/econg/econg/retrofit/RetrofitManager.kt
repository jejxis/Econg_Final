package oasis.team.econg.econg.retrofit

import android.util.Log
import com.google.gson.JsonElement
import oasis.team.econg.econg.data.PostRegister
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.Constants.TAG
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

    //API4 상품 조회
    fun showProjects(auth: String?, completion: (RESPONSE_STATE, ArrayList<Project>?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.showProjects(auth = au).let{it}?:return

        call.enqueue(object: retrofit2.Callback<JsonElement>{

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            var parsedDataArray = ArrayList<Project>()
                            val body = it.asJsonObject.get("result").asJsonArray

                            Log.d(TAG, "showProjects: RetrofitManager - onResponse() called")

                            body.forEach{   resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val id = resultItemObject.get("id").asLong
                                val title = resultItemObject.get("title").asString
                                val openingDate = resultItemObject.get("openingDate").asString
                                val closingDate = resultItemObject.get("closingDate").asString
                                val totalAmount = resultItemObject.get("totalAmount").asInt
                                val achievedRate = resultItemObject.get("achievedRate").asInt
                                val summary = resultItemObject.get("summary").asString
                                val thumbnail = resultItemObject.get("thumbnail").asString
                                val authenticate = resultItemObject.get("authenticate").asBoolean
                                val user = resultItemObject.get("user").asString

                                val project = Project(
                                    id = id,
                                    title = title,
                                    openingDate = openingDate,
                                    closingDate = closingDate,
                                    totalAmount = totalAmount,
                                    achievedRate = achievedRate,
                                    summary = summary,
                                    thumbnail = thumbnail,
                                    authenticate = authenticate,
                                    user = user
                                )
                                parsedDataArray.add(project)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }

        })
    }
}