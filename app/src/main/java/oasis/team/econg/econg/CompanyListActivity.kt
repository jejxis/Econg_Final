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
                i,
                R.drawable.ic_baseline_favorite_border_pink_24,
                "카테고리$i",
                "기업$i",
                "기업${i}입니다.")
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