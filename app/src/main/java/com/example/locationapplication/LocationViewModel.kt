package com.example.locationapplication
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State

class LocationViewModel:ViewModel() {
    private val _location= mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location


    fun UpdateLocation(newLocation:LocationData){
        _location.value=newLocation
    }

}