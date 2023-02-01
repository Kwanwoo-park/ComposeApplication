package com.example.composeapplication

import com.google.android.gms.maps.model.LatLng

data class MapItem(
    val latLng: LatLng,
    val title: String,
    val image: String
)

fun getMapItemList() = listOf(
    MapItem(
        latLng = LatLng(37.752146122127634, 126.7657990497292),
        title = "스타벅스 금릉역점",
        image = "https://firebasestorage.googleapis.com/v0/b/composeapplication.appspot.com/o/starbucks%2F%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3.jpeg?alt=media&token=a55102ca-e6d5-4cc6-a17a-13e5c0ae50f6"
    ),
    MapItem(
        latLng = LatLng(37.76392024854556, 126.77427368046001),
        title = "스타벅스 금촌역점",
        image = "https://firebasestorage.googleapis.com/v0/b/composeapplication.appspot.com/o/starbucks%2Foutput_4188444110.jpg?alt=media&token=0046d275-6cca-4831-9161-db34b17975dd"
    ),
    MapItem(
        latLng = LatLng(37.712261419711304, 126.75967001205711),
        title = "스타벅스 야당역",
        image = "https://img.seoul.co.kr//img/upload/2020/08/20/SSI_20200820003036.jpg"
    ),
)
