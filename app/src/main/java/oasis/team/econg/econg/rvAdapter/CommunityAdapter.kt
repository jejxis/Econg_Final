package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.econg.data.Community
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.data.Reply
import oasis.team.econg.econg.databinding.ItemCommunityBinding
import oasis.team.econg.econg.databinding.ItemVerBinding

// data class Reply(val project: Project, val community: Community)
// data class Community(val comment: String, val projectId: Long, val userid: Long) -> 나중에 API가 어떤 데이터 값을 내뱉는지 봐야할 듯.
class CommunityAdapter(val context: Context?): RecyclerView.Adapter<CommunityAdapter.CommunityHolder>(){
    var listData = mutableListOf<Reply>()//어댑터에서 사용할 목록변수
    var listener: CommunityAdapter.OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.CommunityHolder {//한 화면에 생성할 레이아웃 개수 = 한 화면에 생성할 아이템 개수-> 아이템 레이아웃 생성
        //context = parent.context
        val binding = ItemCommunityBinding.inflate(LayoutInflater.from(context), parent, false)//여기서 만들어 놓은 item_recycler를 붙이는 건가? 그런듯.
        return CommunityHolder(binding)
    }override fun onBindViewHolder(holder: CommunityAdapter.CommunityHolder, position: Int) {//아이템 레이아웃에 값 입력 후 출력 -> 생성된 뷰 홀더를 보여줌.
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            Log.d("MY", "CommunityOnBindViewHolder: ${data.project.id}")
            listener!!.onClicked(data.project.id.toString())
        }

    }
    override fun getItemCount(): Int {// 목록에 나오는 아이템 개수
        return listData.size
    }
    fun setData(arrData : MutableList<Reply>?){
        listData = arrData as ArrayList<Reply>
    }

    fun setClickListener(listener1: CommunityAdapter.OnItemClickListener){
        listener = listener1
    }

    inner class CommunityHolder(val binding: ItemCommunityBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: Reply) {
            binding.imgProject.setImageResource(data.project.img)
            binding.projectCompany.text = "${data.project.company}"
            binding.projectName.text = "${data.project.projectName}"
            binding.achRate.text = data.project.achRate.toString()
            binding.community.text = data.community.comment//->new!
        }

    }
    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}
