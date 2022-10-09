package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.ActivityMyOpenedProjectsBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.ProjectVerAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE

class MyOpenedProjectsActivity : AppCompatActivity() {

    val binding by lazy{ActivityMyOpenedProjectsBinding.inflate(layoutInflater)}

    var projects: MutableList<Project>? = mutableListOf()//신규 프로젝트 데이터
    var projectAdapter = ProjectVerAdapter(this)//신규 프로젝트 어댑터

    var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            //binding.productName.text = intent.getStringExtra("id")
            userId = intent.getStringExtra("id").toString()
        }

        loadData()

        //내가 올린 프로젝트 클릭 리스너 달기
        projectAdapter.setClickListener(onClickedListItem)
    }

    private fun loadData() {
        RetrofitManager.instance.getUserOpenedProjects(auth = API.HEADER_TOKEN, userId = userId.toLong(), completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "UserOpenedProjectList: api call success : ${responseBody.toString()}")
                    projects = responseBody
                    projectAdapter.setData(projects)
                    binding.myProjects.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    binding.myProjects.adapter = projectAdapter
                }
                RESPONSE_STATE.FAIL -> {
//                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "UserOpenedProjectList: api call fail : $responseBody")
                }
            }
        })


    }

    private val onClickedListItem = object : ProjectVerAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@MyOpenedProjectsActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            //Toast.makeText(activity, "프로젝트${id}입니다.", Toast.LENGTH_SHORT).show()
            Log.d("MY", "onClicked: ")
        }
    }
}