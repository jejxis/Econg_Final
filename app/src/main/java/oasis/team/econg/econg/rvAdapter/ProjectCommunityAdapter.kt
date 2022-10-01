package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.ProjectReply
import oasis.team.econg.econg.databinding.ItemProjectCommunityBinding
import oasis.team.econg.econg.dialog.DeleteCommunityDialog
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.loadImageSetView

class ProjectCommunityAdapter(val context: Context?,val id: String): RecyclerView.Adapter<ProjectCommunityAdapter.ProjectCommunityHolder>() {
    var listData = mutableListOf<ProjectReply>()
    var listener: ProjectCommunityAdapter.OnItemClickListener? = null
    var showConfirmDialog: OnDeleteCommunity? = null
    private val storage = Firebase.storage(ECONG_URL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectCommunityHolder {
        val binding = ItemProjectCommunityBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProjectCommunityHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectCommunityHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data, position)

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
        fun setData(data: ProjectReply, position: Int){
            if(id == data.user.userId.toString()){
                binding.editCommunity.visibility = View.VISIBLE
                binding.separate.visibility = View.VISIBLE
                binding.deleteCommunity.visibility = View.VISIBLE

                binding.deleteCommunity.setOnClickListener {
                    //
                }
            }
            storage.loadImageSetView(data.user.profileUrl, binding.communityProfile)
            binding.communityUserName.text = data.user.nickName
            binding.communityContent.text = data.community.comment
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }

    interface OnDeleteCommunity{
        fun selectYes()
    }
}