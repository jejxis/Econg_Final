package oasis.team.econg.econg.menuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.*
import oasis.team.econg.econg.data.UserEditProfile
import oasis.team.econg.econg.data.UserProfile
import oasis.team.econg.econg.data.UserTransfer
import oasis.team.econg.econg.databinding.FragmentHomeBinding
import oasis.team.econg.econg.databinding.FragmentMyBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.econg.utils.loadImageSetView

class MyFragment(/*context: Context*/) : Fragment() {
    lateinit var binding: FragmentMyBinding
    lateinit var main: MainActivity
    var myProfile: UserProfile? = null
    private val storage = Firebase.storage(Constants.ECONG_URL)

    var userId= ""
    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //내가쓴 댓글
        binding = FragmentMyBinding.inflate(inflater,container, false)

        loadData()

        binding.myInfo.setOnClickListener {
            var intent = Intent(main, MyFollowActivity::class.java)
            startActivity(intent)
        }

        binding.reply.setOnClickListener {
            var intent = Intent(main, MyCommunityActivity::class.java)
            intent.putExtra("userid", userId)//"user"
            startActivity(intent)
        }

        //내가 올린 프로젝트
        binding.myOpenedProjects.setOnClickListener {
            var intent = Intent(main, MyOpenedProjectsActivity::class.java)
            intent.putExtra("id",userId)
            startActivity(intent)
        }

        //프로젝트 올리기
        binding.openProject.setOnClickListener {
            var intent = Intent(main, OpenProjectActivity::class.java)
            startActivity(intent)
        }

        binding.orderList.setOnClickListener {
            var intent = Intent(main, OrderListActivity::class.java)
            startActivity(intent)
        }

        binding.btnEdit.setOnClickListener {
            var intent = Intent(main, EditProfileActivity::class.java)
            intent.putExtra("obj",
                UserTransfer(userId = myProfile!!.userId,nickName = myProfile!!.nickName,
                    profileUrl = myProfile!!.profileUrl, description = myProfile!!.description))
            startActivity(intent)
        }

        return binding.root
    }

    fun loadData(){
        RetrofitManager.instance.showMyInfo(auth = API.HEADER_TOKEN, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    myProfile = responseBody
                    Log.d(TAG, "MyFragment - loadData: $myProfile")
                    setData()
                }
                RESPONSE_STATE.FAIL ->{
                    Log.d(TAG, "MyFragment: api call fail : $responseBody")
                }
            }
        })
    }

    fun setData(){
        if(myProfile!!.authenticate) binding.authenticate.visibility = View.VISIBLE
        storage.loadImageSetView(myProfile!!.profileUrl, binding.imgProfile)
        binding.userName.text = myProfile!!.nickName
        binding.followers.text = "팔로워 ${myProfile!!.followerNum}명"
        binding.following.text = "팔로잉 ${myProfile!!.followingNum}명"
        binding.description.text = myProfile!!.description
        userId= myProfile!!.userId.toString()
    }
}