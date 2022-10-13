package oasis.team.econg.econg

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.OrderBeforePay
import oasis.team.econg.econg.data.OrderForPay
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
    var kakaopayLink: String? = ""
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

        setSupportActionBar(binding.paymentToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.goToMyPage.setOnClickListener {
            val intent = Intent(this@PaymentActivity, MainActivity::class.java)
            intent.putExtra("Fragment", "MyFragment")
            startActivity(intent)
            finish()
        }

        binding.goToKakaoPay.setOnClickListener {
            val orderForPay = OrderForPay(
                projectId = orderInfo!!.projectId,
                rewardId = orderInfo!!.rewardId,
                rewardName = orderInfo!!.rewardName,
                price = orderInfo!!.price,
                deliveryAddress = binding.deliveryAddress.text.toString()
            )
            RetrofitManager.instance.payOrder(auth = API.HEADER_TOKEN, param = orderForPay, completion = {
                responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY ->{
                        kakaopayLink = responseBody
                        Log.d(TAG, "kakaopayLink: $kakaopayLink")
                        binding.goToKakaoPay.visibility = View.GONE
                        binding.goToMyPage.visibility = View.VISIBLE
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaopayLink))
                        startActivity(intent)
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "Payment: api call fail : $responseBody")
                        Toast.makeText(this@PaymentActivity, "결제 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}