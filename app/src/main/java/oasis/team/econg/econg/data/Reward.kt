package oasis.team.econg.econg.data

data class Reward(//주문 확인용
    val projectId: Int,
    val title: String,
    val thumbnail: String,
    val rewardId: Int,
    val rewardName: String,
    val price: Int,
    val combination: String
)

data class SimpleReward(//프로젝트 상세화면...결제 다이얼로그
    val rewardId: Long,
    val name: String,
    val price: Int,
    val stock: Int,
    val soldQuantity: Int,
    val combination: String
)

data class PreReward(//프로젝트 올릴 때
    val name: String?,
    val price: Int?,
    val combination: String?
)
