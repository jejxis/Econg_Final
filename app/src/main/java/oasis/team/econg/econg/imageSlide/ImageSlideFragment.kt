package oasis.team.econg.econg.imageSlide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.econg.R
import oasis.team.econg.econg.databinding.FragmentImageSlideBinding

class ImageSlideFragment(/*val image: Int*/) : Fragment() {

    lateinit var binding: FragmentImageSlideBinding
    val KEY = "KEY"
    fun newInstance(data: Int) = ImageSlideFragment().apply {
        arguments = Bundle().apply {
            putInt(KEY, data)
        }
    }
    /*companion object {
        const val KEY = "KEY"
        fun newInstance(data: Int) = ImageSlideFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY, data)
            }
        }
    }*/

    val image by lazy { requireArguments().getInt(KEY) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageSlideBinding.inflate(inflater,container, false)
        binding.imgSlideImage.setImageResource(image)

        return binding.root
    }

}