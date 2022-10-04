package oasis.team.econg.econg.data

data class Login(
    var code: Int,
    var message: String,
    var result: Token
)
data class Token(
    var token: String
)

