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
import oasis.team.econg.econg.MyFollowActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.data.UserForFollow
import oasis.team.econg.econg.databinding.FragmentMyFollowingBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.UserFollowAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE


class MyFollowingFragment : Fragment() {

    lateinit var binding: FragmentMyFollowingBinding
    lateinit var myFollow: MyFollowActivity

    var followingList: MutableList<UserForFollow>? = mutableListOf()
    lateinit var followingAdapter: UserFollowAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myFollow = context as MyFollowActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyFollowingBinding.inflate(inflater,container,false)
        followingAdapter = UserFollowAdapter(myFollow)

        loadFollowingList()
        followingAdapter.setClickListener(onClickedUserItem)
        followingAdapter.setBtnFollowListener(btnFollowListener)
        followingAdapter.setBtnUnfollowListener(btnUnfollowListener)

        return binding.root
    }
    private fun loadFollowingList(){
        RetrofitManager.instance.getMyFollowings(auth = API.HEADER_TOKEN, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    followingList = responseBody
                    Log.d(TAG, "load MyFollowingList success: ${responseBody.toString()}")
                    followingAdapter.setData(followingList)
                    binding.followingList.layoutManager = LinearLayoutManager(requireActivity(),
                        LinearLayoutManager.VERTICAL,false)
                    binding.followingList.adapter = followingAdapter
                }
                RESPONSE_STATE.FAIL->{
                    Log.d(TAG, "load MyFollowingList fail: ${responseBody.toString()}")
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