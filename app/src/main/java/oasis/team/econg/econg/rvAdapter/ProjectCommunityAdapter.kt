package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.ProjectCommunity
import oasis.team.econg.econg.databinding.ItemProjectCommunityBinding
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.loadImageSetView

class ProjectCommunityAdapter(val context: Context?,val id: String): RecyclerView.Adapter<ProjectCommunityAdapter.ProjectCommunityHolder>() {
    var listData = mutableListOf<ProjectCommunity>()
    var listener: ProjectCommunityAdapter.OnItemClickListener? = null
    private val storage = Firebase.storage(ECONG_URL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectCommunityHolder {
        val binding = ItemProjectCommunityBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProjectCommunityHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectCommunityHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data, position)

       /* holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.id.toString())
        }*/
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<ProjectCommunity>?){
        listData = arrData as ArrayList<ProjectCommunity>
    }

    /*fun setClickListener(listener1: ProjectCommunityAdapter.OnItemClickListener){
        listener = listener1
    }*/



    inner class ProjectCommunityHolder(val binding: ItemProjectCommunityBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: ProjectCommunity, position: Int){
            /*if(id == data.user.userId.toString()){
                binding.editCommunity.visibility = View.VISIBLE
                binding.separate.visibility = View.VISIBLE
                binding.deleteCommunity.visibility = View.VISIBLE

            }*/
            storage.loadImageSetView(data.userProfileUrl, binding.communityProfile)
            binding.communityUserName.text = data.userName
            binding.communityContent.text = data.content
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }

}