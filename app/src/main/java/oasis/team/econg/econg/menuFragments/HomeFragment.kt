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
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import oasis.team.econg.econg.*
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.FragmentHomeBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.CompanyHorAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE
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

        //리사이클러뷰 데이터 불러오기
        loadAll()

        //신규 프로젝트 클릭 리스너 달기
        projectAdapter.setClickListener(onClickedListItem)
        //신규 기업 클릭 리스너 달기
        newCompanyAdapter.setClickListener(onClickedCompanyItem)

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

    private fun loadAll(){
        loadData()
        loadNewCompany()
    }

    private fun loadData() {//신규 프로젝트 데이터
        RetrofitManager.instance.showProjects(auth = API.HEADER_TOKEN, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "ProjectListActivity: api call success : ${responseBody.toString()}")
                    projects = responseBody
                    projectAdapter.setData(projects)
                    binding.newProjects.layoutManager = LinearLayoutManager(main,
                        LinearLayoutManager.HORIZONTAL,false)
                    binding.newProjects.adapter = projectAdapter

                    //이미지 슬라이드
                    val pagerAdapter = ScreenSlidePagerAdapter(main)
                    binding.imgSlider.adapter = pagerAdapter
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(main, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "ProjectListActivity: api call fail : $responseBody")
                }
            }
        })
    }

    private fun loadNewCompany(){//신규 기업 데이터

        RetrofitManager.instance.getRecentUsers(auth = API.HEADER_TOKEN, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "UserList: api call success : ${responseBody.toString()}")
                    newUser = responseBody
                    newCompanyAdapter.setData(newUser)
                    binding.newCompany.layoutManager = LinearLayoutManager(main,LinearLayoutManager.HORIZONTAL,false)
                    binding.newCompany.adapter = newCompanyAdapter
                }
                RESPONSE_STATE.FAIL -> {
//                    Toast.makeText(main, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "UserList: api call fail : $responseBody")
                }
            }
        })


    }

    //이미지 슬라이드 어댑터
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> ImageSlideFragment().newInstance(projects?.get(0)?.thumbnail?:"gs://econg-7e3f6.appspot.com/bud.png")
                1 -> ImageSlideFragment().newInstance(projects?.get(1)?.thumbnail?:"gs://econg-7e3f6.appspot.com/bud.png")
                else -> ImageSlideFragment().newInstance(projects?.get(2)?.thumbnail?:"gs://econg-7e3f6.appspot.com/bud.png")
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