package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.econg.data.Order
import oasis.team.econg.econg.databinding.ActivityDetailOrderBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants
import oasis.team.econg.econg.utils.RESPONSE_STATE

class DetailOrderActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailOrderBinding.inflate(layoutInflater)}
    var orderId = ""
    private var order:Order? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            orderId = intent.getStringExtra("id").toString()
        }
        loadOrderData(orderId)
    }

    private fun loadOrderData(id: String) {
        RetrofitManager.instance.getDetailOrderInfo(auth = API.HEADER_TOKEN, id.toLong(),completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY ->{
                    Log.d(Constants.TAG, "loadData: MyOrderList: api call Success: ${responseBody.toString()}")
                    order = responseBody
                    setScreen()
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(Constants.TAG, "loadData: MyOrderList: api call fail : $responseBody")
                }
            }
        })

    }

    private fun setScreen(){
        binding.btnGoToProject.setOnClickListener {
            var intent = Intent(this@DetailOrderActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", order!!.projectId.toString())
            startActivity(intent)
        }
        if(order!!.orderStatus == 0) binding.orderStatus.text = "결제실패"
        else if(order!!.orderStatus == 1) binding.orderStatus.text = "결제완료"
        else binding.orderStatus.text = "실패"

        binding.userName.text = order!!.title
        binding.rewardName.text = order!!.rewardName

        binding.rewardName2.text = order!!.rewardName
        binding.combination.text = order!!.combination
        binding.price.text = order!!.price.toString()

        binding.payPrice.text = "${order!!.price}원"

        binding.deliveryAddress.text = order!!.deliveryAddress
    }
}