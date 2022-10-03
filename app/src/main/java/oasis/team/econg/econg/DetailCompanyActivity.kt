package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.data.UserProfile
import oasis.team.econg.econg.databinding.ActivityDetailCompanyBinding
import oasis.team.econg.econg.rvAdapter.CompanyVerAdapter
import oasis.team.econg.econg.rvAdapter.ProjectVerAdapter
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.loadImageSetView
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class DetailCompanyActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailCompanyBinding.inflate(layoutInflater)}
    var projects: MutableList<Project>? = mutableListOf()//사용자 프로젝트 데이터
    var companyProjectAdapter = ProjectAdapter(this)//인기 프로젝트 어댑터
    var str = ""
    val storage = Firebase.storage(ECONG_URL)
    lateinit var user: UserProfile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            //binding.productName.text = intent.getStringExtra("id")
            str = intent.getStringExtra("id").toString()
        }

        loadUserData()
        setData()

        binding.userCard.setOnClickListener {
            var intent = Intent(this@DetailCompanyActivity, UserFollowActivity::class.java)
            intent.putExtra("id", str)
            startActivity(intent)
        }

        binding.btnFollow.setOnClickListener {
            binding.btnFollow.visibility = View.GONE
            binding.btnUnfollow.visibility = View.VISIBLE
        }

        binding.btnUnfollow.setOnClickListener {
            binding.btnUnfollow.visibility = View.GONE
            binding.btnFollow.visibility = View.VISIBLE
        }

        loadCompanyProjectData()
        companyProjectAdapter.setClickListener(onClickedProjectItem)
    }
    private fun loadUserData(){//API14 /app/profiles
        user = UserProfile(
            userId = str.toLong(),
            nickName = "User${str}",
            description = "저는 김치찌개를 좋아합니다.",
            profileUrl = "gs://econg-7e3f6.appspot.com/bud.png",
            authenticate = true,
            followerNum = 5,
            followingNum = 3,
            myProfile = false
        )
    }

    private fun loadCompanyProjectData() {
        for(i: Int in 1..3){
            projects!!.add(Project(
                i.toLong(),
                "프로젝트${i}",
                "2022-08-25",
                "2022-08-28",
                20000000,
                "gs://econg-7e3f6.appspot.com/bud.png",
                "프로젝트${i}인데요",
                false,
                "사용자${i}",
                75
            ))
        }

        companyProjectAdapter.setData(projects)
        binding.companyProjects.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.companyProjects.adapter = companyProjectAdapter
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
        binding.userName.text = user.nickName
        if(user.authenticate) binding.authenticate.visibility = View.VISIBLE
        storage.loadImageSetView(user.profileUrl, binding.imgProfile)
        binding.followers.text = "팔로워 ${user.followerNum}명"
        binding.following.text = "팔로잉 ${user.followingNum}명"
        binding.description.text = user.description
    }
}