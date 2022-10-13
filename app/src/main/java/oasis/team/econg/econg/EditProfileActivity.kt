package oasis.team.econg.econg

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.UserEditProfile
import oasis.team.econg.econg.data.UserTransfer
import oasis.team.econg.econg.databinding.ActivityEditProfileBinding
import oasis.team.econg.econg.retrofit.RetrofitManager
import oasis.team.econg.econg.utils.API
import oasis.team.econg.econg.utils.Constants.ECONG_URL
import oasis.team.econg.econg.utils.Constants.TAG
import oasis.team.econg.econg.utils.RESPONSE_STATE
import oasis.team.econg.econg.utils.loadImageSetView
import okhttp3.MediaType.Companion.toMediaType

class EditProfileActivity : AppCompatActivity() {
    val binding by lazy{ActivityEditProfileBinding.inflate(layoutInflater)}
    private val storage = Firebase.storage(ECONG_URL)
    private var filePath = ""
    private var toThumbnail = ""
    private var toFirebase = ""
    private var toThumbnailUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent
        val user = intent.getSerializableExtra("obj") as UserTransfer?
        setDataFrom(user)

        setSupportActionBar(binding.editProfileToolBar);
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.btnEditImage.setOnClickListener {
            permissionLauncherForThumbnail.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.btnApply.setOnClickListener {
            val profile = makeEditProfile()
            RetrofitManager.instance.editMyProfile(auth = API.HEADER_TOKEN, userId = user!!.userId, profile = profile, completion = {
                responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY->{
                        Log.d(TAG, "result: $responseBody")
                        if(toThumbnailUri != null)
                            uploadImage(toFirebase, toThumbnailUri!!)
                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "EditProfile: api call fail : $responseBody")
                        Toast.makeText(this@EditProfileActivity, "프로필 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    fun setDataFrom(user: UserTransfer?){
        binding.nickName.setText(user!!.nickName)
        binding.description.setText(user!!.description)
        storage.loadImageSetView(user!!.profileUrl, binding.imgProfile)
        toThumbnail = user.profileUrl
    }

    fun makeEditProfile(): UserEditProfile{
        val profile = UserEditProfile(
            nickName = binding.nickName.text.toString(),
            description = binding.description.text.toString(),
            profileUrl = toThumbnail
        )
        return profile
    }

    val galleryLauncherForThumbnail = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        toThumbnailUri = uri
        filePath = makeFilePath("images", "temp", uri!!)
        toThumbnail = "$ECONG_URL/$filePath"
        toFirebase = filePath
        Glide.with(binding.imgProfile).load(uri).into(binding.imgProfile)
        Log.d(TAG, "uri: $toThumbnailUri")
        Log.d(TAG, "filename: $toThumbnail")
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