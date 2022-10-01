package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import oasis.team.econg.econg.databinding.ActivitySignInBinding

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

            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK//액티비티 스택제거
        }

        binding.btnGoToSU.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}