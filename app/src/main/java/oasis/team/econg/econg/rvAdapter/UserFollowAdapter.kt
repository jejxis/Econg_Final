package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.databinding.ItemUserVerBinding
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.loadImageSetView

class UserFollowAdapter(val context: Context?) : RecyclerView.Adapter<UserFollowAdapter.UserFollowHolder>() {
    var listData = mutableListOf<User>()
    var itemListener: UserFollowAdapter.OnItemClickListener? = null
    private val storage = Firebase.storage(Constants.ECONG_URL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowHolder {
        val binding = oasis.team.econg.econg.databinding.ItemUserVerBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserFollowHolder(binding)
    }

    override fun onBindViewHolder(holder: UserFollowHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            itemListener!!.onClicked(data.userId.toString())
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<User>?){
        listData = arrData as ArrayList<User>
    }

    fun setClickListener(listener1: UserFollowAdapter.OnItemClickListener){
        itemListener = listener1
    }

    inner class UserFollowHolder(val binding: ItemUserVerBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: User){
            storage.loadImageSetView(data.profileUrl, binding.imgProfile)
            binding.userName.text = data.nickName

            binding.btnFollow.setOnClickListener {
                binding.btnFollow.visibility = View.GONE
                binding.btnUnfollow.visibility = View.VISIBLE
            }

            binding.btnUnfollow.setOnClickListener {
                binding.btnUnfollow.visibility = View.GONE
                binding.btnFollow.visibility = View.VISIBLE
            }
        }
    }

    interface OnItemClickListener{
        fun onClicked(id: String)
    }

}