package oasis.team.econg.econg.data

import android.graphics.drawable.Drawable
import oasis.team.econg.econg.rvAdapter.ImageData

//Project(val projectId: Long, val tittle: String, val openingDate: datetime, val closingDate: datetime, val goalAmount: Int, val totalAmount: Int, val summary: String, val supporter: Int, val status: int, val userid: Long, val thumbnail: String, val acheivedRate: Double?)
//카테고리, 회사명, 프로젝트이름, 설명, 달성률

data class Project(//프로젝트 리사이클러뷰에서 사용
    val id: Long,
    val title: String,
    val openingDate: String,
    val closingDate: String,
    val totalAmount: Int,
    val thumbnail: String,
    val summary: String,
    val authenticate: Boolean,
    val user: String,
    val achievedRate: Int
    )
data class ProjectDetail(//프로젝트 상세화면에서 사용할 거.
    val id: Long,
    val title: String,
    val openingDate: String,
    val closingDate: String,
    val goalAmount: Int,
    val totalAmount: Int,
    val summary: String,
    val content: String,
    val thumbnail: String,
    val projectAuthenticate: Boolean,
    val favorite: Boolean,
    val userId: Long,
    val userName: String,
    val userAuthenticate: Boolean,
    val projectImgList: ArrayList<ProjectImage>,
    val rewardList: ArrayList<SimpleReward>
)
data class ProjectImage(val projectImgId: Long, val productImgUrl: String)
