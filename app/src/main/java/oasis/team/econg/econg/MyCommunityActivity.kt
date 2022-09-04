package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Community
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.data.Reply
import oasis.team.econg.econg.databinding.ActivityMyCommunityBinding
import oasis.team.econg.econg.rvAdapter.CommunityAdapter
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

//data class Reply(val project: Project, val community: Community)
//data class Project(val id: Int, val img: Int, val category: String, val company: String, val projectName: String, val projectInfo: String, val achRate: Double)
//data class Community(val comment: String, val projectId: Long, val userid: Long)
class MyCommunityActivity : AppCompatActivity() {
    val binding by lazy{ActivityMyCommunityBinding.inflate(layoutInflater)}

    private var reply: MutableList<Reply>? = mutableListOf()
    private var replyAdapter = CommunityAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        replyAdapter.setClickListener(onClickedListItem)
    }

    private val onClickedListItem = object : CommunityAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@MyCommunityActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }

    private fun loadData() {//신규 프로젝트 데이터
        for(i: Int in 1..20){
            reply!!.add(Reply(Project(
                i,
                R.drawable.ic_baseline_category_24,
                "카테고리$i",
                "회사$i",
                "프로젝트$i",
                "프로젝트${i}인데요",
                150.0
            ),Community(
                i.toLong(),
                "댓글 i입니다.",
                i.toLong(),
                1
            )))
        }

        replyAdapter.setData(reply)
        binding.communityList.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.communityList.adapter = replyAdapter
    }
}