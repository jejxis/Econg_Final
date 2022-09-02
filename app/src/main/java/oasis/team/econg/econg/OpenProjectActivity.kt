package oasis.team.econg.econg

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.databinding.ActivityOpenProjectBinding
import oasis.team.econg.econg.dialog.DatePickerFragment


/*ProjectDetail(//프로젝트 상세화면에서 사용할 거.
val projectId: Long,
val title: String,
val openingDate: String,
val closingDate: String,
val goalAmount: Int,
val totalAmount: Int,
val summary: String,
val supporter: Int,
val status: Int,
val userid: Long,
val thumbnail: Int,//나중에 String 으로 고치기.
val achievedRate: Double)*/
class OpenProjectActivity : AppCompatActivity() {
    val binding by lazy { ActivityOpenProjectBinding.inflate(layoutInflater) }
    val storage = Firebase.storage("gs://econg-7e3f6.appspot.com")
    var toThumbnail = ""
    var toUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnOD.setOnClickListener {
            val newFragment: DialogFragment = DatePickerFragment().newInstance(0)
            newFragment.show(supportFragmentManager, "datePicker")
        }

        binding.btnCD.setOnClickListener {
            val newFragment: DialogFragment = DatePickerFragment().newInstance(1)
            newFragment.show(supportFragmentManager, "datePicker")
        }

        binding.loadThumbnail.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.upload.setOnClickListener {
            uploadImage(toThumbnail, toUri!!)
        }
    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        //uploadImage(uri!!)
        toThumbnail = makeFilePath("images", "temp", uri!!)
        binding.thumbnail.text = toThumbnail
        toUri = uri
    }

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted ->
        if(isGranted){
            galleryLauncher.launch("image/*")
        }else{
            Toast.makeText(baseContext, "외부 저장소 읽기 권한을 승인해야 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImage(fullPath: String, uri: Uri){//uri: Uri
        val imageRef = storage.getReference(fullPath)
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnFailureListener{
            Log.d("MY", "uploadImage: Fail")
        }.addOnSuccessListener {
            Log.d("MY", "uploadImage: $fullPath")
        }
    }

    fun makeFilePath(path: String, userId: String, uri: Uri): String{
        val mimeType = contentResolver.getType(uri)?:"/none"
        val ext = mimeType.split("/")[1]
        val timeSuffix = System.currentTimeMillis()
        val filename = "${path}/${userId}_${timeSuffix}.${ext}"

        return filename
    }

    fun processDatePickerOpenResult(year: Int, month: Int, day: Int) {
        val month_string = Integer.toString(month + 1)
        val day_string = Integer.toString(day)
        val year_string = Integer.toString(year)
        val dateMessage = "$month_string/$day_string/$year_string"
        binding.openingDate.text = dateMessage
    }

    fun processDatePickerCloseResult(year: Int, month: Int, day: Int) {
        val month_string = Integer.toString(month + 1)
        val day_string = Integer.toString(day)
        val year_string = Integer.toString(year)
        val dateMessage = "$month_string/$day_string/$year_string"
        binding.closingDate.text = dateMessage
    }
}