package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.databinding.ItemCompanyHorBinding
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.loadImageSetView

//data class Company(val id: Int, val rank: Int?,val img: Int, val category: String, val companyName: String, val companyInfo: String)
class CompanyHorAdapter(val context: Context?) : RecyclerView.Adapter<CompanyHorAdapter.CompanyHorHolder>(){
    var listData = mutableListOf<User>()//어댑터에서 사용할 목록변수
    var listener: CompanyHorAdapter.OnItemClickListener? = null
    private val storage = Firebase.storage(ECONG_URL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyHorAdapter.CompanyHorHolder {//한 화면에 생성할 레이아웃 개수 = 한 화면에 생성할 아이템 개수-> 아이템 레이아웃 생성
        //context = parent.context
        val binding = ItemCompanyHorBinding.inflate(LayoutInflater.from(context), parent, false)//여기서 만들어 놓은 item_recycler를 붙이는 건가? 그런듯.
        return CompanyHorHolder(binding)
    }

    override fun onBindViewHolder(holder: CompanyHorAdapter.CompanyHorHolder, position: Int) {//아이템 레이아웃에 값 입력 후 출력 -> 생성된 뷰 홀더를 보여줌.
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.userId.toString())
        }

    }

    override fun getItemCount(): Int {// 목록에 나오는 아이템 개수
        return listData.size
    }

    fun setData(arrData : MutableList<User>?){
        listData = arrData as ArrayList<User>
    }

    fun setClickListener(listener1: CompanyHorAdapter.OnItemClickListener){
        listener = listener1
    }

    inner class CompanyHorHolder(val binding: ItemCompanyHorBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: User) {
            storage.loadImageSetView(data.profileUrl, binding.imgCompany)
            binding.companyName.text = "${data.nickName}"
            binding.companyInfo.text = "${data.description}"
            if(data.authenticate)
                binding.auth.visibility = View.VISIBLE
        }

    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}