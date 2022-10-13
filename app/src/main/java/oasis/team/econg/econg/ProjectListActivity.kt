package oasis.team.econg.econg

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.ActivityProjectListBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.ProjectVerAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class ProjectListActivity : AppCompatActivity() {
    val binding by lazy{ ActivityProjectListBinding.inflate(layoutInflater)}

    var projects: MutableList<Project>? = mutableListOf()//신규 프로젝트 데이터
    var projectAdapter = ProjectVerAdapter(this)//신규 프로젝트 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        setSupportActionBar(binding.projectListToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        //신규 프로젝트 클릭 리스너 달기
        projectAdapter.setClickListener(onClickedListItem)

        binding.btnAll.setOnClickListener {
            binding.btnAll.background = resources.getDrawable(R.drawable.button_selected, null)
            binding.btnAll.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnNinety.background = resources.getDrawable(R.drawable.button, null)
            binding.btnNinety.setTextColor(Color.parseColor("#787878"))
            binding.btnAlmost.background = resources.getDrawable(R.drawable.button, null)
            binding.btnAlmost.setTextColor(Color.parseColor("#787878"))
            loadData()
        }

        binding.btnNinety.setOnClickListener {
            binding.btnNinety.background = resources.getDrawable(R.drawable.button_selected, null)
            binding.btnNinety.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnAll.background = resources.getDrawable(R.drawable.button, null)
            binding.btnAll.setTextColor(Color.parseColor("#787878"))
            binding.btnAlmost.background = resources.getDrawable(R.drawable.button, null)
            binding.btnAlmost.setTextColor(Color.parseColor("#787878"))
            loadData90()
        }

        binding.btnAlmost.setOnClickListener {
            binding.btnAlmost.background = resources.getDrawable(R.drawable.button_selected, null)
            binding.btnAlmost.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnAll.background = resources.getDrawable(R.drawable.button, null)
            binding.btnAll.setTextColor(Color.parseColor("#787878"))
            binding.btnNinety.background = resources.getDrawable(R.drawable.button, null)
            binding.btnNinety.setTextColor(Color.parseColor("#787878"))
            loadDataDeadLine()
        }
    }

    private fun loadData() {//신규 프로젝트 데이터
        RetrofitManager.instance.showProjects(auth = API.HEADER_TOKEN, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "ProjectListActivity: api call success : ${responseBody.toString()}")
                    projects = responseBody
                    projectAdapter.setData(projects)
                    binding.projects.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    binding.projects.adapter = projectAdapter
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "ProjectListActivity: api call fail : $responseBody")
                }
            }
        })


    }

    private fun loadData90() {//신규 달성률 90% 이상 데이터
        projects = mutableListOf()
        RetrofitManager.instance.showConditionedProjects(auth = API.HEADER_TOKEN, type = "achieve", completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "ProjectListActivity: api call success : ${responseBody.toString()}")
                    projects = responseBody
                    projectAdapter.setData(projects)
                    binding.projects.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    binding.projects.adapter = projectAdapter
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "ProjectListActivity: api call fail : $responseBody")
                }
            }
        })
    }

    private fun loadDataDeadLine() {//신규 마감임박 데이터
        projects = mutableListOf()
        RetrofitManager.instance.showConditionedProjects(auth = API.HEADER_TOKEN, type = "almost", completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "ProjectListActivity: api call success : ${responseBody.toString()}")
                    projects = responseBody
                    projectAdapter.setData(projects)
                    binding.projects.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    binding.projects.adapter = projectAdapter
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "ProjectListActivity: api call fail : $responseBody")
                }
            }
        })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}