package oasis.team.econg.econg.utils

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
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

fun NestedScrollView.scrollToView(view: View) {
    val y = computeDistanceToView(view) - 100
    this.scrollTo(0, y)
}
fun NestedScrollView.computeDistanceToView(view: View): Int {
    return Math.abs(calculateRectOnScreen(this).top - (this.scrollY + calculateRectOnScreen(view).top))
}

fun calculateRectOnScreen(view: View): Rect {
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    return Rect(
        location[0],
        location[1],
        location[0] + view.measuredWidth,
        location[1] + view.measuredHeight
    )
}
