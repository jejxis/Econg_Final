package oasis.team.econg.econg.imageSlide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.R
import oasis.team.econg.econg.databinding.FragmentImageSlideBinding
import oasis.team.econg.econg.utils.Constants.ECONG_URL

class ImageSlideFragment(/*val image: Int*/) : Fragment() {

    private lateinit var binding: FragmentImageSlideBinding
    private val storage = Firebase.storage("gs://econg-7e3f6.appspot.com/")
    private val key = "KEY"
    fun newInstance(data: String) = ImageSlideFragment().apply {
        arguments = Bundle().apply {
            putString(key, data)
        }
    }

    val image by lazy { requireArguments().getString(key) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageSlideBinding.inflate(inflater,container, false)
        //Log.d("MY", image!!)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MY", image!!)
        storage.getReferenceFromUrl(image!!).downloadUrl.addOnSuccessListener { uri ->
            Glide.with(binding.imgSlideImage).load(uri).into(binding.imgSlideImage)
        }.addOnFailureListener {
            Log.e("STORAGE", "DOWNLOAD_ERROR=>${it.message}")
        }
    }
}