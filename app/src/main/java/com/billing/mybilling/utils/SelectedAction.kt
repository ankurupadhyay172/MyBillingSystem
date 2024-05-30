package com.billing.mybilling.utils


enum class ActiveStatus(val type:Int){
    ACTIVE(1),
    INACTIVE(2)
}



enum class SelectedAction(val type:String){
    ADD("add"),
    UPDATE("update"),
    DELETE("delete"),
    UPDATE_STATUS("update_status")
}

enum class AttendanceType(val type: Int){
    PRESENT(1),
    ABSENT(2),
    HALF_DAY(3),
    LEAVE(4),
    HALF_DAY_LEAVE(5)
}


enum class OrderType(val type: String){
    TABLE("1"),
    PACKING("2"),
    SWIGGY("3"),
    ZOMATO("4")
}

enum class editType(val type: String){
    DISCOUNT("Discount"),
    DELIVERY_CHARGES("Delivery charges")
}

enum class OrderStatus(val status:Int){
    PENDING(0),
    COOKING(1),
    READY_TO_DELIVER(2),
    DELIVERED(3),
    FAILED(4)
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

fun String.setOrderOn(): String {
    return when (this) {
        OrderType.PACKING.type -> "Packing"
        OrderType.TABLE.type->"Table"
        OrderType.SWIGGY.type->"Swiggy"
        OrderType.ZOMATO.type->"Zomato"
        else -> "Table"
    }
}


fun Int.setOrderStatus(): String {
    return when (this) {
        OrderStatus.PENDING.status -> "Pending"
        OrderStatus.COOKING.status -> "Cooking"
        OrderStatus.READY_TO_DELIVER.status -> "Ready to Deliver"
        OrderStatus.DELIVERED.status -> "Delivered"
        OrderStatus.FAILED.status -> "Failed"
        else -> "Pending"
    }
}