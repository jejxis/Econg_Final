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
import oasis.team.econg.econg.data.Community
import oasis.team.econg.econg.data.Company
import oasis.team.econg.econg.data.ProjectReply
import oasis.team.econg.econg.databinding.FragmentDetailProjectCommunityBinding
import oasis.team.econg.econg.rvAdapter.ProjectCommunityAdapter


class DetailProjectCommunityFragment : Fragment() {

    lateinit var binding: FragmentDetailProjectCommunityBinding
    lateinit var detailProject: DetailProjectActivity

    var projectReply: MutableList<ProjectReply>? = mutableListOf()
    lateinit var projectReplyAdapter :ProjectCommunityAdapter

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

        loadData()

        projectReplyAdapter.setClickListener(onClickedListItem)

        return binding.root
    }

    private val onClickedListItem = object : ProjectCommunityAdapter.OnItemClickListener{
        override fun onClicked(id: String) {

        }
    }

    private fun loadData(){
        for(i: Int in 1..5){
            projectReply!!.add(ProjectReply(
                Company(
                    i,
                    R.drawable.ic_baseline_favorite_pink_24,
                    "",
                    "사용자$i",
                    ""
                ),
                Community(
                    i.toLong(),
                    "댓글${i}입니다.",
                    detailProject.str.toLong(),
                    i.toLong()
                )
            ))
        }

        projectReplyAdapter.setData(projectReply)
        binding.projectCommunity.layoutManager = LinearLayoutManager(detailProject,
        LinearLayoutManager.VERTICAL, false)
        binding.projectCommunity.adapter = projectReplyAdapter
    }

}