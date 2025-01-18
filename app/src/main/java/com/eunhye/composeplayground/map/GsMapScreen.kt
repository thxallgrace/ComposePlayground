package com.eunhye.composeplayground.map

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng


/**
 *  var grant by remember { mutableStateOf(false) }
 *
 *             val launcher = rememberLauncherForActivityResult(
 *                 contract = ActivityResultContracts.RequestPermission(),
 *                 onResult = { isGranted ->
 *                     grant = isGranted
 *                 }
 *             )
 *
 *             if(ContextCompat.checkSelfPermission(
 *                     this,
 *                     android.Manifest.permission.ACCESS_FINE_LOCATION
 *             ) == PackageManager.PERMISSION_GRANTED) {
 *                 grant = true
 *             }
 *
 *             if(grant) {
 *                 val viewModel = viewModel<GsMapViewModel>()
 *                 lifecycle.addObserver(viewModel)
 *                 GsMapScreen(viewModel = viewModel)
 *             } else {
 *                 Column(
 *                     modifier = Modifier.fillMaxSize(),
 *                     verticalArrangement = Arrangement.Center,
 *                     horizontalAlignment = Alignment.CenterHorizontally,
 *                 ) {
 *                     Text("권한이 허용되지 않았습니다")
 *                     Button(onClick = {
 *                         launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
 *                     }) {
 *                         Text("권한 요청")
 *                     }
 *                 }
 *             }
 */

@Composable
fun GsMapScreen(viewModel: GsMapViewModel) {
    val map = rememberMapView()
    val state = viewModel.state.value

    AndroidView(
        factory = { map },
        update = { mapView ->
            mapView.getMapAsync { googleMap ->
                state.location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                    googleMap.addPolyline(state.polylineOptions)
                }
            }
        },
    )
}


@Composable
fun rememberMapView() : MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =  LifecycleEventObserver { _ , event ->
            when(event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return mapView
}
