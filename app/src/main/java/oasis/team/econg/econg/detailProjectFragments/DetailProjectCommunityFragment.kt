package oasis.team.econg.econg.detailProjectFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.DetailProjectActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.ProjectCommunity
import oasis.team.econg.econg.databinding.FragmentDetailProjectCommunityBinding
import oasis.team.econg.econg.rvAdapter.ProjectCommunityAdapter
import java.time.LocalDateTime


class DetailProjectCommunityFragment : Fragment() {

    lateinit var binding: FragmentDetailProjectCommunityBinding
    lateinit var detailProject: DetailProjectActivity

    var projectCommunity: MutableList<ProjectCommunity>? = mutableListOf()
    lateinit var projectReplyAdapter :ProjectCommunityAdapter

    private val MyID = "KEY"
    fun newInstance(data: String) = DetailProjectCommunityFragment().apply {
        arguments = Bundle().apply {
            putString(MyID, data)
        }
    }

    private val myId by lazy { requireArguments().getString(MyID) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        detailProject = context as DetailProjectActivity
        projectReplyAdapter = ProjectCommunityAdapter(detailProject, myId!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProjectCommunityBinding.inflate(inflater, container, false)

        loadData()

        //projectReplyAdapter.setClickListener(onClickedListItem)
        binding.uploadReply.setOnClickListener {
            uploadCommunity()
            binding.replyPlace.text.clear()
        }

        return binding.root
    }

    private fun uploadCommunity() {//API8 :/projectId/communities
        projectCommunity!!.add(
            ProjectCommunity(
            id =  200,
            content = binding.replyPlace.text.toString(),
            userId = myId!!.toLong(),
            userName = "content200",
            userProfileUrl = "gs://econg-7e3f6.appspot.com/bud.png"
            )
        )

        setListData()
    }

    /*private val onClickedListItem = object : ProjectCommunityAdapter.OnItemClickListener{
        override fun onClicked(id: String) {

        }
    }*/

    private fun loadData(){
        for(i: Int in 1..5){
            projectCommunity!!.add(
                ProjectCommunity(
                    id =  i,
                    content = "content200",
                    userId = 200,
                    userName = "누구게",
                    userProfileUrl = "gs://econg-7e3f6.appspot.com/bud.png"
                )
            )
        }

        setListData()
    }

    private fun setListData(){
        projectReplyAdapter.setData(projectCommunity)
        binding.projectCommunity.layoutManager = LinearLayoutManager(detailProject,
            LinearLayoutManager.VERTICAL, false)
        binding.projectCommunity.adapter = projectReplyAdapter
    }

}