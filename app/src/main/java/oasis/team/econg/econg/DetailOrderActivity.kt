package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.econg.data.Order
import oasis.team.econg.econg.databinding.ActivityDetailOrderBinding

class DetailOrderActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailOrderBinding.inflate(layoutInflater)}
    var orderId = ""
    private var order:Order? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            //binding.productName.text = intent.getStringExtra("id")
            orderId = intent.getStringExtra("id").toString()
        }
        order = loadOrderData(orderId)
        setScreen()
    }

    private fun loadOrderData(id: String): Order {
        return Order(
            id = id.toLong(),
            price = 5000,
            orderStatus = 0,
            userId = id.toLong(),
            rewardId = id.toLong(),
            projectId = id.toInt(),
            deliveryAddress = "대한민국 어딘가",
            rewardName = "리워드 $id",
            thumbnail = "",
            title = "프로젝트 $id",
            combination = "연필 1자루 + 볼펜 1자루"
        )
    }

    private fun setScreen(){
        binding.btnGoToProject.setOnClickListener {
            var intent = Intent(this@DetailOrderActivity, DetailProjectActivity::class.java)
            intent.putExtra("id", order!!.projectId.toString())
            startActivity(intent)
        }
        if(order!!.orderStatus == 0) binding.orderStatus.text = "성공"
        else if(order!!.orderStatus == 1) binding.orderStatus.text = "진행중"
        binding.rewardName.text = order!!.rewardName

        binding.rewardName2.text = order!!.rewardName
        binding.combination.text = order!!.combination
        binding.price.text = order!!.price.toString()

        binding.payPrice.text = "${order!!.price}원"

        binding.deliveryAddress.text = order!!.deliveryAddress
    }
}