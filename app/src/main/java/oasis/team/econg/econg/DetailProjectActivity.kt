package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import oasis.team.econg.econg.databinding.ActivityDetailProjectBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment

class DetailProjectActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailProjectBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //이미지 슬라이드
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.imgSlider.adapter = pagerAdapter
    }

    //이미지 슬라이드 어댑터
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> ImageSlideFragment(R.drawable.ic_baseline_favorite_24)
                1 -> ImageSlideFragment(R.drawable.ic_baseline_doorbell_24)
                else -> ImageSlideFragment(R.drawable.ic_baseline_category_24)
            }
        }
    }
}