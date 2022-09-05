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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import oasis.team.econg.econg.data.Reward
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
    var rewardCount = 0

    var rewards: MutableList<Reward>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //addRewardView()

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

        binding.btnAddReward.setOnClickListener {
            addRewardView()
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

    private fun addRewardView(){
        val layout = ConstraintLayout(this)
        layout.background = resources.getDrawable(R.drawable.reward, null)//R.drawable.reward.toDrawable()

        val parForCloseButton = ConstraintLayout.LayoutParams(50,50 )
        val closeButton = makeCloseButton()
        parForCloseButton.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        parForCloseButton.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        parForCloseButton.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        closeButton.layoutParams = parForCloseButton

        layout.addView(closeButton)

        val parForRewardLayout = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        val rewardLayout = LinearLayout(this)
        rewardLayout.setPadding(10,10,70,10)
        rewardLayout.orientation = LinearLayout.VERTICAL
        parForRewardLayout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        parForRewardLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        parForRewardLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        rewardLayout.layoutParams = parForRewardLayout

        layout.addView(rewardLayout)

        val parForReward = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val rewardName = EditText(this)
        rewardName.inputType = TYPE_CLASS_TEXT
        rewardName.hint = "리워드 이름"
        rewardName.layoutParams = parForReward

        rewardLayout.addView(rewardName)

        val rewardCombination = EditText(this)
        rewardCombination.inputType = TYPE_CLASS_TEXT
        rewardCombination.hint = "리워드 구성"
        rewardCombination.layoutParams = parForReward

        rewardLayout.addView(rewardCombination)

        val rewardPrice = EditText(this)
        rewardPrice.layoutParams = parForReward
        rewardPrice.inputType = TYPE_CLASS_NUMBER
        rewardPrice.hint = "리워드 가격"
        rewardPrice.layoutParams = parForReward

        rewardLayout.addView(rewardPrice)

        binding.rewardLayout.addView(layout)
        closeButton.setOnClickListener {
            binding.rewardLayout.removeView(layout)
            rewardCount--
        }
        rewardCount++
    }


    private fun makeCloseButton(): ImageButton{
        val btn = ImageButton(this)
        btn.setImageResource(R.drawable.ic_round_close_24)
        btn.setBackgroundColor(Color.RED)
        btn.maxWidth = 30
        btn.maxHeight = 30
        return btn
    }
}