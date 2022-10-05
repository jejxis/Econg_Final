package oasis.team.econg.econg.retrofit

import android.util.Log
import com.google.gson.JsonElement
import oasis.team.econg.econg.data.*
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
                                val project = jsonElementToProject(resultItem)
                                parsedDataArray.add(project)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }

        })
    }

    //API4  상품 조회 - 마감 임박 상품
    fun showAlmostProjects(auth: String?, type: String?, completion: (RESPONSE_STATE, ArrayList<Project>?) -> Unit){
        var au = auth.let{ it}?: ""
        var type = type.let{it}?: ""
    }

    //API4  상품 조회 - 마감 임박 상품
    fun showAchievedProjects(auth: String?, type: String?, completion: (RESPONSE_STATE, ArrayList<Project>?) -> Unit){

    }

    //API5 특정 프로젝트 화면
    fun showDetailProject(auth: String?, projectId: Long?, completion: (RESPONSE_STATE, ProjectDetail?) -> Unit){
        var au = auth.let{ it}?: ""
        var projectId = projectId.let{it}?: -1
        Log.d(TAG, "DetailProject: RetrofitManager - in API")
        val call = iRetrofit?.showDetailProject(auth = au, projectId = projectId).let{
            it
        }?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "DetailProject: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "DetailProject: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asJsonObject
                            val projectImgList = ArrayList<ProjectImage>()
                            val rewardList = ArrayList<SimpleReward>()

                            val id = body.get("id").asLong
                            val title = body.get("title").asString
                            val openingDate = body.get("openingDate").asString
                            val closingDate = body.get("closingDate").asString
                            val goalAmount = body.get("goalAmount").asInt
                            val totalAmount = body.get("totalAmount").asInt
                            val achievedRate = body.get("achievedRate").asInt
                            val summary = body.get("summary").asString
                            val content = body.get("content").asString
                            val thumbnail = body.get("thumbnail").asString
                            val projectAuthenticate = body.get("projectAuthenticate").asBoolean
                            val favorite = body.get("favorite").asBoolean
                            val userId = body.get("userId").asLong
                            val userName = body.get("userName").asString
                            val userAuthenticate = body.get("userAuthenticate").asBoolean

                            val imageList = body.get("projectImgList").asJsonArray

                            imageList.forEach{ item ->
                                val obj = item.asJsonObject
                                val projectImgId = obj.get("projectImgId").asLong
                                val projectImgUrl = obj.get("projectImgUrl").asString

                                val img = ProjectImage(projectImgId = projectImgId, projectImgUrl = projectImgUrl)//!!projectImgUrl
                                projectImgList.add(img)

                                projectImgList.add(img)
                            }

                            val rewards = body.get("rewardList").asJsonArray

                            rewards.forEach{ item ->
                                val obj = item.asJsonObject
                                val rewardId = obj.get("id").asLong
                                val name = obj.get("name").asString
                                val price = obj.get("price").asInt
                                val stock = obj.get("stock").asInt
                                val soldQuantity = obj.get("soldQuantity").asInt
                                val combination = obj.get("combination").asString

                                val reward = SimpleReward(
                                    rewardId = rewardId,
                                    name = name,
                                    price = price,
                                    stock = stock,
                                    soldQuantity = soldQuantity,
                                    combination = combination
                                )

                                rewardList.add(reward)
                            }

                            val detailProject = ProjectDetail(
                                id = id, title = title, openingDate = openingDate, closingDate = closingDate,
                                goalAmount = goalAmount, totalAmount = totalAmount, achievedRate = achievedRate, summary = summary,
                                content = content, thumbnail = thumbnail, projectAuthenticate = projectAuthenticate,
                                favorite = favorite, userId = userId, userName = userName, userAuthenticate = userAuthenticate,
                                projectImgList = projectImgList, rewardList = rewardList
                            )

                            completion(RESPONSE_STATE.OKAY, detailProject)
                            Log.d(TAG, "onResponse: $detailProject")
                        }
                    }
                }
            }

        })
    }

    fun jsonElementToProject(element: JsonElement): Project{
        val resultItemObject = element.asJsonObject
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
        return project
    }
}