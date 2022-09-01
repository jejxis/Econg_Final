package oasis.team.econg.econg.data

import android.graphics.drawable.Drawable
//Project(val projectId: Long, val tittle: String, val openingDate: datetime, val closingDate: datetime, val goalAmount: Int, val totalAmount: Int, val summary: String, val supporter: Int, val status: int, val userid: Long, val thumbnail: String, val acheivedRate: Double?)
//카테고리, 회사명, 프로젝트이름, 설명, 달성률
data class Project(val id: Int, val img: Int, val category: String, val company: String, val projectName: String, val projectInfo: String, val achRate: Double)
data class ProjectDetail(//프로젝트 상세화면에서 사용할 거.
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
    val achievedRate: Double,
    val story: String)
