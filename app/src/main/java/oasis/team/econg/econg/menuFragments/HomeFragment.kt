package oasis.team.econg.econg.menuFragments

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import oasis.team.econg.econg.*
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.FragmentHomeBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import oasis.team.econg.econg.rvAdapter.CompanyHorAdapter
import oasis.team.econg.forui.rvAdapter.ProjectAdapter
import java.lang.Math.abs

class HomeFragment(/*context: Context*/) : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var main: MainActivity
    //
    var projects: MutableList<Project>? = mutableListOf()//신규 프로젝트 데이터
    var newUser: MutableList<User>? = mutableListOf()//신규 기업 데이터

    lateinit var projectAdapter: ProjectAdapter//신규 프로젝트 어댑터
    lateinit var newCompanyAdapter: CompanyHorAdapter//신규 기업 어댑터

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container, false)

        projectAdapter = ProjectAdapter(main)
        newCompanyAdapter = CompanyHorAdapter(main)

        //이미지 슬라이드
        val pagerAdapter = ScreenSlidePagerAdapter(main)
        binding.imgSlider.adapter = pagerAdapter

        //리사이클러뷰 데이터 불러오기
        loadAll()

        //신규 프로젝트 클릭 리스너 달기
        projectAdapter.setClickListener(onClickedListItem)
        //신규 기업 클릭 리스너 달기
        newCompanyAdapter.setClickListener(onClickedCompanyItem)


        //버튼 이동 클릭리스너
        binding.btnHome.setOnClickListener {
            binding.myScroll.scrollToView(binding.imgSlider)
        }
        binding.btnCompany.setOnClickListener {
            binding.myScroll.scrollToView(binding.newC)
        }
        binding.btnCrowd.setOnClickListener {
            binding.myScroll.scrollToView(binding.newP)
        }

        binding.allNewProjects.setOnClickListener {//전체 프로젝트 리스트
            var intent = Intent(main, ProjectListActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        binding.allNewCompany.setOnClickListener { //전체 기업 리스트
            var intent = Intent(main, CompanyListActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        return binding.root
    }

    fun NestedScrollView.scrollToView(view: View) {
        val y = computeDistanceToView(view) - 100
        this.scrollTo(0, y)
    }

    internal fun NestedScrollView.computeDistanceToView(view: View): Int {
        return abs(calculateRectOnScreen(this).top - (this.scrollY + calculateRectOnScreen(view).top))
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

    private fun loadAll(){
        loadData()
        //loadPopularData()
        loadNewCompany()
        //loadPopularCompany()
    }

    private fun loadData() {//신규 프로젝트 데이터
        for(i: Int in 1..20){
            projects!!.add(Project(
                i,
                R.drawable.ic_baseline_category_24,
                "카테고리$i",
                "회사$i",
                "프로젝트$i",
                "프로젝트${i}인데요",
                150.0
            ))
        }

        projectAdapter.setData(projects)
        binding.newProjects.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.newProjects.adapter = projectAdapter
    }

    private fun loadNewCompany(){//신규 기업 데이터
        for(i: Int in 1..5){
            newUser!!.add(User(
            i,
            R.drawable.ic_baseline_category_24,
            "카테고리$i",
            "기업$i",
            "기업${i}입니다."))
        }
        newCompanyAdapter.setData(newUser)
        binding.newCompany.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.newCompany.adapter = newCompanyAdapter
    }

    //이미지 슬라이드 어댑터
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> ImageSlideFragment().newInstance(R.drawable.ic_baseline_favorite_pink_24)
                1 -> ImageSlideFragment().newInstance(R.drawable.ic_baseline_doorbell_24)
                else -> ImageSlideFragment().newInstance(R.drawable.ic_baseline_category_24)
            }
        }
    }

    //신규 프로젝트 클릭 이벤트 처리 -> 프로젝트 상세 화면으로 이동
   private val onClickedListItem = object : ProjectAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(context, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
   }

    //신규 기업 클릭 이벤트 처리 -> 기업 상세 화면으로 이동
    private val onClickedCompanyItem = object : CompanyHorAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(context, DetailCompanyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}