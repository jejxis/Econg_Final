package oasis.team.econg.econg.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import oasis.team.econg.econg.data.Reward
import oasis.team.econg.econg.databinding.DialogFundBinding

class FundDialog(context: Context, rewards: MutableList<Reward>) : DialogFragment(){
    private var _binding: DialogFundBinding? = null
    private val binding get() = _binding!!
    private val rewards = rewards

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogFundBinding.inflate(inflater, container, false)
        val view = binding.root

        /// 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.close.setOnClickListener {
            dismiss()
        }

        for(i in 0 until rewards.size){
            data class Reward(
                val rewardId: Long,
                val name: String,
                val price: Int,
                val stock: Int,
                val soldQuantity: Int,
                val combination: String,
                val projectId: Long
            )
            var myRadio = RadioButton(context)
            myRadio.text = "${rewards[i].name} : ${rewards[i].price}원"
            myRadio.id = rewards[i].rewardId.toInt()
            var rprms = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
            binding.combination.addView(myRadio, rprms)
        }


        binding.btnPayment.setOnClickListener {
            val id = binding.combination.checkedRadioButtonId
            Log.d("MY", "리워드${id} 선택")
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface Confirm
}