package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.MyCommunity
import oasis.team.econg.econg.databinding.ActivityMyCommunityBinding
import oasis.team.econg.econg.rvAdapter.MyCommunityAdapter
import java.time.LocalDateTime

class MyCommunityActivity : AppCompatActivity() {
    val binding by lazy{ActivityMyCommunityBinding.inflate(layoutInflater)}

    private var myCommunity: MutableList<MyCommunity>? = mutableListOf()
    private var communityAdapter = MyCommunityAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        communityAdapter.setClickListener(onClickedListItem)
    }

    private val onClickedListItem = object : MyCommunityAdapter.OnItemClickListener{
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
            myCommunity!!.add(
                MyCommunity(
                    id = i.toLong(),
                    content = "Content$i",
                    updatedAt = LocalDateTime.now(),
                    userProfileUrl = "gs://econg-7e3f6.appspot.com/bud.png",
                    userid = 1,
                    userName = "나잖아?",
                    projectId = i.toLong(),
                    projectName = "Project$i"
                )
            )
        }

        communityAdapter.setData(myCommunity)
        binding.communityList.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.communityList.adapter = communityAdapter
    }
}