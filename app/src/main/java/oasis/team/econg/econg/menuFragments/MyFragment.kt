package oasis.team.econg.econg.menuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.econg.*
import oasis.team.econg.econg.databinding.FragmentHomeBinding
import oasis.team.econg.econg.databinding.FragmentMyBinding

class MyFragment(/*context: Context*/) : Fragment() {
    lateinit var binding: FragmentMyBinding
    lateinit var main: MainActivity

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
        binding.reply.setOnClickListener {
            var intent = Intent(main, MyCommunityActivity::class.java)
            intent.putExtra("userid", "user")
            startActivity(intent)
        }

        binding.mySupportedProjects.setOnClickListener {
            var intent = Intent(main, MySupportedProjectsActivity::class.java)
            startActivity(intent)
        }

        //내가 올린 프로젝트
        binding.myOpenedProjects.setOnClickListener {
            var intent = Intent(main, MyOpenedProjectsActivity::class.java)
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

        return binding.root
    }


}