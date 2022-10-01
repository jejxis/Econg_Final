package oasis.team.econg.econg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import oasis.team.econg.econg.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSU.setOnClickListener {
            val email = binding.suEmail.text.toString().trim()
            val password = binding.suPasswd.text.toString().trim()
            val phone = binding.suPhone.text.toString().trim()
            val userName = binding.suName.text.toString().trim()


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

            if (phone.isEmpty() || !Pattern.matches(
                    "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}\$",
                    phone
                )
            ) {
                binding.suPhone.error = "Check the Phone Number\n01000000000"
                binding.suPhone.requestFocus()
                return@setOnClickListener
            }

            if (userName.isEmpty()) {
                binding.suName.error = "UserName required"
                binding.suName.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}