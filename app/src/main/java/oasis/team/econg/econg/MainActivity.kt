package oasis.team.econg.econg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

        setSupportActionBar(binding.mainToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getNavi()
    }

    private fun getNavi(){

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

                R.id.item_my -> {
                    showMyFragment()
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false

            }
        }
    }
    private fun showHomeFragment(){
        binding.mainText.visibility = View.GONE
        binding.mainLogo.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())//
            .commitAllowingStateLoss()
    }

    private fun showFavoriteFragment(){
        binding.mainText.text = "찜한 프로젝트"
        binding.mainText.visibility = View.VISIBLE
        binding.mainLogo.visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, FavoriteFragment())//
            .commitAllowingStateLoss()
    }
    private fun showMyFragment(){
        binding.mainText.text = "마이페이지"
        binding.mainText.visibility = View.VISIBLE
        binding.mainLogo.visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, MyFragment())//
            .commitAllowingStateLoss()
    }
}