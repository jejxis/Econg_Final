package oasis.team.econg.econg.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import oasis.team.econg.econg.EditProjectActivity
import oasis.team.econg.econg.OpenProjectActivity
import oasis.team.econg.econg.R
import oasis.team.econg.econg.databinding.FragmentDatePickerBinding
import oasis.team.econg.econg.imageSlide.ImageSlideFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener  {
    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!
    var op: OpenProjectActivity? = null
    var ep: EditProjectActivity? = null
    val KEY = "KEY"

    fun newInstance(data: Int) = DatePickerFragment().apply {
        arguments = Bundle().apply {
            putInt(KEY, data)
        }
    }

    val flag by lazy { requireArguments().getInt(KEY) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context.javaClass == OpenProjectActivity::class.java){
            op = context as OpenProjectActivity
        }else if(context.javaClass == EditProjectActivity::class.java){
            ep = context as EditProjectActivity
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDatePickerBinding.inflate(inflater,container,false)
        val view = binding.root


        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)

        return if(op == null){
            DatePickerDialog(ep!!, this, year, month, day)
        } else{
            DatePickerDialog(op!!, this, year, month, day)
        }

    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {

        if(op == null){
            if(flag == 0) ep!!.processDatePickerOpenResult(year,month,day)
            else if(flag == 1) ep!!.processDatePickerCloseResult(year,month,day)
        } else{
            if(flag == 0) op!!.processDatePickerOpenResult(year,month,day)
            else if(flag == 1) op!!.processDatePickerCloseResult(year,month,day)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}