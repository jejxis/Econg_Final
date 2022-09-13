package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import oasis.team.econg.econg.followFragments.FollowerFragment
import oasis.team.econg.econg.followFragments.FollowingFragment
import oasis.team.econg.econg.databinding.ActivityUserFollowBinding

class UserFollowActivity : AppCompatActivity() {
    val binding by lazy{ ActivityUserFollowBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tabFollow.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val pos = tab.position
                when(pos){
                    0 -> showFollowerList()
                    1 -> showFollowingList()
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
            .replace(R.id.followFrame, FollowerFragment())
            .commitAllowingStateLoss()
    }

    fun showFollowingList(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.followFrame, FollowingFragment())
            .commitAllowingStateLoss()
    }
}