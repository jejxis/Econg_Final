package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.econg.data.Company
import oasis.team.econg.econg.databinding.ItemCompanyVerBinding

//data class Company(val id: Int, val rank: Int?,val img: Int, val category: String, val companyName: String, val companyInfo: String)
class CompanyVerAdapter (val context: Context?) : RecyclerView.Adapter<CompanyVerAdapter.CompanyVerHolder>(){
    var listData = mutableListOf<Company>()//어댑터에서 사용할 목록변수
    var listener: CompanyVerAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyVerAdapter.CompanyVerHolder {//한 화면에 생성할 레이아웃 개수 = 한 화면에 생성할 아이템 개수-> 아이템 레이아웃 생성
        //context = parent.context
        val binding = ItemCompanyVerBinding.inflate(LayoutInflater.from(context), parent, false)//여기서 만들어 놓은 item_recycler를 붙이는 건가? 그런듯.
        return CompanyVerHolder(binding)
    }

    override fun onBindViewHolder(holder: CompanyVerAdapter.CompanyVerHolder, position: Int) {//아이템 레이아웃에 값 입력 후 출력 -> 생성된 뷰 홀더를 보여줌.
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.id.toString())
        }

    }

    override fun getItemCount(): Int {// 목록에 나오는 아이템 개수
        return listData.size
    }

    fun setData(arrData : MutableList<Company>?){
        listData = arrData as ArrayList<Company>
    }

    fun setClickListener(listener1: CompanyVerAdapter.OnItemClickListener){
        listener = listener1
    }

    inner class CompanyVerHolder(val binding: ItemCompanyVerBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: Company) {
            binding.imgCompany.setImageResource(data.img)
            binding.companyCategory.text = "${data.category}"
            binding.companyName.text = "${data.companyName}"
            binding.companyInfo.text = "${data.companyInfo}"
        }

    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}