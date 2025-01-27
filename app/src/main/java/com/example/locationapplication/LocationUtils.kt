package com.example.locationapplication

import android.content.Context
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context:Context) {

    private val _fusedLocationClient:FusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(context)

@SuppressLint("MissingPermission")


    fun requestLocationUpdate(viewModel: LocationViewModel){
        val LocationCallback=object:LocationCallback(){
            override fun onLocationResult(LocationResult: LocationResult){
                super.onLocationResult(LocationResult)
                LocationResult.lastLocation?.let{
                    val location =LocationData(Latitude=it.latitude, Longitude = it.longitude)
                    viewModel.UpdateLocation(location)
                }

            }

        }
        val locationRequest=LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).build()
         _fusedLocationClient.requestLocationUpdates(locationRequest,LocationCallback, Looper.getMainLooper())
    }


    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
        &&
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    fun reverseGeoCodeLocation(location:LocationData):String{
        val geocoder= Geocoder(context, Locale.getDefault())
        val coordinates=LatLng(location.Latitude,location.Longitude)
        val addresses: MutableList<Address>? = geocoder.getFromLocation(coordinates.latitude,coordinates.longitude,1)
        return if(addresses?.isNotEmpty()==true){
            addresses[0].getAddressLine(0)
        }else{
            "Address Not  Found"
        }
    }
}
