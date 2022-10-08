package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.MyCommunity
import oasis.team.econg.econg.databinding.ActivityMyCommunityBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.MyCommunityAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE
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

        RetrofitManager.instance.getMyCommunites(auth = API.HEADER_TOKEN, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    Log.d(Constants.TAG, "loadData: MyCommunityList: api call Success: ${responseBody.toString()}")
                    myCommunity = responseBody

                    communityAdapter.setData(myCommunity)
                    binding.communityList.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    binding.communityList.adapter = communityAdapter
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(Constants.TAG, "loadData: MyCommunityList: api call fail : $responseBody")
                }
            }
        })

    }
}