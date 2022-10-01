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
import oasis.team.econg.econg.databinding.FragmentFollowerBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import oasis.team.econg.econg.rvAdapter.UserFollowAdapter

class FollowerFragment : Fragment() {

    lateinit var binding: FragmentFollowerBinding
    lateinit var userFollow: UserFollowActivity

    var followerList: MutableList<User>? = mutableListOf()
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

        return binding.root
    }

    private fun loadFollowerList(){
        followerList = mutableListOf()
        for(i: Int in 1..5){
            followerList!!.add(User(
                i.toLong(),
                "사용자$i",
                null,
                "gs://econg-7e3f6.appspot.com/bud.png",
                true))
        }
        followerAdapter.setData(followerList)
        binding.followerList.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
        binding.followerList.adapter = followerAdapter
    }

    private val onClickedUserItem = object: UserFollowAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(context, DetailCompanyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}