package com.rogerio.myfitapp.detailgoal

import com.google.android.gms.fitness.data.*
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import com.rogerio.myfitapp.presentation.model.RewardViewEntity


fun getReward() = RewardViewEntity(
)
fun getFitItemViewEntity() = FitItemViewEntity(
    reward = getReward()
)

fun getDataSet() = DataSet(getRaw(), getList())

fun getList(): MutableList<DataSource>? {
    val list: MutableList<DataSource> = ArrayList()
    val data = DataSource.Builder()
        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .setType(DataSource.TYPE_RAW)
        .build()

    list.add(data)
    return list
}

fun getRaw(): RawDataSet? {
    return  RawDataSet(0, getRawDataPoint(), false)
}

fun getRawDataPoint(): MutableList<RawDataPoint>? {
    val raw = RawDataPoint(0,0, getValues(),0,0,0,0)
    val list: MutableList<RawDataPoint> = ArrayList()
    list.add(raw)
    return list
}

fun getValues(): Array<out Value>? {
    val values: Array<out Value> = emptyArray()
    val value = Value(1)
//    values.set(0,value)
    return values
}

