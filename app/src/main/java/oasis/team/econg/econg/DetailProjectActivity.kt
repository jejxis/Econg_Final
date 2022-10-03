package oasis.team.econg.econg


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
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
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import java.util.ArrayList

class DetailProjectActivity : AppCompatActivity() {
    private val MYID = "2"
    private val binding by lazy{ActivityDetailProjectBinding.inflate(layoutInflater)}
    var project: ProjectDetail? = null
    var str = ""
    var isItFilled : Boolean = false

    var imgUrls = mutableListOf<ProjectImage>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            //binding.productName.text = intent.getStringExtra("id")
            str = intent.getStringExtra("id").toString()
        }

        loadProjectInfo()
        val pagerAdapter = ScreenSlidePagerAdapter(this@DetailProjectActivity)
        binding.projectImageSlider.adapter = pagerAdapter
        showProjectStory()

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
            if(isItFilled){//팔로우된 상태
                binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_pink_24)
                isItFilled = false
            }else{//팔로우 안된 상태
                binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_pink_24)
                isItFilled = true
            }
        }

        binding.btnFund.setOnClickListener {
            /*val rewards : MutableList<SimpleReward> = mutableListOf()
            for(i: Int in 1..5){
                rewards!!.add(
                    SimpleReward(//프로젝트 상세화면...결제 다이얼로그
                        rewardId = i.toLong(),
                        name = "reward$i",
                        price = 5000,
                        stock = 500,
                        soldQuantity = 200,
                        combination =  "연필 ${i}자루 + 볼펜 ${i}자루"
                    )
                )
            }*/
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
            .replace(R.id.detailProjectFrame, DetailProjectCommunityFragment().newInstance(MYID))
            .commitAllowingStateLoss()
    }

    private fun loadProjectInfo(){
        project = ProjectDetail(//프로젝트 상세화면에서 사용할 거.
            id = str.toLong(),
            title = "프로젝트$str",
            openingDate = "2022.08.23",
            closingDate = "2022.08.25",
            goalAmount = 5000000,
            totalAmount = 3500000,
            summary = "프로젝트${str}입니다.",
            content = getString(R.string.any),
            thumbnail = "gs://econg-7e3f6.appspot.com/bud.png",
            projectAuthenticate = true,
            favorite = true,
            userId = str.toLong(),
            userName = "사용자$str",
            userAuthenticate = true,
            projectImgList = arrayListOf(
                ProjectImage(1,
                    "gs://econg-7e3f6.appspot.com/images/temp_1662087387101.jpeg"),
                ProjectImage(2,"gs://econg-7e3f6.appspot.com/images/temp_1664617049408.jpeg")
            ),
            rewardList = arrayListOf(
                SimpleReward(//프로젝트 상세화면...결제 다이얼로그
                    rewardId = 11.toLong(),
                    name = "reward11",
                    price = 5000,
                    stock = 500,
                    soldQuantity = 200,
                    combination =  "연필 11자루 + 볼펜 11자루"
                ),
                SimpleReward(//프로젝트 상세화면...결제 다이얼로그
                    rewardId = 22,
                    name = "reward22",
                    price = 5000,
                    stock = 500,
                    soldQuantity = 200,
                    combination =  "연필 22자루 + 볼펜 22자루"
                )
            )
        )

        val achievedRate: Int = (project!!.totalAmount / project!!.goalAmount) * 100

        binding.projectName.text = project!!.title
        binding.projectSum.text = project!!.summary
        binding.openingDate.text = project!!.openingDate
        binding.closingDate.text = project!!.closingDate
        binding.goalAmount.text = project!!.goalAmount.toString()
        binding.totalAmount.text = project!!.totalAmount.toString()
        binding.achievedRate.text = achievedRate.toString()
        binding.achievedProgress.progress = achievedRate

        if(project!!.projectAuthenticate == true){
            binding.projectEcoAuth.visibility = View.VISIBLE
        }

        if(project!!.userAuthenticate == true){
            binding.userEcoAuth.visibility = View.VISIBLE
        }

        binding.user.text = project!!.userName

        binding.goToDetailCompany.setOnClickListener {
            var intent = Intent(this@DetailProjectActivity, DetailCompanyActivity::class.java)
            intent.putExtra("id", project!!.userId.toString())
            startActivity(intent)
        }


    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = project!!.projectImgList.size

        override fun createFragment(position: Int): Fragment {
            if(position == 0) return ImageSlideFragment().newInstance(project!!.thumbnail)
            else return ImageSlideFragment().newInstance(project!!.projectImgList[position-1].productImgUrl)
        }
    }

    fun NestedScrollView.scrollToView(view: View) {
        val y = computeDistanceToView(view) - 100
        this.scrollTo(0, y)
    }

    internal fun NestedScrollView.computeDistanceToView(view: View): Int {
        return Math.abs(calculateRectOnScreen(this).top - (this.scrollY + calculateRectOnScreen(view).top))
    }

    internal fun calculateRectOnScreen(view: View): Rect {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return Rect(
            location[0],
            location[1],
            location[0] + view.measuredWidth,
            location[1] + view.measuredHeight
        )
    }
}