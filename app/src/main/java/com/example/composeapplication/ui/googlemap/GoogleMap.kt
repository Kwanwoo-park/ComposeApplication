package com.example.composeapplication.ui.googlemap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composeapplication.MapViewPager
import com.example.composeapplication.R
import com.example.composeapplication.getMapItemList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GoogleMap() {
    val list = getMapItemList()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(list[0].latLng, 14f)
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false
            )
        )
    }

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings
        ) {
            list.forEachIndexed { index, mapItem ->
                MarkerInfoWindow(
                    state = MarkerState(position = mapItem.latLng),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                        true
                    }
                )
            }
        }

        MapViewPager(
            list = list,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 10.dp, max = 200.dp)
                .align(Alignment.BottomCenter)
        )

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                cameraPositionState
                    .move(CameraUpdateFactory.newLatLng(list[page].latLng))
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        var mapProperties by remember {
            mutableStateOf(
                MapProperties(maxZoomPreference = 30f, minZoomPreference = 15f)
            )
        }

        var mapUiSettings by remember {
            mutableStateOf(
                MapUiSettings(mapToolbarEnabled = true)
            )
        }

        val latLng = LatLng(37.75593943612764, 126.76820417030824)
        val cameraPositionState = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(latLng, 23f)
        }

        GoogleMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
//                Marker(
//                    state = MarkerState(position = latLng),
//                    title = "집",
//                    snippet = "Home"
//                )
            MarkerInfoWindowContent(
                state = MarkerState(position = latLng)
            ) {marker ->
                Button(
                    contentPadding = PaddingValues(10.dp),
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Yellow
                    )
                ) {
                    Column {
                        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = null)
                        Text(text = marker.title ?: "집", color = Color.Blue)
                    }
                }
            }
        }
    }
}