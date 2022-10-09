package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Order
import oasis.team.econg.econg.data.OrderConfirmation
import oasis.team.econg.econg.databinding.ActivityOrderListBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.OrderVerAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE

class OrderListActivity : AppCompatActivity() {
    val binding by lazy{ActivityOrderListBinding.inflate(layoutInflater)}

    var orders: MutableList<OrderConfirmation>? = mutableListOf()
    var orderAdapter = OrderVerAdapter(this)

    var projectId:Long = 0
    var orderId:Long =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        orderAdapter.setClickListener(onClickedOrderItem)
        orderAdapter.setDetailOrderListener(onClickedDetailOrder)

        loadOrders()
    }

    private fun loadOrders() {
        RetrofitManager.instance.getOrderedProjects(auth = API.HEADER_TOKEN, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    Log.d(Constants.TAG, "loadData: MyOrderList: api call Success: ${responseBody.toString()}")
                    orders = responseBody
                    orderAdapter.setData(orders)
                    binding.rvOrder.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    binding.rvOrder.adapter = orderAdapter
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(Constants.TAG, "loadData: MyOrderList: api call fail : $responseBody")
                }
            }
        })


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