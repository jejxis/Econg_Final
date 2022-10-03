package oasis.team.econg.econg

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.OrderBeforePay
import oasis.team.econg.econg.data.OrderConfirmation
import oasis.team.econg.econg.databinding.ActivityPaymentBinding
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.loadImageSetView
//API
class PaymentActivity : AppCompatActivity() {
    val binding by lazy{ActivityPaymentBinding.inflate(layoutInflater)}
    var rewardID = ""
    var projectID = ""
    val storage = Firebase.storage(ECONG_URL)
    private lateinit var orderInfo: OrderBeforePay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("rewardID")){
            rewardID = intent.getStringExtra("rewardID").toString()
        }
        if(intent.hasExtra("projectID")){
            projectID = intent.getStringExtra("projectID").toString()
        }

        loadOrderInfo()
        setOrderInfo()

        binding.goToKakaoPay.setOnClickListener {
            //use orderInfo data & binding.deliveryAddress.text.toString()

            // call "/app/orders" API12

            //get link from API

            //move to the link
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.naver.com"))
            startActivity(intent)
        }
    }

    private fun  loadOrderInfo(){//Retrofit here API11
        orderInfo = OrderBeforePay(//->use in PaymentActivity
            projectId = projectID.toLong(),
            title =  "Project${projectID}",
            thumbnail = "gs://econg-7e3f6.appspot.com/bud.png",
            rewardId = rewardID.toLong(),
            rewardName = "Reward${rewardID}",
            price = 5000,
            combination = "Pencil and Ball pen"
        )
    }

    private fun setOrderInfo(){
        binding.combination.text = orderInfo.combination
        binding.price.text = orderInfo.price.toString()
        binding.rewardName.text = orderInfo.rewardName
        binding.title.text = orderInfo.title
        storage.loadImageSetView(orderInfo.thumbnail, binding.thumbnail)
    }
}