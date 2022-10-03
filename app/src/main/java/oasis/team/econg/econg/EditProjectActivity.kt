package oasis.team.econg.econg

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.PreReward
import oasis.team.econg.econg.data.Reward
import oasis.team.econg.econg.databinding.ActivityEditProjectBinding
import oasis.team.econg.econg.dialog.DatePickerFragment
import oasis.team.econg.econg.rvAdapter.RewardAdapter

class EditProjectActivity : AppCompatActivity() {
    val binding by lazy{ActivityEditProjectBinding.inflate(layoutInflater)}
    private var projectID = ""

    private val storage = Firebase.storage("gs://econg-7e3f6.appspot.com")
    private var toThumbnail = ""
    private var toUri : Uri? = null

    private var rewards: MutableList<PreReward>? = mutableListOf()
    private var rewardAdapter = RewardAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("projectID")){
            projectID = intent.getStringExtra("projectID").toString()
        }

        loadProjectDataToEdit()

        loadProjectRewards()
        rewardAdapter.setData(rewards)
        binding.rewardRecycler.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.rewardRecycler.adapter = rewardAdapter

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
            Log.d("MY_ADD", "rewards: ${rewards.toString()}")
        }

        binding.btnAddReward.setOnClickListener {
            rewards = rewardAdapter.returnData()
            rewards!!.add(PreReward(null,null,null, null))
            rewardAdapter.setData(rewards)
            rewardAdapter.notifyDataSetChanged()
            Log.d("MY_ADD", "rewards: ${rewards.toString()}")
            Log.d("MY_ADD", "rewardAdapter.listData: ${rewardAdapter.listData}")
        }
    }

    private fun loadProjectDataToEdit(){
        binding.name.setText("프로젝트$projectID")
        binding.thumbnail.setText("프로젝트$projectID 썸네일")
        binding.summary.setText("프로젝트$projectID 요약")
        binding.story.setText("프로젝트$projectID 상세설명")
        binding.openingDate.setText("2022.8.23 ~ 2022.8.25")
        binding.closingDate.setText("2022.8.23 ~ 2022.8.25")
    }

    private fun loadProjectRewards(){
        for(i: Int in 1..5){
            rewards!!.add(
                PreReward(
                    "리워드$i",
                    5000,
                    "연필 하나, 볼펜 하나",
                    100
                )
            )
        }
    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        //uploadImage(uri!!)
        toThumbnail = makeFilePath("images", "temp", uri!!)
        binding.thumbnail.text = toThumbnail
        toUri = uri
    }

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
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