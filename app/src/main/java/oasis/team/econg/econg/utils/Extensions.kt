package oasis.team.econg.econg.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


//chek string json
fun String?.isJsonObject():Boolean {
    if(this == null) return false
    else return this!!.startsWith("{") == true && this!!.endsWith("}")
}

//check string json array
fun String?.isJsonArray():Boolean {
    if(this == null) return false
    else return this!!.startsWith("[") == true && this!!.endsWith("]")
}

fun FirebaseStorage.loadImageSetView(imageUrl: String,view: ImageView){
    this.getReferenceFromUrl(imageUrl).downloadUrl.addOnSuccessListener { uri ->
        Glide.with(view).load(uri).into(view)
    }.addOnFailureListener {
        Log.e("STORAGE", "DOWNLOAD_ERROR=>${it.message}")
    }
}
