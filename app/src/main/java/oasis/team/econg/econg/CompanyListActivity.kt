package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.databinding.ActivityCompanyListBinding
import oasis.team.econg.econg.rvAdapter.CompanyVerAdapter

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
        for(i: Int in 1..10){
            companies!!.add(User(
                i.toLong(),
                "사용자$i",
                null,
                "gs://econg-7e3f6.appspot.com/bud.png",
                true)
            )
        }
        companyAdapter.setData(companies)
        binding.companies.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
        binding.companies.adapter = companyAdapter
    }

    private val onClickedCompanyItem = object : CompanyVerAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@CompanyListActivity, DetailCompanyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}