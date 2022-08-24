package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.econg.databinding.ActivityMyCommunityBinding

class MyCommunityActivity : AppCompatActivity() {
    val binding by lazy{ActivityMyCommunityBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}