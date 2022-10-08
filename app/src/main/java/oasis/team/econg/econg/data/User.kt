package oasis.team.econg.econg.data

data class User(
    val userId: Long,
    val nickName: String,
    val description: String?,
    val profileUrl: String,
    val authenticate: Boolean
    )

data class UserProfile(
    val userId: Long,
    val nickName: String,
    val description: String?,
    val profileUrl: String,
    val authenticate: Boolean,
    val followingNum: Int,
    val followerNum: Int,
    val myProfile: Boolean,
    val isFollow: Boolean
)