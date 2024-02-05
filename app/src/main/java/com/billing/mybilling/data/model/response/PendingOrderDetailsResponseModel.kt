package com.billing.mybilling.data.model.response

data class PendingOrderDetailsResponseModel(val status: Int,val result:List<Products>){
    fun getTotalAmount():Int {
        var tot = 0
        for (i in result){
            tot += i.selected_quan * i.price!!.toInt()
        }
        return tot
    }
}


