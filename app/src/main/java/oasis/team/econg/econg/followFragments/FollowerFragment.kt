package oasis.team.econg.econg.followFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.DetailCompanyActivity
import oasis.team.econg.econg.MainActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.UserFollowActivity
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.data.UserForFollow
import oasis.team.econg.econg.databinding.FragmentFollowerBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.UserFollowAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE

class FollowerFragment : Fragment() {

    lateinit var binding: FragmentFollowerBinding
    lateinit var userFollow: UserFollowActivity

    var followerList: MutableList<UserForFollow>? = mutableListOf()
    private lateinit var followerAdapter: UserFollowAdapter

    private val userID = "KEY"
    fun newInstance(data: String) = FollowerFragment().apply {
        arguments = Bundle().apply {
            putString(userID, data)
        }
    }

    private val userId by lazy { requireArguments().getString(userID) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userFollow = context as UserFollowActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        followerAdapter = UserFollowAdapter(userFollow)

        loadFollowerList()
        followerAdapter.setClickListener(onClickedUserItem)
        followerAdapter.setBtnFollowListener(btnFollowListener)
        followerAdapter.setBtnUnfollowListener(btnUnfollowListener)

        return binding.root
    }

    private fun loadFollowerList(){
        RetrofitManager.instance.getUserFollowers(auth = API.HEADER_TOKEN, userId = userId!!.toLong(), completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    followerList = responseBody
                    Log.d(Constants.TAG, "load UserFollowerList success: ${responseBody.toString()}")
                    followerAdapter.setData(followerList)
                    binding.followerList.layoutManager = LinearLayoutManager(requireActivity(),
                        LinearLayoutManager.VERTICAL,false)
                    binding.followerList.adapter = followerAdapter
                }
                RESPONSE_STATE.FAIL->{
                    Log.d(Constants.TAG, "load MyFollowerList fail: ${responseBody.toString()}")
                }
            }
        })

    }

    private val onClickedUserItem = object: UserFollowAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(context, DetailCompanyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private val btnFollowListener = object: UserFollowAdapter.BtnFollowListener{
        override fun follow(userId: Long) {
            RetrofitManager.instance.pushFollow(auth = API.HEADER_TOKEN, userId= userId, completion = {
                    responseState, responseBody->
                when(responseState){
                    RESPONSE_STATE.OKAY->{
                        Log.d(Constants.TAG, "follow - success: $responseBody")
                    }
                    RESPONSE_STATE.FAIL->{
                        Log.d(Constants.TAG, "follow - fail: $responseBody")
                    }
                }
            })
        }
    }

    private val btnUnfollowListener = object: UserFollowAdapter.BtnUnfollowListener{
        override fun unfollow(userId: Long) {
            RetrofitManager.instance.pushFollow(auth = API.HEADER_TOKEN, userId= userId, completion = {
                    responseState, responseBody->
                when(responseState){
                    RESPONSE_STATE.OKAY->{
                        Log.d(Constants.TAG, "unfollow - success: $responseBody")
                    }
                    RESPONSE_STATE.FAIL->{
                        Log.d(Constants.TAG, "unfollow - fail: $responseBody")
                    }
                }
            })
        }
    }
}