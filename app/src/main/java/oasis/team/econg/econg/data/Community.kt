package oasis.team.econg.econg.data

import java.time.LocalDateTime

//comment: text, projectId: Long, userid: Long
data class ProjectCommunity(
    val id: Int,
    val content: String,
    val updatedAt: String,
    val userId: Long,
    val userName: String,
    val userProfileUrl: String
    )
data class MyCommunity(
    val id: Long,
    val content: String,
    val updatedAt: String,
    val userId: Long,
    val userName: String,
    val userProfileUrl: String,
    val projectId: Long,
    val projectName: String
)
