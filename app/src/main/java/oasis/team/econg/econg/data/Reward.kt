package oasis.team.econg.econg.data

data class Reward(
    val rewardId: Long,
    val name: String,
    val price: Int,
    val stock: Int,
    val soldQuantity: Int,
    val combination: String,
    val projectId: Long
)

data class PreReward(
    val name: String?,
    val price: Int?,
    val combination: String?
)
