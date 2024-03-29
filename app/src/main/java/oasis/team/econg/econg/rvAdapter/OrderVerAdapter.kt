package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.OrderConfirmation
import oasis.team.econg.econg.databinding.ItemOrderVerBinding
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.loadImageSetView

class OrderVerAdapter(val context: Context?): RecyclerView.Adapter<OrderVerAdapter.OrderHolder>() {
    var listData = mutableListOf<OrderConfirmation>()
    private var listener: OrderVerAdapter.OnItemClickListener?=null
    private var detailOrderListener: BtnDetailOrderListener?=null
    private val storage = Firebase.storage(Constants.ECONG_URL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val binding = ItemOrderVerBinding.inflate(LayoutInflater.from(context), parent, false)
        return OrderHolder(binding, detailOrderListener!!)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(data.projectId.toString())
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<OrderConfirmation>?){
        listData = arrData as ArrayList<OrderConfirmation>
    }

    fun setClickListener(l: OrderVerAdapter.OnItemClickListener){
        listener = l
    }

    fun setDetailOrderListener(li: BtnDetailOrderListener){
        detailOrderListener = li
    }

    inner class OrderHolder(val binding: ItemOrderVerBinding, var listener: OrderVerAdapter.BtnDetailOrderListener): RecyclerView.ViewHolder(binding.root){
        fun setData(data: OrderConfirmation){
            storage.loadImageSetView(data.thumbnail, binding.projectImage)
            if(data.orderStatus == 0) binding.projectState.text = "성공"
            else if(data.orderStatus == 1) binding.projectState.text = "진행중"
            else if (data.orderStatus==2) binding.projectState.text = "실패"

            binding.projectName.text = data.title
            binding.rewardName.text = data.rewardName
            binding.btnOrderDetail.setOnClickListener {
                listener!!.goToDetailOrder(data.orderId.toString())
            }
        }


    }
    interface BtnDetailOrderListener{
        fun goToDetailOrder(id: String)
    }

    interface OnItemClickListener{
        fun onClicked(id: String)
    }
}
