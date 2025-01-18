package com.eunhye.composeplayground.map

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class GsMapViewModel(application: Application) : AndroidViewModel(application), LifecycleEventObserver {
    private val fusedLocationProviderClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    private val locationRequest : LocationRequest
    private val locationCallback : MyLocationCallBack

    private var _state = mutableStateOf(
        MapState(null, PolylineOptions().width(5f).color(Color.RED))
    )
    val state : State<MapState> = _state

    init {
        locationCallback = MyLocationCallBack()
        locationRequest = LocationRequest.Builder(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(5000)
            .build()
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener() {
        Looper.myLooper()?.let { looper ->
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                looper,
            )
        }
    }

    private fun removeLocationListener() {
        // 현재 위치 요청을 삭제 ②
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if(event == Lifecycle.Event.ON_RESUME) {
            addLocationListener()
        }

        if(event == Lifecycle.Event.ON_PAUSE) {
            removeLocationListener()
        }
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            val polylineOptions = state.value.polylineOptions

            _state.value = state.value.copy(
                location = location,
                polylineOptions = polylineOptions.add(
                    LatLng(
                        location?.latitude ?: 0.0,
                        location?.longitude?: 0.0
                    )
                )
            )
        }
    }
}
