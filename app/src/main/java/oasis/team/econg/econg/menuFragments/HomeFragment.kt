package oasis.team.econg.econg.menuFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.FragmentHomeBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class HomeFragment(context: Context) : Fragment() {

    lateinit var binding: FragmentHomeBinding
    var projects: MutableList<Project>? = mutableListOf()//데이터 담을 리스트
    var projectAdapter = ProjectAdapter(context)//어댑터 생성 -> 문제!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container, false)

        //이미지 슬라이드
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        binding.imgSlider.adapter = pagerAdapter

        //리사이클러뷰 데이터 불러오기
        loadAll()

        //리사이클러뷰 어댑터 달기
        projectAdapter.setClickListener(onClickedListItem)

        return binding.root
    }

    private fun loadAll(){
        loadData()
        loadPopularData()
    }

    private fun loadData() {
        for(i: Int in 1..20){
            projects!!.add(Project(i,
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

    private fun loadPopularData() {
        for(i: Int in 1..5){
            projects!!.add(Project(i,
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

    //이미지 슬라이드 어댑터
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> ImageSlideFragment(R.drawable.ic_baseline_favorite_24)
                1 -> ImageSlideFragment(R.drawable.ic_baseline_doorbell_24)
                else -> ImageSlideFragment(R.drawable.ic_baseline_category_24)
            }
        }
    }

    //리사이클러뷰 클릭 이벤트 처리
   private val onClickedListItem = object : ProjectAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            /*var intent = Intent(context, ??::class.java)
            intent.putExtra("id", id)
            startActivity(intent)*/
            Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }
}