package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.econg.databinding.ActivityDetailCompanyBinding

class DetailCompanyActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailCompanyBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}