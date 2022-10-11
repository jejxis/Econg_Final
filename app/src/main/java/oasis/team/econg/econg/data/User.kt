package oasis.team.econg.econg.data

import java.io.Serializable

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
data class UserForFollow(
    val userId: Long,
    val userName: String,
    val profileUrl: String,
    val follow: Boolean,
    val myProfile: Boolean
)
data class UserEditProfile(
    val nickName: String,
    val description: String,
    val profileUrl: String
)
data class UserTransfer(
    val userId: Long,
    val nickName: String,
    val description: String?,
    val profileUrl: String
): Serializable