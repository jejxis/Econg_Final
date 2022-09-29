package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Order
import oasis.team.econg.econg.data.OrderConfirmation
import oasis.team.econg.econg.databinding.ActivityOrderListBinding
import oasis.team.econg.econg.rvAdapter.OrderVerAdapter

class OrderListActivity : AppCompatActivity() {
    val binding by lazy{ActivityOrderListBinding.inflate(layoutInflater)}

    var orders: MutableList<OrderConfirmation>? = mutableListOf()
    var orderAdapter = OrderVerAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        orderAdapter.setClickListener(onClickedOrderItem)
        orderAdapter.setDetailOrderListener(onClickedDetailOrder)

        loadOrders()
    }

    private fun loadOrders() {
        orders = mutableListOf()
        for(i: Int in 1..20){
            orders!!.add(OrderConfirmation(
                id = i.toLong(),
                combination = "연필 ${i}자루 + 볼펜 ${i}자루",
                price = 5000,
                projectId = i,
                rewardId = i,
                rewardName = "리워드${i}",
                thumbnail = "",
                title = "프로젝트${i}",
                orderStatus = 1
            ))
        }

        orderAdapter.setData(orders)
        binding.rvOrder.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.rvOrder.adapter = orderAdapter
    }

    private val onClickedOrderItem = object : OrderVerAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@OrderListActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private val onClickedDetailOrder = object : OrderVerAdapter.BtnDetailOrderListener{
        override fun goToDetailOrder(id: String) {
            var intent = Intent(this@OrderListActivity, DetailOrderActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}