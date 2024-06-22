package com.example.locationapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.locationapplication.ui.theme.LocationApplicationTheme
import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel:LocationViewModel by viewModels()
        setContent {
            LocationApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background)
                {
                  App(viewModel)
                }

            }
        }
    }
}
@Composable
fun App(viewModel: LocationViewModel){

    val context= LocalContext.current
    val locationUtils= LocationUtils(context)

    LocationDisplay(locationUtils = locationUtils, viewModel,context = context)
}


@Composable
fun LocationDisplay(locationUtils: LocationUtils,viewModel: LocationViewModel,context: Context){
   val location=viewModel.location.value
   val address =location?.let { locationUtils.reverseGeoCodeLocation(location) }

    val requestPermissionLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult ={permissions->if(permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true
            && permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true){
        }else{
            //Ask for permission
            locationUtils.requestLocationUpdate(viewModel)
            val rationaleRequired= ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)||
                    (ActivityCompat.shouldShowRequestPermissionRationale(context,Manifest.permission.ACCESS_COARSE_LOCATION)

            )
            if(rationaleRequired){
                Toast.makeText(context,"Location permission is needed",Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(context,"Go to the settings and grant permission",Toast.LENGTH_LONG).show()
            }
        }}
        )



    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)
    {
        if(locationUtils!=null){
            if (location != null) {
                Text("Address:${location.Latitude}${location.Longitude}\n $address")
            }
        }else{
            Text(text = "Location is not available")
        }

        Button(onClick = {
            if(locationUtils.hasLocationPermission(context)){
                //permission granted updated location
                locationUtils.requestLocationUpdate(viewModel)
            }else{
                //Request location permission
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION
                ,Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }) {
            Text(text = "Get Location")
        }
    }

}