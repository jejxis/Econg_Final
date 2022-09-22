package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.econg.databinding.ActivityEditProjectBinding

class EditProjectActivity : AppCompatActivity() {
    val binding by lazy{ActivityEditProjectBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}