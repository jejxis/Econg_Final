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

    //API3 상품 등록
    fun openProject(auth: String?, param: ProjectForOpen, completion: (RESPONSE_STATE, String?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.openProject(auth = au, param = param).let{it}?:return
        Log.d(TAG, "OpenProject: RetrofitManager - In API")

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "OpenProject: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "OpenProject: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asString
                            completion(RESPONSE_STATE.OKAY, body)
                        }
                    }
                }
            }
        })
    }

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
                                val project = convertJsonElementToProject(resultItem)
                                parsedDataArray.add(project)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }

        })
    }

    //API4  상품 조회 - 마감 임박, 달성률 90% 이상 상품
    fun showConditionedProjects(auth: String?, type: String?, completion: (RESPONSE_STATE, ArrayList<Project>?) -> Unit){
        var au = auth.let{ it}?: ""
        var type = type.let{it}?: ""
        val call = iRetrofit?.showConditionedProjects(auth = au, type = type).let{
            it
        }?: return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "ProjectList: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "ProjectList: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            var parsedDataArray = ArrayList<Project>()
                            val body = it.asJsonObject.get("result").asJsonArray

                            Log.d(TAG, "showProjects: RetrofitManager - onResponse() called")

                            body.forEach{   resultItem ->
                                val project = convertJsonElementToProject(resultItem)
                                parsedDataArray.add(project)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
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
                            val status = body.get("status").asString

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
                                projectImgList = projectImgList, rewardList = rewardList, status = status
                            )

                            completion(RESPONSE_STATE.OKAY, detailProject)
                            Log.d(TAG, "onResponse: $detailProject")
                        }
                    }
                }
            }

        })
    }

    fun convertJsonElementToProject(element: JsonElement): Project{
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
        val status = resultItemObject.get("status").asString

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
            user = user,
            status = status
        )
        return project
    }

    //API 6
    fun pushFavorite(auth: String?, projectId: Long?, completion: (RESPONSE_STATE, String?) -> Unit){
        var au = auth.let{ it}?: ""
        var projectId = projectId.let{it}?: -1
        Log.d(TAG, "DetailProject: RetrofitManager - in API")
        val data = PostFavorite(projectId)
        val call = iRetrofit?.postFavorite(auth = au, data).let{
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
                            val body = it.asJsonObject.get("result").asString

                            completion(RESPONSE_STATE.OKAY, body)
                            Log.d(TAG, "onResponse: $body")
                        }
                    }
                }
            }

        })
    }

    //API 7
    fun getFavorites(auth: String?,  completion: (RESPONSE_STATE,ArrayList<Project>?) -> Unit){
        var au = auth.let{ it}?: ""

        Log.d(TAG, "FavoriteProjects: RetrofitManager - in API")
        val call = iRetrofit?.getFavorites(auth = au).let{
            it
        }?: return

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

                            Log.d(TAG, "FavoriteProjects: RetrofitManager - onResponse() called")

                            body.forEach{   resultItem ->
                                val project = convertJsonElementToProject(resultItem)
                                parsedDataArray.add(project)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }

        })
    }

    //API8 프로젝트 커뮤니티 등록
    fun postProjectCommunity(auth: String?, projectId: Long?, content: String?, completion: (RESPONSE_STATE, String?) -> Unit){
        var au = auth.let{it}?:""
        var projectId = projectId.let{it}?: -1
        var content = content.let{it}?:""

        val call = iRetrofit?.postProjectCommunity(auth = au, projectId = projectId, content = content).let{it}?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "DetailProject: postCommunity - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "DetailProject: postCommunity - onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asString
                            completion(RESPONSE_STATE.OKAY, body)
                        }
                    }
                }
            }
        })
    }

    //API9 프로젝트 커뮤니티 조회
    fun showProjectCommunities(auth: String?, projectId: Long?, completion: (RESPONSE_STATE, ArrayList<ProjectCommunity>?) -> Unit) {
        var au = auth.let{it}?:""
        var projectId = projectId.let{it}?: -1
        val call = iRetrofit?.showProjectCommunities(auth = au, projectId = projectId).let{it}?:return

        call.enqueue(object :retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "DetailProject: communityList - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "DetailProject: communityList - onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            var parsedDataArray = ArrayList<ProjectCommunity>()
                            val body = it.asJsonObject.get("result").asJsonArray
                            Log.d(TAG, "showProjectCommunities: RetrofitManager - onResponse() called")
                            if(body.size() > 0){
                                body.forEach{   resultItem ->
                                    val resultItemObject = resultItem.asJsonObject
                                    val community = ProjectCommunity(
                                        id = resultItemObject.get("id").asInt,
                                        content = resultItemObject.get("content").asString,
                                        updatedAt = resultItemObject.get("updatedAt").asString,
                                        userId = resultItemObject.get("userId").asLong,
                                        userName = resultItemObject.get("userName").asString,
                                        userProfileUrl = resultItemObject.get("useProfileUrl").asString
                                    )
                                    parsedDataArray.add(community)
                                }
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    //API11 상품 주문 화면 가져오기
    fun showProjectOrder(auth: String?, rewardId: Long?, completion: (RESPONSE_STATE, OrderBeforePay?) -> Unit){
        var au = auth.let{it}?:""
        var rewardId = rewardId.let{it}?:-1
        Log.d(TAG, "Payment: RetrofitManager - in API")
        val call = iRetrofit?.showProjectOrder(auth = au, rewardId = rewardId).let{
            it
        }?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "Payment: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "Payment: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asJsonObject

                            val projectId = body.get("projectId").asLong
                            val title = body.get("title").asString
                            val thumbnail = body.get("thumbnail").asString
                            val rewardId = body.get("rewardId").asLong
                            val rewardName = body.get("rewardName").asString
                            val price = body.get("price").asInt
                            val combination = body.get("combination").asString

                            val orderBeforePay = OrderBeforePay(
                                projectId = projectId,
                                title = title,
                                thumbnail = thumbnail,
                                rewardId = rewardId,
                                rewardName = rewardName,
                                price = price,
                                combination = combination
                            )

                            completion(RESPONSE_STATE.OKAY, orderBeforePay)
                        }
                    }
                }
            }
        })
    }


    
    //API12 상품 주문
    fun payOrder(auth: String?, param: OrderForPay, completion: (RESPONSE_STATE, String?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.payOrder(auth = au, param = param).let{
            it
        }?:return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "Payment - payOrder: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "Payment - payOrder: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asString
                            completion(RESPONSE_STATE.OKAY, body)
                        }
                    }
                }
            }
        })
    }

    fun getRecentUsers(auth: String?, completion:(RESPONSE_STATE, ArrayList<User>?) -> Unit){
        var au = auth.let{it}?:""

        val call = iRetrofit?.getRecentUsers(auth = au).let{
            it
        }?: return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "UserList: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "UserList: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            var parsedDataArray = ArrayList<User>()
                            val body = it.asJsonObject.get("result").asJsonArray

                            Log.d(TAG, "UserList: RetrofitManager - onResponse() called")

                            body.forEach{   resultItem ->
                                val obj = resultItem.asJsonObject
                                var desc = if(obj.get("description").isJsonNull)" " else obj.get("description").asString
                                val user = User(obj.get("userId").asLong,
                                                obj.get("nickName").asString,
                                                desc,
                                                obj.get("profileUrl").asString,
                                                obj.get("authenticate").asBoolean
                                                )
                                parsedDataArray.add(user)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })


    }


    //API13 자신의 사용정보 조회
    fun showMyInfo(auth: String?, completion:(RESPONSE_STATE, UserProfile?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.showMyInfo(auth = au).let{it}?:return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager: showMyInfo() - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager: showMyInfo() - onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asJsonObject

                            val userId = body.get("userId").asLong
                            val nickName = body.get("nickName").asString

                            var description = ""
                            if(body.get("description").isJsonNull) description = ""
                            else description = body.get("description").asString

                            val profileUrl = body.get("profileUrl").asString
                            val authenticate = body.get("authenticate").asBoolean
                            val followingNum = body.get("followingNum").asInt
                            val followerNum = body.get("followerNum").asInt
                            val myProfile = body.get("myProfile").asBoolean

                            val userProfile = UserProfile(
                                userId = userId,
                                nickName = nickName,
                                description = description,
                                profileUrl = profileUrl,
                                authenticate = authenticate,
                                followingNum = followingNum,
                                followerNum = followerNum,
                                myProfile = myProfile,
                                body.get("isFollow").asBoolean
                            )

                            completion(RESPONSE_STATE.OKAY, userProfile)
                            Log.d(TAG, "RetrofitManager - showMyInfo: onResponse: $userProfile")
                        }
                }
            }
        })
    }

