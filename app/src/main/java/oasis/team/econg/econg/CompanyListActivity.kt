package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.databinding.ActivityCompanyListBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.CompanyVerAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE

class CompanyListActivity : AppCompatActivity() {
    val binding by lazy{ActivityCompanyListBinding.inflate(layoutInflater)}

    var companies: MutableList<User>? = mutableListOf()
    var companyAdapter = CompanyVerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadCompanyData()

        companyAdapter.setClickListener(onClickedCompanyItem)
    }

    private fun loadCompanyData(){

        RetrofitManager.instance.getRecentUsers(auth = API.HEADER_TOKEN, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "UserList: api call success : ${responseBody.toString()}")
                    companies = responseBody
                    companyAdapter.setData(companies)
                    binding.companies.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    binding.companies.adapter = companyAdapter
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this, "api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "UserList: api call fail : $responseBody")
                }
            }
        })
    }

    private val onClickedCompanyItem = object : CompanyVerAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@CompanyListActivity, DetailCompanyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}