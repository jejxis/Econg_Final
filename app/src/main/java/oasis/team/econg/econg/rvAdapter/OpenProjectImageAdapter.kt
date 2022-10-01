package oasis.team.econg.econg.rvAdapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oasis.team.econg.econg.databinding.ItemOpenProjectImageBinding

data class ImageData(val str: String, val uri: Uri?)
class OpenProjectImageAdapter(val context: Context?): RecyclerView.Adapter<OpenProjectImageAdapter.ImageHolder>() {
    var listData = mutableListOf<ImageData>()
    private var listener: BtnAddImageListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = ItemOpenProjectImageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageHolder(binding).apply {
            itemView.setOnClickListener {
                this.pos = adapterPosition
            }
        }
    }



    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val data = listData.get(position)
        holder.setData(data, position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(arrData: MutableList<ImageData>?){
        listData = arrData!!
    }

    fun returnData():MutableList<ImageData>{
        return listData
    }

    inner class ImageHolder(val binding: ItemOpenProjectImageBinding): RecyclerView.ViewHolder(binding.root){
        var uriString = ""
        var pos = -1

        fun setData(data: ImageData, position: Int){
            pos = position

            binding.imageUri.text = data.str

            binding.btnRemoveImage.setOnClickListener {
                if(listData.size == 1){
                    listData[0] = ImageData("", null)
                    this@OpenProjectImageAdapter.notifyItemChanged(0)
                }
                else{
                    listData.removeAt(position)
                    this@OpenProjectImageAdapter.notifyItemRemoved(position)
                }
            }

            binding.loadImage.setOnClickListener {
                listener!!.loadAndAddImage(position)
            }
        }
    }

    fun setBtnAddImageListener(li: BtnAddImageListener){
        listener = li
    }

    interface BtnAddImageListener{
        fun loadAndAddImage(position: Int)
    }
}