package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import oasis.team.econg.econg.databinding.ActivityMainBinding
import oasis.team.econg.econg.menuFragments.FavoriteFragment
import oasis.team.econg.econg.menuFragments.HomeFragment
import oasis.team.econg.econg.menuFragments.MyFragment
import oasis.team.econg.econg.utils.Constants.TAG

class MainActivity : AppCompatActivity() {
    val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    var whichFragment = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent
        if(intent.hasExtra("Fragment")){
            whichFragment = intent.getStringExtra("Fragment").toString()
            if(whichFragment.equals("MyFragment")){
                binding.bNavi.selectedItemId  = R.id.item_my
                showMyFragment()
                Log.d(TAG, "onCreate: Have to show myfragment")
            }
        }else{
            showHomeFragment()
        }

        getNavi()
    }

    private fun getNavi(){
        /*supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())//
            .commitAllowingStateLoss()*/

        binding.bNavi.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.item_home -> {
                    showHomeFragment()
                    return@setOnItemSelectedListener true
                }

                R.id.item_favorite -> {
                    showFavoriteFragment()
                    return@setOnItemSelectedListener true
                }

                R.id.item_notice -> {
                    return@setOnItemSelectedListener true
                }

                R.id.item_my -> {
                    showMyFragment()
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false

            }
        }
    }
    private fun showHomeFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())//
            .commitAllowingStateLoss()
    }

    private fun showFavoriteFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, FavoriteFragment())//
            .commitAllowingStateLoss()
    }
    private fun showMyFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, MyFragment())//
            .commitAllowingStateLoss()
    }
}