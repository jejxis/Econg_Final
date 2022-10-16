package oasis.team.econg.econg

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.lang.UCharacter
import android.net.Uri
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_TEXT
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
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
import oasis.team.econg.econg.data.ImageUrl
import oasis.team.econg.econg.data.PreReward
import oasis.team.econg.econg.data.ProjectForOpen
import oasis.team.econg.econg.data.Reward
import oasis.team.econg.econg.databinding.ActivityOpenProjectBinding
import oasis.team.econg.econg.dialog.DatePickerFragment
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.rvAdapter.ImageData
import oasis.team.econg.econg.rvAdapter.OpenProjectImageAdapter
import oasis.team.econg.econg.rvAdapter.RewardAdapter
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.econg.utils.scrollToView

class OpenProjectActivity : AppCompatActivity() {
    val binding by lazy { ActivityOpenProjectBinding.inflate(layoutInflater) }
    private val storage = Firebase.storage(ECONG_URL)
    private var filePath = ""
    private var toThumbnail = ""
    private var toThumbnailUri : Uri? = null
    private var toUri : Uri? = null

    private var rewards: MutableList<PreReward>? = mutableListOf()
    private var rewardAdapter = RewardAdapter(this)

    private var imgDataList: MutableList<ImageData>? = mutableListOf()
    private var imgUrlList: MutableList<ImageUrl> = mutableListOf()
    private var imgAdapter = OpenProjectImageAdapter(this)

    private var pos = -1

    private var projectForUpload: ProjectForOpen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rewards!!.add(PreReward(null,null,null, null))
        rewardAdapter.setData(rewards)
        binding.rewardRecycler.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.rewardRecycler.adapter = rewardAdapter

        setSupportActionBar(binding.openProjectToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

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

        binding.cancel.setOnClickListener { finish() }

        binding.upload.setOnClickListener {

            projectForUpload = makeProject()//이거 가지고 레트로핏
            if(projectForUpload == null) return@setOnClickListener
            else{
                RetrofitManager.instance.openProject(auth = API.HEADER_TOKEN, param = projectForUpload!!, completion = {
                        responseState, responseBody ->
                    when(responseState){
                        RESPONSE_STATE.OKAY ->{
                            Log.d(TAG, "result: $responseBody")
                            uploadImage(toThumbnail, toThumbnailUri!!)
                            for(img in imgDataList!!){
                                if(img.str.compareTo("") != 0 && img.uri != null){
                                    Log.d("MY", img.toString())
                                    uploadImage(img.str, img.uri!!)
                                }
                            }
                            val intent = Intent(this@OpenProjectActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        RESPONSE_STATE.FAIL -> {
                            Log.d(TAG, "Payment: api call fail : $responseBody")
                            Toast.makeText(this@OpenProjectActivity, "결제 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
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
            rewards!!.add(PreReward(null,null,null, null))
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
        var month_string = Integer.toString(month + 1)
        var day_string = Integer.toString(day)
        val year_string = Integer.toString(year)
        if(month < 9) month_string = "0$month_string"
        if(day < 10) day_string = "0$day_string"
        val dateMessage = "$year_string-$month_string-$day_string"
        binding.openingDate.text = dateMessage
    }

    fun processDatePickerCloseResult(year: Int, month: Int, day: Int) {
        var month_string = Integer.toString(month + 1)
        var day_string = Integer.toString(day)
        val year_string = Integer.toString(year)
        if(month < 9) month_string = "0$month_string"
        if(day < 10) day_string = "0$day_string"
        val dateMessage = "$year_string-$month_string-$day_string"
        binding.closingDate.text = dateMessage
    }

    private val btnAddImage = object : OpenProjectImageAdapter.BtnAddImageListener{
        override fun loadAndAddImage(position: Int) {
            pos = position
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun makeProject(): ProjectForOpen?{
        if(binding.name.text.trim().isEmpty()){
            binding.name.requestFocus()
            return null
        }
        if(binding.thumbnail.text.isEmpty()){
            binding.openScroll.scrollToView(binding.thumbnail)
            return null
        }
        if(binding.summary.text.trim().isEmpty()){
            binding.summary.requestFocus()
            return null
        }
        if(binding.story.text.trim().isEmpty()){
            binding.story.requestFocus()
            return null
        }
        if(binding.goalAmount.text.trim().isEmpty()){
            binding.goalAmount.requestFocus()
            return null
        }
        if(binding.openingDate.text.isEmpty()){
            binding.openScroll.scrollToView(binding.openingDate)
            return null
        }
        if(binding.closingDate.text.isEmpty()){
            binding.openScroll.scrollToView(binding.closingDate)
            return null
        }

        for(img in imgDataList!!){
            if(img.str.compareTo("") != 0)
                imgUrlList.add(ImageUrl(ECONG_URL+"/"+img.str))
        }
        for(rw in rewards!!){
            if(rw.price == null || rw.combination == null || rw.stock == null || rw.price == null){
                Toast.makeText(this@OpenProjectActivity, "리워드의 빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show()
                return null
            }
        }
        val project = ProjectForOpen(
                title = binding.name.text.toString(),
                openingDate = binding.openingDate.text.toString(),
                closingDate = binding.closingDate.text.toString(),
                goalAmount = binding.goalAmount.text.toString().toInt(),
                summary = binding.summary.text.toString(),
                thumbnail = "$ECONG_URL/$toThumbnail",
                content = binding.story.text.toString(),
                projectImgList = imgUrlList as ArrayList<ImageUrl>,
                rewardList = rewards as ArrayList<PreReward>
            )

        Log.d(TAG, "makeProject: $project")
        return project
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}