package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.econg.databinding.ActivityMainBinding
import oasis.team.econg.econg.menuFragments.HomeFragment
import oasis.team.econg.econg.menuFragments.MyFragment

class MainActivity : AppCompatActivity() {
    val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment(this@MainActivity))
            .commitAllowingStateLoss()
        getNavi()
    }

    private fun getNavi(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment(this@MainActivity))//
            .commitAllowingStateLoss()

        binding.bNavi.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.item_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, HomeFragment(this@MainActivity))//
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                /*R.id.item_category -> {
                    return@setOnItemSelectedListener true
                }*/

                R.id.item_favorite -> {
                    return@setOnItemSelectedListener true
                }

                R.id.item_notice -> {
                    return@setOnItemSelectedListener true
                }

                R.id.item_my -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, MyFragment(this@MainActivity))//
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false

            }
        }
    }


}