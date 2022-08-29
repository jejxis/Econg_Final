package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.econg.data.Project
import oasis.team.econg.econg.databinding.ItemVerBinding
import oasis.team.econg.forui.rvAdapter.ProjectAdapter

class ProjectVerAdapter(val context: Context?): RecyclerView.Adapter<ProjectVerAdapter.HomePopularHolder>() {
    var listData = mutableListOf<Project>()//어댑터에서 사용할 목록변수
    var listener: ProjectAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectVerAdapter.HomePopularHolder {//한 화면에 생성할 레이아웃 개수 = 한 화면에 생성할 아이템 개수-> 아이템 레이아웃 생성
        //context = parent.context
        val binding = ItemVerBinding.inflate(LayoutInflater.from(context), parent, false)//여기서 만들어 놓은 item_recycler를 붙이는 건가? 그런듯.
        return HomePopularHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectVerAdapter.HomePopularHolder, position: Int) {//아이템 레이아웃에 값 입력 후 출력 -> 생성된 뷰 홀더를 보여줌.
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.id.toString())
        }

    }

    override fun getItemCount(): Int {// 목록에 나오는 아이템 개수
        return listData.size
    }

    fun setData(arrData : MutableList<Project>?){
        listData = arrData as ArrayList<Project>
    }

    fun setClickListener(listener1: ProjectAdapter.OnItemClickListener){
        listener = listener1
    }

    inner class HomePopularHolder(val binding: ItemVerBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: Project) {
            binding.imgProject.setImageResource(data.img)
            binding.projectCategory.text = "${data.category}"
            binding.projectCompany.text = "${data.company}"
            binding.projectName.text = "${data.projectName}\n${data.projectInfo}"
            binding.achRate.text = data.achRate.toString()
        }

    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}