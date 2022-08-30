package oasis.team.econg.econg.menuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import oasis.team.econg.econg.DetailCompanyActivity
import oasis.team.econg.econg.DetailProjectActivity
import oasis.team.econg.econg.MainActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.Company
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.FragmentFavoriteBinding
import oasis.team.econg.econg.databinding.FragmentHomeBinding
import oasis.team.econg.econg.menuFavorite.FavoriteCompanyFragment
import oasis.team.econg.econg.menuFavorite.FavoriteProjectFragment
import oasis.team.econg.econg.rvAdapter.CompanyHorAdapter
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var main: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater,container, false)

        favoriteProject()

        binding.tabMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val pos = tab.position
                when(pos){
                    0 -> favoriteProject()
                    1 -> favoriteCompany()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        return binding.root
    }

    fun favoriteProject(){//좋아요 누른 프로젝트
        childFragmentManager.beginTransaction()
            .replace(R.id.favoriteFrame, FavoriteProjectFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun favoriteCompany(){//팔로우한 기업
        childFragmentManager.beginTransaction()
            .replace(R.id.favoriteFrame, FavoriteCompanyFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}