package com.billing.mybilling.data.model.response

data class TableResponseModel(val status: Int, val result:List<Table>)

data class Table(val table_id:String,val table_name:String,var isOccupied:Int,val timestamp:String,var isSelected:Boolean){
    fun isOccupied():Boolean{
        return isOccupied==1
    }

    fun updateOccupied(){
        if (isOccupied==1) isOccupied=0 else isOccupied = 1
    }
    fun updateSelected(){
        isSelected = !isSelected
    }
}