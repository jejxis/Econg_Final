package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import oasis.team.econg.econg.databinding.ActivityMyFollowBinding
import oasis.team.econg.econg.followFragments.FollowerFragment
import oasis.team.econg.econg.followFragments.FollowingFragment
import oasis.team.econg.econg.followFragments.MyFollowerFragment
import oasis.team.econg.econg.followFragments.MyFollowingFragment

class MyFollowActivity : AppCompatActivity() {
    val binding by lazy {ActivityMyFollowBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showFollowingList()

        binding.tabFollow.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val pos = tab.position
                when(pos){
                    0 -> showFollowingList()
                    1 -> showFollowerList()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    fun showFollowerList(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.followFrame, MyFollowerFragment())
            .commitAllowingStateLoss()
    }

    fun showFollowingList(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.followFrame, MyFollowingFragment())
            .commitAllowingStateLoss()
    }
}