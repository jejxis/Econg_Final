package oasis.team.econg.econg.menuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.econg.DetailCompanyActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.databinding.FragmentHomeBinding
import oasis.team.econg.econg.databinding.FragmentMyBinding

class MyFragment(context: Context) : Fragment() {
    lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBinding.inflate(inflater,container, false)
        binding.reply.setOnClickListener {
            var intent = Intent(context, DetailCompanyActivity::class.java)
            intent.putExtra("userid", id)
            startActivity(intent)
        }
        return binding.root
    }


}