package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import oasis.team.econg.econg.followFragments.FollowerFragment
import oasis.team.econg.econg.followFragments.FollowingFragment
import oasis.team.econg.econg.databinding.ActivityUserFollowBinding

class UserFollowActivity : AppCompatActivity() {
    val binding by lazy{ ActivityUserFollowBinding.inflate(layoutInflater)}
    var userId = ""
    var userName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            userId = intent.getStringExtra("id").toString()
        }
        if(intent.hasExtra("name")){
            userName = intent.getStringExtra("name").toString()
        }

        setSupportActionBar(binding.userFollowToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.userFollowText.text = userName

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
            .replace(R.id.followFrame, FollowerFragment().newInstance(userId))
            .commitAllowingStateLoss()
    }

    fun showFollowingList(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.followFrame, FollowingFragment().newInstance(userId))
            .commitAllowingStateLoss()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}