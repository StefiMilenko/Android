package elfak.mosis.mobproj.model

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint


class LocationViewModel: ViewModel() {
    private val _longitude = MutableLiveData<String>( "")
    val longitude:LiveData<String>
    get() = _longitude
    private val _latitude = MutableLiveData<String>("")
    val latitude:LiveData<String>
    get() = _latitude
    var setLocation: Boolean = false
    var setUserLocation: Boolean = false
    private val _userlongitude = MutableLiveData<String>( "")
    var userlongitude:LiveData<String> = _userlongitude
    private val _userlatitude = MutableLiveData<String>("")
    var userlatitude:LiveData<String> = _userlatitude
    fun setLocation(lon:String, lat:String){
        _longitude.value = lon
        _latitude.value = lat
        setLocation=false
    }
    fun setUserLocation(){
        setUserLocation = true
    }
    fun fetchUserLocation(loc: GeoPoint) {
        _userlatitude.postValue(loc.longitude.toString())
        _userlongitude.postValue(loc.latitude.toString())
        /*val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                _userlatitude.value = location.latitude.toString()
                userlatitude = _userlatitude
                _userlongitude.value = location.longitude.toString()
                userlongitude = _userlongitude
            }
        }*/
    }
    fun onProviderDisabled(provider: String?) {}

    fun onProviderEnabled(provider: String?) {}

    fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}