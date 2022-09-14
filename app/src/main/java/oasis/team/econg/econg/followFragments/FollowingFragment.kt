package oasis.team.econg.econg.followFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.DetailCompanyActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.UserFollowActivity
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.databinding.FragmentFollowingBinding
import oasis.team.econg.econg.rvAdapter.UserFollowAdapter


class FollowingFragment : Fragment() {

    lateinit var binding: FragmentFollowingBinding
    lateinit var userFollow: UserFollowActivity

    var followingList: MutableList<User>? = mutableListOf()
    lateinit var followingAdapter: UserFollowAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userFollow = context as UserFollowActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        followingAdapter = UserFollowAdapter(userFollow)

        loadFollowingList()
        followingAdapter.setClickListener(onClickedUserItem)

        return binding.root
    }

    private fun loadFollowingList(){
        followingList = mutableListOf()
        for(i: Int in 1..5){
            followingList!!.add(User(
                i,
                R.drawable.ic_baseline_category_24,
                "카테고리$i",
                "기업$i",
                "기업${i}입니다."))
        }
        followingAdapter.setData(followingList)
        binding.followingList.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
        binding.followingList.adapter = followingAdapter
    }

    private val onClickedUserItem = object: UserFollowAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(context, DetailCompanyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}