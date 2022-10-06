package oasis.team.econg.econg

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.OrderBeforePay
import oasis.team.econg.econg.data.OrderConfirmation
import oasis.team.econg.econg.databinding.ActivityPaymentBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.econg.utils.loadImageSetView
//API
class PaymentActivity : AppCompatActivity() {
    val binding by lazy{ActivityPaymentBinding.inflate(layoutInflater)}
    var rewardID = ""
    var projectID = ""
    val storage = Firebase.storage(ECONG_URL)
    private var orderInfo: OrderBeforePay? = null
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
        RetrofitManager.instance.showProjectOrder(auth = API.HEADER_TOKEN, rewardId = rewardID.toLong(), completion = {
            responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY ->{
                        orderInfo = responseBody
                        Log.d(TAG, "PaymentActivity: api call success: ${orderInfo.toString()} ")
                        setOrderInfo()

                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "Payment: api call fail : $responseBody")
                        Toast.makeText(this@PaymentActivity, "데이터 로딩에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

        })
    }

    private fun setOrderInfo(){
        binding.combination.text = orderInfo!!.combination
        binding.price.text = orderInfo!!.price.toString()
        binding.rewardName.text = orderInfo!!.rewardName
        binding.title.text = orderInfo!!.title
        storage.loadImageSetView(orderInfo!!.thumbnail, binding.thumbnail)
    }
}