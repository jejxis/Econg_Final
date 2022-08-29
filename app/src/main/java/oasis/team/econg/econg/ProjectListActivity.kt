package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.ActivityProjectListBinding
import oasis.team.econg.econg.rvAdapter.ProjectVerAdapter
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class ProjectListActivity : AppCompatActivity() {
    val binding by lazy{ ActivityProjectListBinding.inflate(layoutInflater)}

    var projects: MutableList<Project>? = mutableListOf()//신규 프로젝트 데이터
    var projectAdapter = ProjectVerAdapter(this)//신규 프로젝트 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        //신규 프로젝트 클릭 리스너 달기
        projectAdapter.setClickListener(onClickedListItem)
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
        binding.projects.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.projects.adapter = projectAdapter
    }

    //신규 프로젝트 클릭 이벤트 처리 -> 프로젝트 상세 화면으로 이동
    private val onClickedListItem = object : ProjectAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@ProjectListActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }
}