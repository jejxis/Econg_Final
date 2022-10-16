package oasis.team.econg.econg


import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabItem
import oasis.team.econg.econg.data.*
import oasis.team.econg.econg.databinding.ActivityDetailProjectBinding
import oasis.team.econg.econg.detailProjectFragments.DetailProjectCommunityFragment
import oasis.team.econg.econg.detailProjectFragments.DetailProjectStoryFragment
import oasis.team.econg.econg.dialog.FundDialog
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.econg.utils.scrollToView
import java.util.ArrayList

class DetailProjectActivity : AppCompatActivity() {
    private val binding by lazy{ActivityDetailProjectBinding.inflate(layoutInflater)}
    var project: ProjectDetail? = null
    var str = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            str = intent.getStringExtra("id").toString()
        }

        setSupportActionBar(binding.detailProjectToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        loadProjectInfo()

        binding.btnEdit.setOnClickListener {
            Log.d("MY", "EDIT!!!!")
            var intent = Intent(this, EditProjectActivity::class.java)
            intent.putExtra("projectID", str)
            startActivity(intent)
        }

        binding.tabStoryCommunity.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val pos = tab.position
                when(pos){
                    0 -> showProjectStory()
                    1 -> showProjectCommunity()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.btnUp.setOnClickListener { binding.myScroll.scrollToView(binding.projectImageSlider) }

        binding.btnFav.setOnClickListener {
            pushFavorite()
        }

        binding.btnFund.setOnClickListener {
            if(project!!.status != "ONGOING"){
                Toast.makeText(this@DetailProjectActivity, "후원할 수 없는 프로젝트입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dialog = FundDialog(this, project!!.rewardList, project!!.id)
            dialog.isCancelable = true
            dialog.show(this.supportFragmentManager, "FundDialog")
        }
    }

    fun showProjectStory(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailProjectFrame, DetailProjectStoryFragment().newInstance(project!!.content))
            .commitAllowingStateLoss()
    }

    fun showProjectCommunity(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailProjectFrame, DetailProjectCommunityFragment().newInstance(str))
            .commitAllowingStateLoss()
    }

    private fun loadProjectInfo(){
        RetrofitManager.instance.showDetailProject(auth = API.HEADER_TOKEN, projectId = str.toLong(), completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    project = responseBody
                    Log.d(TAG, "loadProjectInfo: $project")
                    setData()
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "DetailProject: api call fail : $responseBody")
                    Toast.makeText(this@DetailProjectActivity, "데이터 로딩에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun pushFavorite(){
        RetrofitManager.instance.pushFavorite(auth = API.HEADER_TOKEN, projectId = str.toLong(), completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    var result = responseBody
                    if(result.equals("찜 등록"))
                        binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_pink_24)
                    else
                        binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_pink_24)

                }

                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "DetailProject: api call fail : $responseBody")
                    Toast.makeText(this@DetailProjectActivity, "찜콩 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun setData(){

        val achievedRate = (project!!.totalAmount * 100) / project!!.goalAmount

        binding.projectName.text = project!!.title
        binding.projectSum.text = project!!.summary
        binding.openingDate.text = project!!.openingDate
        binding.closingDate.text = project!!.closingDate
        binding.goalAmount.text = project!!.goalAmount.toString()
        binding.totalAmount.text = project!!.totalAmount.toString()
        binding.achievedRate.text = achievedRate.toString()
        binding.achievedProgress.progress = achievedRate
        binding.status.text = project!!.status

        binding.detailProjectText.text = project!!.title

        if(project!!.status != "ONGOING"){
            binding.btnFund.background = resources.getDrawable(R.drawable.button, null)
            binding.btnFund.setTextColor(Color.parseColor("#787878"))
        }

        if(project!!.projectAuthenticate){
            binding.projectEcoAuth.visibility = View.VISIBLE
        }

        if(project!!.userAuthenticate){
            binding.userEcoAuth.visibility = View.VISIBLE
        }

        if(project!!.favorite){
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_pink_24)
        }else{
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_pink_24)
        }

        binding.user.text = project!!.userName

        binding.goToDetailCompany.setOnClickListener {
            var intent = Intent(this@DetailProjectActivity, DetailCompanyActivity::class.java)
            intent.putExtra("id", project!!.userId.toString())
            startActivity(intent)
        }

        val pagerAdapter = ScreenSlidePagerAdapter(this@DetailProjectActivity)
        binding.projectImageSlider.adapter = pagerAdapter
        showProjectStory()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = project!!.projectImgList.size

        override fun createFragment(position: Int): Fragment {
            if(position == 0) return ImageSlideFragment().newInstance(project!!.thumbnail)
            else return ImageSlideFragment().newInstance(project!!.projectImgList[position-1].projectImgUrl)
        }
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