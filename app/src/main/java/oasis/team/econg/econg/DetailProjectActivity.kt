package oasis.team.econg.econg

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import oasis.team.econg.econg.data.ProjectDetail
import oasis.team.econg.econg.databinding.ActivityDetailProjectBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import oasis.team.econg.econg.menuFragments.HomeFragment

class DetailProjectActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailProjectBinding.inflate(layoutInflater)}
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
            achievedRate = 70.0)

        binding.projectName.text = project!!.title
        binding.projectSum.text = project!!.summary
        binding.story.text = getString(R.string.any)
        binding.openingDate.text = project!!.openingDate
        binding.closingDate.text = project!!.closingDate
        binding.goalAmount.text = project!!.goalAmount.toString()
        binding.totalAmount.text = project!!.totalAmount.toString()
        binding.achievedRate.text = project!!.achievedRate.toString()
        binding.achievedProgress.progress = project!!.achievedRate.toInt()
        binding.thumbnail.setImageResource(project!!.thumbnail)//나중에 glide로..

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