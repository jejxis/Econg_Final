package oasis.team.econg.econg.utils

import oasis.team.econg.econg.samplePreference.MyApplication

object Constants {
    const val ECONG_URL = ""
    const val TAG : String = "MYTAG"
}
enum class RESPONSE_STATE{
    OKAY,
    FAIL
}
object API{
    const val BASE_URL : String = ""

    var HEADER_TOKEN : String = "${MyApplication.prefs.token}"
}
