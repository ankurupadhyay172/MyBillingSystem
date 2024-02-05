package com.billing.mybilling.utils

enum class SelectedAction(val type:String){
    ADD("add"),
    UPDATE("update"),
    DELETE("delete")
}

enum class OrderType(val type: String){
    TABLE("table"),
    PACKING("packing")
}

enum class editType(val type: String){
    DISCOUNT("Discount"),
    DELIVERY_CHARGES("Delivery charges")
}

enum class OrderStatus(val status:Int){
    PENDING(0),
    DELIVERED(1),
    FAILED(2)
}

enum class PaymentType(val type:Int){
    ONLINE(0),
    OFFLINE(1)
}
enum class AnalyticsType(val type:Int) {
    DayByDay(0),
    MonthByMonth(1),
    YearByYear(2)
}

enum class UserType(val id:Int){
    ADMIN(1),
    STAFF(2)
}

enum class UserStatus(val status:Int){
    ACTIVE(1),
    InACTIVE(2)
}

