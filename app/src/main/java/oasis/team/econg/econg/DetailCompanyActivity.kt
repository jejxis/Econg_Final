package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.ActivityDetailCompanyBinding
import oasis.team.econg.econg.rvAdapter.ProjectVerAdapter
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class DetailCompanyActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailCompanyBinding.inflate(layoutInflater)}
    var projects: MutableList<Project>? = mutableListOf()//사용자 프로젝트 데이터
    var companyProjectAdapter = ProjectVerAdapter(this)//인기 프로젝트 어댑터
    var str = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            //binding.productName.text = intent.getStringExtra("id")
            str = intent.getStringExtra("id").toString()
        }

        binding.userName.text = "회사 $str"

        loadCompanyProjectData()
        companyProjectAdapter.setClickListener(onClickedListItem)
    }

    private fun loadCompanyProjectData() {
        for(i: Int in 1..3){
            projects!!.add(Project(
                i,
                R.drawable.ic_baseline_category_24,
                "카테고리$i",
                "회사 $str",
                "프로젝트$i",
                "프로젝트${i}인데요",
                150.0
            ))
        }

        companyProjectAdapter.setData(projects)
        binding.companyProjects.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.companyProjects.adapter = companyProjectAdapter
    }

    private val onClickedListItem = object : ProjectAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@DetailCompanyActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }
}