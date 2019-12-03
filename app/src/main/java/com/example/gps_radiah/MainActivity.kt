package com.example.gps_radiah

import android.content.ContentProviderClient
import android.content.SyncRequest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.net.CacheRequest

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var  locationCallback: LocationCallback

    val REQUEST_CODE = 1000;

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE->{
                if(grantResults.size>0)
                {
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        else {
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            imageView.setOnClickListener(View.OnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE
                    )
                    return@OnClickListener
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback,
                    Looper.myLooper()
                )

                imageView.isEnabled = !imageView.isEnabled
                imageView2.isEnabled = !imageView2.isEnabled
            })

            imageView2.setOnClickListener(View.OnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        , REQUEST_CODE
                    )
                    return@OnClickListener
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)

                imageView.isEnabled = !imageView.isEnabled
                imageView2.isEnabled = !imageView2.isEnabled
            })
        }
    }
    private fun buildLocationCallBack(){
        locationCallback= object :LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                var location= p0!!.locations.get(p0!!.locations.size-1)
                //get last location
                text_locations.text= location.latitude.toString()+"/"+location.longitude.toString()
            }
        }
    }
    private fun buildLocationRequest(){
        locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement=10f
    }

}
