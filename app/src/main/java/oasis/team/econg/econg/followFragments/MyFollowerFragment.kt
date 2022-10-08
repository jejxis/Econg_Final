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
import oasis.team.econg.econg.MyFollowActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.databinding.FragmentMyFollowerBinding
import oasis.team.econg.econg.rvAdapter.UserFollowAdapter


class MyFollowerFragment : Fragment() {

    lateinit var binding: FragmentMyFollowerBinding
    lateinit var myFollow: MyFollowActivity

    var followerList: MutableList<User>? = mutableListOf()
    private lateinit var followerAdapter: UserFollowAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myFollow = context as MyFollowActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyFollowerBinding.inflate(inflater,container,false)
        followerAdapter = UserFollowAdapter(myFollow)

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