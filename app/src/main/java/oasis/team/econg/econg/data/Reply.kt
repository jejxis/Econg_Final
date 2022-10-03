package oasis.team.econg.econg.data

import java.time.LocalDateTime

//data class Reply(val project: Project, val community: Community)
data class ProjectReply(val id: Int, val content: String, val updatedAt: LocalDateTime, val userId: Long, val userName: String, val userProfileUrl: String)
