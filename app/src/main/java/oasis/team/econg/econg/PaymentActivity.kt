package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.econg.data.OrderConfirmation
import oasis.team.econg.econg.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    val binding by lazy{ActivityPaymentBinding.inflate(layoutInflater)}
    var rewardID = ""
    private lateinit var orderInfo: OrderConfirmation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("rewardID")){
            rewardID = intent.getStringExtra("rewardID").toString()
        }

        loadOrderInfo()
        setOrderInfo()

        binding.goToKakaoPay.setOnClickListener {
            //use orderInfo.rewardId, orderInfo.rewardName, orderInfo.price, binding.deliveryAddress.text.toString()
        }
    }

    private fun  loadOrderInfo(){
        orderInfo = OrderConfirmation(
            id = rewardID.toLong(),
            combination = "리워드 조합",
            price = 50000,
            projectId = 5,
            rewardId = rewardID.toInt(),
            rewardName = "리워드명",
            thumbnail = "썸네일",
            title = "프로젝트명",
            orderStatus = 0
        )
    }

    private fun setOrderInfo(){
        binding.combination.text = orderInfo.combination
        binding.price.text = orderInfo.price.toString()
        binding.rewardName.text = orderInfo.rewardName
        binding.title.text = orderInfo.title
    }
}