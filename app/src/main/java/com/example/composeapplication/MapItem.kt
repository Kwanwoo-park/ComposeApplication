package com.example.composeapplication

import com.google.android.gms.maps.model.LatLng

data class MapItem(
    val latLng: LatLng,
    val title: String,
    val image: String
)

fun getMapItemList() = listOf(
    MapItem(
        latLng = LatLng(37.740118399999744, 127.04342009999966),
        title = "스타벅스 의정부공원점",
        image = "https://ldb-phinf.pstatic.net/20200825_200/1598301949474sBbkY_JPEG/3509_20171121080426_cnkfi.jpg"
    ),
    MapItem(
        latLng = LatLng(37.73786099999975, 127.04186979999947),
        title = "스타벅스 신세계의정부",
        image = "https://ldb-phinf.pstatic.net/20200825_242/1598302003990GfJlO_JPEG/9698_20180615072632_2qg34.jpg"
    ),
    MapItem(
        latLng = LatLng(37.73305020000005, 127.03457659999995),
        title = "스타벅스 의정부예술의전당DT점",
        image = "https://ldb-phinf.pstatic.net/20210804_47/1628023521693AOigS_JPEG/3571_20210803091352_qzsc3.jpg"
    ),
)
