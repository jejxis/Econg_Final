package oasis.team.econg.econg.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

/*
//chek string json
fun String?.isJsonObject():Boolean {
    if(this?.startsWith("{") == true && this?.endsWith("}")){
        return true
    }else{
        return false
    }
}

//check string json array
fun String?.isJsonArray():Boolean {
    if(this?.startsWith("[") == true && this?.endsWith("]")){
        return true
    }else{
        return false
    }
}*/

fun FirebaseStorage.loadImageSetView(imageUrl: String,view: ImageView){
    this.getReferenceFromUrl(imageUrl).downloadUrl.addOnSuccessListener { uri ->
        Glide.with(view).load(uri).into(view)
    }.addOnFailureListener {
        Log.e("STORAGE", "DOWNLOAD_ERROR=>${it.message}")
    }
}

        /*storage.getReferenceFromUrl("gs://econg-7e3f6.appspot.com/bud.png").downloadUrl.addOnSuccessListener { uri ->
                Glide.with(binding.imgProject).load(uri).into(binding.imgProject)
            }.addOnFailureListener {
                Log.e("STORAGE", "DOWNLOAD_ERROR=>${it.message}")
            }*/
