package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.MyCommunity
import oasis.team.econg.econg.databinding.ItemCommunityBinding
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.loadImageSetView

// data class Reply(val project: Project, val community: Community)
// data class Community(val comment: String, val projectId: Long, val userid: Long) -> 나중에 API가 어떤 데이터 값을 내뱉는지 봐야할 듯.
class MyCommunityAdapter(val context: Context?): RecyclerView.Adapter<MyCommunityAdapter.CommunityHolder>(){
    var listData = mutableListOf<MyCommunity>()//어댑터에서 사용할 목록변수
    var listener: MyCommunityAdapter.OnItemClickListener? = null
    private val storage = Firebase.storage(ECONG_URL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommunityAdapter.CommunityHolder {//한 화면에 생성할 레이아웃 개수 = 한 화면에 생성할 아이템 개수-> 아이템 레이아웃 생성
        //context = parent.context
        val binding = ItemCommunityBinding.inflate(LayoutInflater.from(context), parent, false)//여기서 만들어 놓은 item_recycler를 붙이는 건가? 그런듯.
        return CommunityHolder(binding)
    }override fun onBindViewHolder(holder: MyCommunityAdapter.CommunityHolder, position: Int) {//아이템 레이아웃에 값 입력 후 출력 -> 생성된 뷰 홀더를 보여줌.
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            Log.d("MY", "CommunityOnBindViewHolder: ${data.projectId}")
            listener!!.onClicked(data.projectId.toString())
        }

    }
    override fun getItemCount(): Int {// 목록에 나오는 아이템 개수
        return listData.size
    }
    fun setData(arrData : MutableList<MyCommunity>?){
        listData = arrData as ArrayList<MyCommunity>
    }

    fun setClickListener(listener1: MyCommunityAdapter.OnItemClickListener){
        listener = listener1
    }

    inner class CommunityHolder(val binding: ItemCommunityBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: MyCommunity) {
            storage.loadImageSetView(data.userProfileUrl, binding.imgProject)
            binding.projectCompany.text = "${data.userName}"
            binding.projectName.text = "${data.projectName}"
            binding.date.text = data.updatedAt.toString()
            binding.community.text = data.content//->new!
        }

    }
    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}
