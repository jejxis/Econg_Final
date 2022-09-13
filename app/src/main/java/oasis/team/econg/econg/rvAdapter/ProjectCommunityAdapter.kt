package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.econg.data.ProjectReply
import oasis.team.econg.econg.databinding.ItemProjectCommunityBinding

class ProjectCommunityAdapter(val context: Context?): RecyclerView.Adapter<ProjectCommunityAdapter.ProjectCommunityHolder>() {
    var listData = mutableListOf<ProjectReply>()
    var listener: ProjectCommunityAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectCommunityHolder {
        val binding = ItemProjectCommunityBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProjectCommunityHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectCommunityHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.community.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<ProjectReply>?){
        listData = arrData as ArrayList<ProjectReply>
    }

    fun setClickListener(listener1: ProjectCommunityAdapter.OnItemClickListener){
        listener = listener1
    }

    inner class ProjectCommunityHolder(val binding: ItemProjectCommunityBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: ProjectReply){
            binding.communityProfile.setImageResource(data.user.img)
            binding.communityUserName.text = data.user.companyName
            binding.communityContent.text = data.community.comment
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}