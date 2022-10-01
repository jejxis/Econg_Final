package oasis.team.econg.econg.data

data class User(
    val userId: Long,
    val nickName: String,
    val description: String?,
    val profileUrl: String,
    val authenticate: Boolean
    )