//    API 14
    fun getUserProfile(auth: String?, userId: Long?, completion: (RESPONSE_STATE, UserProfile?) -> Unit){
        var au = auth.let{ it}?: ""
        var userId = userId.let{it}?: -1
        Log.d(TAG, "UserProfile: RetrofitManager - in API")
        val call = iRetrofit?.getUserProfile(auth = au, userId = userId).let{
            it
        }?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "UserProfile: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "UserProfile: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asJsonObject
                            var desc = if(body.get("description").isJsonNull) " " else body.get("description").asString
                            val userProfile = UserProfile(
                                body.get("userId").asLong,
                                body.get("nickName").asString,
                                desc,
                                body.get("profileUrl").asString,
                                body.get("authenticate").asBoolean,
                                body.get("followingNum").asInt,
                                body.get("followerNum").asInt,
                                body.get("myProfile").asBoolean,
                                body.get("isFollow").asBoolean
                            )

                            completion(RESPONSE_STATE.OKAY, userProfile)
                            Log.d(TAG, "onResponse: $userProfile")
                        }
                    }
                }
            }

        })
    }

//API 15
    fun pushFollow(auth: String?, userId: Long?, completion: (RESPONSE_STATE, String?) -> Unit){
        var au = auth.let{ it}?: ""
        var userId = userId.let{it}?: -1
        Log.d(TAG, "pushFollow: RetrofitManager - in API")
        val data = Follow(userId)
        val call = iRetrofit?.postFollow(auth = au, data).let{
            it
        }?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "pushFollow: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "pushFollow: RetrofitManager - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asString

                            completion(RESPONSE_STATE.OKAY, body)
                            Log.d(TAG, "onResponse: $body")
                        }
                    }
                }
            }
        })
    }



    //API16 내 팔로잉
    fun getMyFollowings(auth: String?, completion: (RESPONSE_STATE, ArrayList<UserForFollow>?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.getMyFollowings(auth = au).let{it}?:return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager: getMyFollowings - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager: getMyFollowings - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val parsedDataArray = ArrayList<UserForFollow>()
                            val body = it.asJsonObject.get("result").asJsonArray
                            body.forEach { resultItem ->
                                val uff = convertJsonElementToUserForFollow(resultItem)
                                parsedDataArray.add(uff)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    //API17 내 팔로워
    fun getMyFollowers(auth: String?, completion: (RESPONSE_STATE, ArrayList<UserForFollow>?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.getMyFollowers(auth = au).let{it}?:return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager: getMyFollowers - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager: getMyFollowers - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val parsedDataArray = ArrayList<UserForFollow>()
                            val body = it.asJsonObject.get("result").asJsonArray
                            body.forEach { resultItem ->
                                val uff = convertJsonElementToUserForFollow(resultItem)
                                parsedDataArray.add(uff)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    //API18 특정 팔로잉
    fun getUserFollowings(auth: String?, userId: Long?, completion: (RESPONSE_STATE, ArrayList<UserForFollow>?) -> Unit){
        var au = auth.let{it}?:""
        var ui = userId.let{it}?:-1
        val call = iRetrofit?.getUserFollowings(auth = au, userId = ui).let{it}?:return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager: getMyFollowings - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager: getMyFollowings - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val parsedDataArray = ArrayList<UserForFollow>()
                            val body = it.asJsonObject.get("result").asJsonArray
                            body.forEach { resultItem ->
                                val uff = convertJsonElementToUserForFollow(resultItem)
                                parsedDataArray.add(uff)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    //API19 특정 유저 팔로워
    fun getUserFollowers(auth: String?, userId: Long?,completion: (RESPONSE_STATE, ArrayList<UserForFollow>?) -> Unit){
        var au = auth.let{it}?:""
        var ui = userId.let{it}?:-1
        val call = iRetrofit?.getUserFollowers(auth = au, userId = ui).let{it}?:return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager: getUserFollowers - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager: getUserFollowers - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val parsedDataArray = ArrayList<UserForFollow>()
                            val body = it.asJsonObject.get("result").asJsonArray
                            body.forEach { resultItem ->
                                val uff = convertJsonElementToUserForFollow(resultItem)
                                parsedDataArray.add(uff)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    fun convertJsonElementToUserForFollow(element: JsonElement): UserForFollow{
        val  resultItemObject = element.asJsonObject
        val userId = resultItemObject.get("userId").asLong
        val userName = resultItemObject.get("userName").asString
        val profileUrl = resultItemObject.get("profileUrl").asString
        val follow = resultItemObject.get("follow").asBoolean
        val myProfile = resultItemObject.get("myProfile").asBoolean

        val uff = UserForFollow(
            userId,
            userName,
            profileUrl,
            follow,
            myProfile
        )

        return uff
    }

    //API 20
    fun getMyCommunites(auth:String?, completion: (RESPONSE_STATE, ArrayList<MyCommunity>?) -> Unit){
        var au = auth.let{it}?:""

        val call = iRetrofit?.getMyCommunities(auth = au).let{it}?:return

        call.enqueue(object :retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "MycommunityList - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "MycommunityList - onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            var parsedDataArray = ArrayList<MyCommunity>()
                            val body = it.asJsonObject.get("result").asJsonArray
                            Log.d(TAG, "MycommunityList: RetrofitManager - onResponse() called")
                            if(body.size() > 0){
                                body.forEach{   resultItem ->
                                    val resultItemObject = resultItem.asJsonObject
                                    val community = MyCommunity(
                                        id = resultItemObject.get("id").asLong,
                                        content = resultItemObject.get("content").asString,
                                        updatedAt = resultItemObject.get("updatedAt").asString,
                                        userId = resultItemObject.get("userId").asLong,
                                        userName = resultItemObject.get("userName").asString,
                                        userProfileUrl = resultItemObject.get("useProfileUrl").asString,
                                        projectId = resultItemObject.get("projectId").asLong,
                                        projectName = resultItemObject.get("projectName").asString
                                    )
                                    parsedDataArray.add(community)
                                }
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    //API 22
    fun getDetailOrderInfo(auth:String?, orderId: Long?,completion: (RESPONSE_STATE, Order?) -> Unit){
        var au = auth.let{it}?:""
        var orderId = orderId.let{it}?: -1

        val call = iRetrofit?.getDetailOrderInfo(auth = au, orderId= orderId).let{it}?:return

        call.enqueue(object :retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "MyOrderList - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "MyOrderList - onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asJsonObject
                            Log.d(TAG, "DetailOrder: RetrofitManager - onResponse() called")
                            val resultItemObject = body.asJsonObject
                            var orderStatus :Int =
                                if(resultItemObject.get("orderStatus").asString == "PAYCOMPLETED") 1
                                else 0
                            val order = Order(
                                price = resultItemObject.get("totalMoney").asInt,
                                combination = resultItemObject.get("combination").asString,
                                projectId = resultItemObject.get("projectId").asLong,
                                deliveryAddress = resultItemObject.get("deliveryAddress").asString,
                                rewardName = resultItemObject.get("rewardName").asString,
                                title = resultItemObject.get("projectName").asString,
                                orderStatus = orderStatus,
                            )
                            completion(RESPONSE_STATE.OKAY, order)
                        }
                    }
                }
            }
        })

    }

    //API 23
    fun getOrderedProjects(auth:String?, completion: (RESPONSE_STATE, ArrayList<OrderConfirmation>?) -> Unit){
        var au = auth.let{it}?:""

        val call = iRetrofit?.getOrderedProjects(auth = au).let{it}?:return

        call.enqueue(object :retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "MyOrderList - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "MyOrderList - onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            var parsedDataArray = ArrayList<OrderConfirmation>()
                            val body = it.asJsonObject.get("result").asJsonArray
                            Log.d(TAG, "MyOrderList: RetrofitManager - onResponse() called")
                            if(body.size() > 0){
                                body.forEach{   resultItem ->
                                    val resultItemObject = resultItem.asJsonObject
                                    var orderStatus :Int =
                                        if(resultItemObject.get("status").asString == "ONGOING") 1
                                        else if(resultItemObject.get("status").asString == "SUCCESS") 0
                                        else 2
                                    val project = OrderConfirmation(
                                        combination = resultItemObject.get("combination").asString,
                                        price = resultItemObject.get("price").asInt,
                                        projectId = resultItemObject.get("projectId").asLong,
                                        rewardId = resultItemObject.get("rewardId").asLong,
                                        rewardName = resultItemObject.get("rewardName").asString,
                                        title = resultItemObject.get("title").asString,
                                        thumbnail = resultItemObject.get("thumbnail").asString,
                                        orderStatus = orderStatus,
                                        orderId = resultItemObject.get("orderId").asLong,
                                        status = resultItemObject.get("status").asBoolean,

                                    )
                                    parsedDataArray.add(project)
                                }
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }



    //API 24
    fun getUserOpenedProjects(auth: String?, userId: Long?, completion: (RESPONSE_STATE, ArrayList<Project>?) -> Unit){
        var au = auth.let{it}?:""
        var userId = userId.let{it}?: -1

        val call = iRetrofit?.getUserOpenedProjects(auth = au, userId= userId).let{it}?:return

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

                            Log.d(TAG, "getUserOpenedProject: RetrofitManager - onResponse() called")

                            body.forEach{   resultItem ->
                                val project = convertJsonElementToProject(resultItem)
                                parsedDataArray.add(project)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }

        })
    }

    //API21
    fun editMyProfile(auth: String,userId: Long,profile: UserEditProfile, completion: (RESPONSE_STATE, String?) -> Unit) {
        var au = auth.let{it}?:""
        var userId = userId.let{it}?:-1
        Log.d(TAG, "editMyProfile: RetrofitManager - in API")
        val call = iRetrofit?.editMyProfile(auth = au, userId = userId,
        profile = profile).let{
            it
        }?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "editMyProfile: RetrofitManager - onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "editMyProfile - onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject.get("result").asString

                            completion(RESPONSE_STATE.OKAY, body)
                            Log.d(TAG, "onResponse: $body")
                        }
                    }
                }
            }
        })
    }
}

