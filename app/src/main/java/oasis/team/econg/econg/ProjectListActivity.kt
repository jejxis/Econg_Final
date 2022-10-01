package oasis.team.econg.econg

import android.content.Intent
import android.graphics.Color
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

        binding.btnAll.setOnClickListener {
            binding.btnAll.background = resources.getDrawable(R.drawable.button_selected, null)
            binding.btnAll.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnNinety.background = resources.getDrawable(R.drawable.button, null)
            binding.btnNinety.setTextColor(Color.parseColor("#787878"))
            loadData()
        }

        binding.btnNinety.setOnClickListener {
            binding.btnNinety.background = resources.getDrawable(R.drawable.button_selected, null)
            binding.btnNinety.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnAll.background = resources.getDrawable(R.drawable.button, null)
            binding.btnAll.setTextColor(Color.parseColor("#787878"))
            loadData90()
        }
    }

    private fun loadData() {//신규 프로젝트 데이터
        projects = mutableListOf()
        for(i: Int in 1..20){
            projects!!.add(Project(
                i.toLong(),
                "프로젝트${i}",
                "2022-08-25",
                "2022-08-28",
                20000000,
                "gs://econg-7e3f6.appspot.com/bud.png",
                "프로젝트${i}인데요",
                false,
                "사용자${i}",
                75
            ))
        }

        projectAdapter.setData(projects)
        binding.projects.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.projects.adapter = projectAdapter
    }

    private fun loadData90() {//신규 프로젝트 데이터
        projects = mutableListOf()
        for(i: Int in 21..40){
            projects!!.add(Project(
                i.toLong(),
                "프로젝트${i}",
                "2022-08-25",
                "2022-08-28",
                20000000,
                "gs://econg-7e3f6.appspot.com/bud.png",
                "프로젝트${i}인데요",
                false,
                "사용자${i}",
                75
            ))
        }

        projectAdapter.setData(projects)
        binding.projects.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.projects.adapter = projectAdapter
    }

    //신규 프로젝트 클릭 이벤트 처리 -> 프로젝트 상세 화면으로 이동
    private val onClickedListItem = object : ProjectVerAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@ProjectListActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }
}