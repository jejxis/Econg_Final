package oasis.team.econg.econg

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.icu.lang.UCharacter
import android.net.Uri
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_TEXT
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.PreReward
import oasis.team.econg.econg.data.Reward
import oasis.team.econg.econg.databinding.ActivityOpenProjectBinding
import oasis.team.econg.econg.dialog.DatePickerFragment
import oasis.team.econg.econg.rvAdapter.ImageData
import oasis.team.econg.econg.rvAdapter.OpenProjectImageAdapter
import oasis.team.econg.econg.rvAdapter.RewardAdapter


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
    private val storage = Firebase.storage("gs://econg-7e3f6.appspot.com")
    private var filePath = ""
    private var toThumbnail = ""
    private var toThumbnailUri : Uri? = null
    private var toUri : Uri? = null

    private var rewards: MutableList<PreReward>? = mutableListOf()
    private var rewardAdapter = RewardAdapter(this)

    private var imgDataList: MutableList<ImageData>? = mutableListOf()
    private var imgAdapter = OpenProjectImageAdapter(this)

    private var pos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rewards!!.add(PreReward(null,null,null))
        rewardAdapter.setData(rewards)
        binding.rewardRecycler.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.rewardRecycler.adapter = rewardAdapter

        imgDataList!!.add(ImageData("", null))
        imgAdapter.setData(imgDataList)
        binding.rvImage.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.rvImage.adapter = imgAdapter

        imgAdapter.setBtnAddImageListener(btnAddImage)

        binding.btnOD.setOnClickListener {
            val newFragment: DialogFragment = DatePickerFragment().newInstance(0)
            newFragment.show(supportFragmentManager, "datePicker")
        }

        binding.btnCD.setOnClickListener {
            val newFragment: DialogFragment = DatePickerFragment().newInstance(1)
            newFragment.show(supportFragmentManager, "datePicker")
        }

        binding.loadThumbnail.setOnClickListener {
            permissionLauncherForThumbnail.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        }

        binding.upload.setOnClickListener {
            uploadImage(toThumbnail, toThumbnailUri!!)
            for(img in imgDataList!!){
                if(img.str.compareTo("") != 0 && img.uri != null){
                    Log.d("MY", img.toString())
                    uploadImage(img.str, img.uri!!)
                }
            }
            Log.d("MY_ADD", "rewards: ${rewards.toString()}")
        }

        binding.btnAddImage.setOnClickListener {
            imgDataList = imgAdapter.returnData()

            if(imgDataList!!.size >= 5) return@setOnClickListener

            imgDataList!!.add(ImageData("", null))
            imgAdapter.setData(imgDataList)
            imgAdapter.notifyDataSetChanged()
        }

        binding.btnAddReward.setOnClickListener {
            rewards = rewardAdapter.returnData()
            rewards!!.add(PreReward(null,null,null))
            rewardAdapter.setData(rewards)
            rewardAdapter.notifyDataSetChanged()
            Log.d("MY_ADD", "rewards: ${rewards.toString()}")
            Log.d("MY_ADD", "rewardAdapter.listData: ${rewardAdapter.listData}")
        }
    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        //uploadImage(uri!!)
        filePath = makeFilePath("images", "temp", uri!!)
        toUri = uri
        imgDataList!!.removeAt(pos)
        imgDataList!!.add(pos, ImageData(filePath, toUri))
        imgAdapter.setData(imgDataList)
        imgAdapter.notifyDataSetChanged()
    }

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted ->
        if(isGranted){
            galleryLauncher.launch("image/*")
        }else{
            Toast.makeText(baseContext, "외부 저장소 읽기 권한을 승인해야 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncherForThumbnail = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        //uploadImage(uri!!)
        filePath = makeFilePath("images", "temp", uri!!)
        toUri = uri
        toThumbnail = filePath
        toThumbnailUri = toUri
        binding.thumbnail.text = toThumbnail.toString()//filePath
    }

    val permissionLauncherForThumbnail = registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted ->
        if(isGranted){
            galleryLauncherForThumbnail.launch("image/*")
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
        val filename = "/${path}/${userId}_${timeSuffix}.${ext}"

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

    private val btnAddImage = object : OpenProjectImageAdapter.BtnAddImageListener{
        override fun loadAndAddImage(position: Int) {
            pos = position
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}