package oasis.team.econg.econg.menuFavorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.DetailCompanyActivity
import oasis.team.econg.econg.MainActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.data.Company
import oasis.team.econg.econg.databinding.FragmentFavoriteBinding
import oasis.team.econg.econg.databinding.FragmentFavoriteCompanyBinding
import oasis.team.econg.econg.rvAdapter.CompanyHorAdapter
import oasis.team.econg.econg.rvAdapter.CompanyVerAdapter

class FavoriteCompanyFragment : Fragment() {

    lateinit var binding: FragmentFavoriteCompanyBinding
    lateinit var main: MainActivity

    var favCompany: MutableList<Company>? = mutableListOf()//좋아요한 기업 데이터
    lateinit var companyAdapter: CompanyVerAdapter//기업 어댑터

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteCompanyBinding.inflate(inflater,container, false)

        companyAdapter = CompanyVerAdapter(main)
        companyAdapter.setClickListener(onClickedCompanyItem)

        loadFavCompany()

        return binding.root
    }

    private fun loadFavCompany(){//팔로우 기업 데이터
        favCompany = mutableListOf()
        for(i: Int in 1..5){
            favCompany!!.add(Company(
                i,
                R.drawable.ic_baseline_category_24,
                "카테고리$i",
                "기업$i",
                "기업${i}입니다."))
        }
        companyAdapter.setData(favCompany)
        binding.favCompany.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
        binding.favCompany.adapter = companyAdapter
    }

    //신규 기업 클릭 이벤트 처리 -> 기업 상세 화면으로 이동
    private val onClickedCompanyItem = object : CompanyVerAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(context, DetailCompanyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

}