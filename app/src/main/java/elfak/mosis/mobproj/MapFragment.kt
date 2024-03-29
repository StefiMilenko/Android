package elfak.mosis.mobproj

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import elfak.mosis.mobproj.model.LocationViewModel
import elfak.mosis.mobproj.model.PosaoViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {
    lateinit var map: MapView
    private val posaoViewModel: PosaoViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var ctx: Context? = getActivity()?.getApplicationContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences((ctx!!)))
        map = requireView().findViewById<MapView>(R.id.map)
        map.setMultiTouchControls(true)
        if(ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else{
            setupMap()
        }
        map.controller.setZoom(12.0)
        val startPoint = GeoPoint(43.3209, 21.8958)
        map.controller.setCenter(startPoint)
    }

    private fun setupMap(){
        var startPoint:GeoPoint = GeoPoint(43.3209, 21.8958)
        map.controller.setZoom(12.0)
        if(locationViewModel.setLocation){
            setOnMapClickOverlay()
        }
        else {
            if(posaoViewModel.selected!=null){
                startPoint =GeoPoint(posaoViewModel.selected!!.latitude.toDouble(), posaoViewModel.selected!!.longitude.toDouble())
            } else {
                setMyLocationOverlay()
            }
        }
        map.controller.animateTo(startPoint)
    }

    private fun setMyLocationOverlay(){
        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if(isGranted){
            setMyLocationOverlay()
            setOnMapClickOverlay()
        }
    }

    private fun setOnMapClickOverlay(){
        var receive = object: MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                var long= p?.longitude.toString()
                var lat= p?.latitude.toString()
                locationViewModel.setLocation(long,lat)
                findNavController().popBackStack()
                return true
            }
            override fun longPressHelper(p: GeoPoint):Boolean{
                return false
            }
        }
        var overlayEvents = MapEventsOverlay(receive)
        map.overlays.add(overlayEvents)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_edit -> {
                this.findNavController().navigate(R.id.action_MapFragment_to_EditFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        var item = menu.findItem(R.id.action_list)
        item.isVisible = false
        item = menu.findItem(R.id.action_show_map)
        item.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}