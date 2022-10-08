package oasis.team.econg.econg.detailProjectFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.DetailProjectActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.ProjectCommunity
import oasis.team.econg.econg.databinding.FragmentDetailProjectCommunityBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.ProjectCommunityAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE
import java.time.LocalDateTime


class DetailProjectCommunityFragment : Fragment() {

    lateinit var binding: FragmentDetailProjectCommunityBinding
    lateinit var detailProject: DetailProjectActivity

    var projectCommunityList: MutableList<ProjectCommunity>? = mutableListOf()
    lateinit var projectReplyAdapter :ProjectCommunityAdapter

    private val projectID = "KEY"
    fun newInstance(data: String) = DetailProjectCommunityFragment().apply {
        arguments = Bundle().apply {
            putString(projectID, data)
        }
    }

    private val projectId by lazy { requireArguments().getString(projectID) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        detailProject = context as DetailProjectActivity
        projectReplyAdapter = ProjectCommunityAdapter(detailProject)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProjectCommunityBinding.inflate(inflater, container, false)
        Log.d(TAG, "DetailProjectCommunityFragment projectId: $projectId")
        loadData()

        //projectReplyAdapter.setClickListener(onClickedListItem)
        binding.uploadReply.setOnClickListener {
            uploadCommunity()
            binding.replyPlace.text.clear()
        }

        return binding.root
    }

    private fun uploadCommunity() {//API8 :/projectId/communities
        RetrofitManager.instance.postProjectCommunity(auth = API.HEADER_TOKEN, projectId = projectId!!.toLong(),content = binding.replyPlace.text.toString(), completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    loadData()
                }
                RESPONSE_STATE.FAIL ->{
                    Log.d(TAG, "uploadCommunity: $responseBody")
                    Toast.makeText(detailProject, "결제 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        setListData()
    }


    private fun loadData(){
        RetrofitManager.instance.showProjectCommunities(auth = API.HEADER_TOKEN, projectId = projectId!!.toLong(), completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    Log.d(TAG, "loadData: ProjectCommunityList: api call Success: ${responseBody.toString()}")
                    projectCommunityList = responseBody
                    setListData()
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "loadData: ProjectCommunityList: api call fail : $responseBody")
                }
            }
        })

    }

    private fun setListData(){
        projectReplyAdapter.setData(projectCommunityList)
        binding.projectCommunity.layoutManager = LinearLayoutManager(detailProject,
            LinearLayoutManager.VERTICAL, false)
        binding.projectCommunity.adapter = projectReplyAdapter
    }

}