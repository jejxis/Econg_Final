package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import oasis.team.econg.econg.data.Login
import oasis.team.econg.econg.data.PostLogin
import oasis.team.econg.econg.databinding.ActivitySignInBinding
import oasis.team.econg.econg.interfaceModel.APIS
import oasis.team.econg.econg.samplePreference.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignInActivity : AppCompatActivity() {
    val binding by lazy { ActivitySignInBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGoToHome.setOnClickListener {
            val email = binding.suEmail.text.toString().trim()
            val password = binding.suPasswd.text.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.suEmail.error = "Check the Email"
                binding.suEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.suPasswd.error = "Password required"
                binding.suPasswd.requestFocus()
                return@setOnClickListener
            }

            val retrofit = Retrofit.Builder()
                .baseUrl("https://isileeserver.shop")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val api = retrofit.create(APIS::class.java)

            val data = PostLogin(binding.suEmail.text.toString(),binding.suPasswd.text.toString())

            api.postLogin(data).enqueue(object : Callback<Login> {

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())

                    MyApplication.prefs.token = response.body()?.result?.token.toString()
                    Log.d("log", MyApplication.prefs.token!!)

                    if(response.body()?.result?.token.toString() == "null"){
                        Toast.makeText(this@SignInActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK//액티비티 스택제거
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    // 실패
                    Log.d("log",t.message.toString())
                    Log.d("log","fail")
                }

            })
        }

        binding.btnGoToSU.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}