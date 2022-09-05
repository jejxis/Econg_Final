package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.econg.data.PreReward
import oasis.team.econg.econg.data.Reward
import oasis.team.econg.econg.databinding.LayoutRewardBinding

class RewardAdapter(val context: Context?): RecyclerView.Adapter<RewardAdapter.RewardHolder>() {
    var listData = mutableListOf<PreReward>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardHolder {
        val binding = LayoutRewardBinding.inflate(LayoutInflater.from(context), parent, false)
        return RewardHolder(binding)
    }

    override fun onBindViewHolder(holder: RewardHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data, position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<PreReward>?){
        listData = arrData as ArrayList<PreReward>
    }

    inner class RewardHolder(val binding: LayoutRewardBinding): RecyclerView.ViewHolder(binding.root){
        var reward = PreReward("", 0, "")
        var pos = -1

        private val textWatcherReward = object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(p0 != null && !p0.toString().equals("")){
                    reward = PreReward(
                        binding.rewardName.text?.toString(),
                        Integer.parseInt(binding.rewardPrice.text?.toString()),
                        binding.rewardCombination.text?.toString()
                    )

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }


        fun setData(data: PreReward, position: Int){
            pos = position
            binding.btnRemoveReward.setOnClickListener {
                listData.removeAt(position)
                this@RewardAdapter.notifyItemRemoved(position)
            }
            binding.rewardName.addTextChangedListener(textWatcherReward)
            binding.rewardPrice.addTextChangedListener(textWatcherReward)
            binding.rewardCombination.addTextChangedListener(textWatcherReward)
        }
    }
}