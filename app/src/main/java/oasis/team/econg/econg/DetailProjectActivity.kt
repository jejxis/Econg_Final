package oasis.team.econg.econg


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabItem
import oasis.team.econg.econg.data.ProjectDetail
import oasis.team.econg.econg.data.Reward
import oasis.team.econg.econg.databinding.ActivityDetailProjectBinding
import oasis.team.econg.econg.detailProjectFragments.DetailProjectCommunityFragment
import oasis.team.econg.econg.detailProjectFragments.DetailProjectStoryFragment
import oasis.team.econg.econg.dialog.FundDialog

class DetailProjectActivity : AppCompatActivity() {
    private val MYID = "2"
    private val binding by lazy{ActivityDetailProjectBinding.inflate(layoutInflater)}
    var project: ProjectDetail? = null
    var str = ""
    var isItFilled : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            //binding.productName.text = intent.getStringExtra("id")
            str = intent.getStringExtra("id").toString()
        }

        loadProjectInfo()
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

        binding.btnUp.setOnClickListener { binding.myScroll.scrollToView(binding.thumbnail) }

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
            val rewards : MutableList<Reward> = mutableListOf()
            for(i: Int in 1..5){
                rewards!!.add(
                    Reward(
                        i.toLong(),
                        "리워드$i",
                        5000,
                        100,
                        3,
                        "연필 하나, 볼펜 하나",
                        str.toLong()
                    )
                )
            }
            val dialog = FundDialog(this, rewards)
            dialog.isCancelable = true
            dialog.show(this.supportFragmentManager, "FundDialog")
        }
    }

    fun showProjectStory(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailProjectFrame, DetailProjectStoryFragment().newInstance(project!!.story))
            .commitAllowingStateLoss()
    }

    fun showProjectCommunity(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailProjectFrame, DetailProjectCommunityFragment().newInstance(MYID))
            .commitAllowingStateLoss()
    }

    private fun loadProjectInfo(){
        project = ProjectDetail(//프로젝트 상세화면에서 사용할 거.
            projectId = str.toLong(),
            title = "프로젝트$str",
            openingDate = "2022.08.23",
            closingDate = "2022.08.25",
            goalAmount = 5000000,
            totalAmount = 3500000,
            summary = "프로젝트${str}입니다.",
            supporter = 5000,
            status = 0,
            userid = str.toLong(),
            thumbnail = R.drawable.ic_baseline_favorite_border_pink_24,//나중에 String 으로 고치기.
            achievedRate = 70.0,
            story = getString(R.string.any))

        binding.projectName.text = project!!.title
        binding.projectSum.text = project!!.summary
        //binding.story.text = getString(R.string.any)
        binding.openingDate.text = project!!.openingDate
        binding.closingDate.text = project!!.closingDate
        binding.goalAmount.text = project!!.goalAmount.toString()
        binding.totalAmount.text = project!!.totalAmount.toString()
        binding.achievedRate.text = project!!.achievedRate.toString()
        binding.achievedProgress.progress = project!!.achievedRate.toInt()
        binding.thumbnail.setImageResource(project!!.thumbnail)//나중에 glide로..
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