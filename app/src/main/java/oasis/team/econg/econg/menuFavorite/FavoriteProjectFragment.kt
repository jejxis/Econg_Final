package oasis.team.econg.econg.menuFavorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.DetailProjectActivity
import oasis.team.econg.econg.MainActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.FragmentFavoriteBinding
import oasis.team.econg.econg.databinding.FragmentFavoriteProjectBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.ProjectVerAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class FavoriteProjectFragment : Fragment() {

    lateinit var binding: FragmentFavoriteProjectBinding
    lateinit var main: MainActivity

    var favProject: MutableList<Project>? = mutableListOf()//팔로우한 프로젝트 데이터
    lateinit var projectAdapter: ProjectVerAdapter//프로젝트 어댑터

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteProjectBinding.inflate(inflater,container, false)

        projectAdapter = ProjectVerAdapter(main)
        projectAdapter.setClickListener(onClickedListItem)

        loadFavProject()

        return binding.root
    }

    private fun loadFavProject() {//좋아요 프로젝트 데이터
        RetrofitManager.instance.getFavorites(auth = API.HEADER_TOKEN,  completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "UserOpenedProjectList: api call success : ${responseBody.toString()}")
                    favProject = responseBody
                    projectAdapter.setData(favProject)
                    binding.favProject.layoutManager = LinearLayoutManager(requireActivity(),
                        LinearLayoutManager.VERTICAL,false)
                    binding.favProject.adapter = projectAdapter
                }
                RESPONSE_STATE.FAIL -> {
//                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "UserOpenedProjectList: api call fail : $responseBody")
                }
            }
        })


    }

    //신규 프로젝트 클릭 이벤트 처리 -> 프로젝트 상세 화면으로 이동
    private val onClickedListItem = object : ProjectVerAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(context, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }
}