package oasis.team.econg.econg.detailProjectFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import oasis.team.econg.econg.DetailProjectActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.databinding.FragmentDetailProjectCommunityBinding


class DetailProjectCommunityFragment : Fragment() {

    lateinit var binding: FragmentDetailProjectCommunityBinding
    lateinit var detailProject: DetailProjectActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        detailProject = context as DetailProjectActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProjectCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }


}