package oasis.team.econg.econg.data

import android.graphics.drawable.Drawable

//카테고리, 회사명, 프로젝트이름, 설명, 달성률
data class Project(val id: Int, val rank: Int?,val img: Int, val category: String, val company: String, val projectName: String, val projectInfo: String, val achRate: Double)

