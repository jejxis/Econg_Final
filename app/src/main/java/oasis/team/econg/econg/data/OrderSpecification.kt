package oasis.team.econg.econg.data
//"productImg": "Image/17/image:31489",
//        "title": "숏패딩이에요 재질 좋아요",
//        "price": 30000,
//        "orderId": 16,
//        "tradingMethod": "DIRECT",
//        "buyer": "상점상점",
//        "seller": "상점상점",
//        "shippingFee": "EXCLUDE",
//        "totalPrice": 30000,
//        "year": 2022,
//        "month": 3,
//        "day": 29,
//        "time": "01:51:46",
//        "address": null,
//        "directAddress": "지역정보 없음",
//        "name": null,
//        "buyerPhone": "01034546234",
//        "sellerPhone": "01034546234"
data class OrderSpecification(
    val address: String?,
    val buyer: Int,
    val buyerPhone: String,
    val day: Int,
    val directAddress: String,
    val month: Int,
    val name: String?,
    val orderId: Int,
    val price: Int,
    val productImg: String,
    val seller: Int,
    val sellerPhone: String,
    val shippingFee: String,
    val time: String,
    val title: String,
    val totalPrice: Int,
    val tradingMethod: String,
    val year: Int
)

data class Order(
    val addressOption: String,
    val payMethod: String,
    val point: Int,
    val productId: Int,
    val tax: Int,
    val tradingMethod: String
)