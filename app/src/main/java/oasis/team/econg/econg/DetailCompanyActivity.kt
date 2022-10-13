package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.data.UserProfile
import oasis.team.econg.econg.databinding.ActivityDetailCompanyBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.CompanyVerAdapter
import oasis.team.econg.econg.rvAdapter.ProjectVerAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.econg.utils.loadImageSetView
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class DetailCompanyActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailCompanyBinding.inflate(layoutInflater)}
    var projects: MutableList<Project>? = mutableListOf()//사용자 프로젝트 데이터
    var companyProjectAdapter = ProjectAdapter(this)//인기 프로젝트 어댑터
    var str = ""
    val storage = Firebase.storage(ECONG_URL)
    var user: UserProfile? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            //binding.productName.text = intent.getStringExtra("id")
            str = intent.getStringExtra("id").toString()
        }

        loadUserData()

        setSupportActionBar(binding.detailCompanyToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)


        binding.userCard.setOnClickListener {
            var intent = Intent(this@DetailCompanyActivity, UserFollowActivity::class.java)
            intent.putExtra("id", str)
            intent.putExtra("name", binding.userName.text)
            startActivity(intent)
        }

        binding.btnFollow.setOnClickListener {
            pushFollow()
        }

        binding.btnUnfollow.setOnClickListener {
            pushFollow()
        }

        loadCompanyProjectData()
        companyProjectAdapter.setClickListener(onClickedProjectItem)
    }
    private fun loadUserData(){
        RetrofitManager.instance.getUserProfile(auth = API.HEADER_TOKEN, userId = str.toLong(), completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    user = responseBody
                    Log.d(Constants.TAG, "loadProjectInfo: $user")
                    setData()
                }
                RESPONSE_STATE.FAIL -> {
//                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "UserProfile: api call fail : $responseBody")
                }
            }
        })
    }

    private fun loadCompanyProjectData() {
        RetrofitManager.instance.getUserOpenedProjects(auth = API.HEADER_TOKEN, userId = str.toLong(), completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "UserOpenedProjectList: api call success : ${responseBody.toString()}")
                    projects = responseBody
                    companyProjectAdapter.setData(projects)
                    binding.companyProjects.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
                    binding.companyProjects.adapter = companyProjectAdapter
                }
                RESPONSE_STATE.FAIL -> {
//                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "UserOpenedProjectList: api call fail : $responseBody")
                }
            }
        })
    }

    private val onClickedProjectItem = object : ProjectAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@DetailCompanyActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }

    fun setData(){
        binding.userName.text = user!!.nickName
        if(user!!.authenticate) binding.authenticate.visibility = View.VISIBLE
        storage.loadImageSetView(user!!.profileUrl, binding.imgProfile)
        binding.followers.text = "팔로워 ${user!!.followerNum}명"
        binding.following.text = "팔로잉 ${user!!.followingNum}명"
        binding.description.text = user?.description

        if(user!!.myProfile){
            binding.btnFollow.visibility = View.GONE
            binding.btnUnfollow.visibility = View.GONE
        }

        else if(user!!.isFollow){
            binding.btnFollow.visibility = View.GONE
            binding.btnUnfollow.visibility = View.VISIBLE

        }
        else{
            binding.btnUnfollow.visibility = View.GONE
            binding.btnFollow.visibility = View.VISIBLE
        }
    }

    private fun pushFollow(){
        RetrofitManager.instance.pushFollow(auth = API.HEADER_TOKEN, userId = str.toLong(), completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    var result = responseBody
                    if(result.equals("팔로우 완료")) {
                        binding.btnFollow.visibility = View.GONE
                        binding.btnUnfollow.visibility = View.VISIBLE
                    }
                    else{
                        binding.btnUnfollow.visibility = View.GONE
                        binding.btnFollow.visibility = View.VISIBLE
                    }

                }

                RESPONSE_STATE.FAIL -> {
                    Log.d(Constants.TAG, "PUSHfOLLOW: api call fail : $responseBody")
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}