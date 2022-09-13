package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.econg.data.User
import oasis.team.econg.econg.databinding.ItemUserVerBinding

class UserFollowAdapter(val context: Context?) : RecyclerView.Adapter<UserFollowAdapter.UserFollowHolder>() {
    var listData = mutableListOf<User>()
    var itemListener: UserFollowAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowHolder {
        val binding = oasis.team.econg.econg.databinding.ItemUserVerBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserFollowHolder(binding)
    }

    override fun onBindViewHolder(holder: UserFollowHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            itemListener!!.onClicked(data.id.toString())
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
            binding.imgProfile.setImageResource(data.img)
            binding.userName.text = data.companyName

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