package oasis.team.econg.econg.detailProjectFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.econg.DetailProjectActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.databinding.FragmentDetailProjectStoryBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment


class DetailProjectStoryFragment : Fragment() {

    lateinit var binding: FragmentDetailProjectStoryBinding
    lateinit var detailProject: DetailProjectActivity

    private val STORY = "STORY"
    fun newInstance(data: String) = DetailProjectStoryFragment().apply {
        arguments = Bundle().apply {
            putString(STORY, data)
        }
    }

    private val story by lazy { requireArguments().getString(STORY) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        detailProject = context as DetailProjectActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailProjectStoryBinding.inflate(inflater,container, false)
        binding.story.text = story
        return binding.root
    }

}