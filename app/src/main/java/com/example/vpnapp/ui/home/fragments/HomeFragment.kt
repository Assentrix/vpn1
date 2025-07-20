package com.example.vpnapp.ui.home.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.LOCATION_SERVICE
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpnapp.R
import com.example.vpnapp.databinding.FragmentHomeBinding
import com.example.vpnapp.ui.MainActivity
import com.example.vpnapp.ui.home.viewModel.HomeViewModel
import com.example.vpnapp.utils.extensions.navigateWithAnimation
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var activity: MainActivity

    private var mGoogleMap: GoogleMap? = null
    private var currentMarker: Marker? = null

    private var newDevicePopupVisible = true
    private var isFistTimeLaunch = true

    // Location Handling
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            (permissions[Manifest.permission.ACCESS_FINE_LOCATION]
                ?: false) || (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false) -> {
                if (isLocationEnabled()) {
                    getUserLocationAndUpdateMap()
                } else {
                    createTurnGPSOnLocationRequest()
                }
            }
        }
    }
    private val gpsEnableLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            getUserLocationAndUpdateMap()
        } else {
            Toast.makeText(activity, getString(R.string.toast_turn_on_gps), LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as MainActivity
        if (isFistTimeLaunch) {
            binding.layoutNewDeviceCreated.visibility = View.VISIBLE
            binding.mapTopLayerView.visibility = View.VISIBLE
            binding.loader.visibility = View.VISIBLE
            requestAppPermission()
            setUpMap()
            isFistTimeLaunch = false
        }
        setUpObservers()
        setUpInitialUI()
        setUpTextViews()
        setUpClickListeners()
    }

    private fun setUpInitialUI() {
        updateUIAsPerConnection(isConnected = false)
    }

    private fun setUpObservers() {
        viewModel.deviceNameWithStyle.observe(viewLifecycleOwner) { styledText ->
            binding.txtDeviceName.text = styledText
        }
        viewModel.timeLeftWithStyle.observe(viewLifecycleOwner) { styledText ->
            binding.txtTimeLeft.text = styledText
        }
        viewModel.inIPWithStyle.observe(viewLifecycleOwner) { styledText ->
            binding.txtInIp.text = styledText
        }
        viewModel.outIPWithStyle.observe(viewLifecycleOwner) { styledText ->
            binding.txtOutIp.text = styledText
        }
        viewModel.isVPNConnected.observe(viewLifecycleOwner) { isConnected ->
            updateUIAsPerConnection(isConnected)
        }
    }

    private fun setUpTextViews() {
        binding.apply {
            val deviceName = "Deep Coyote" // Replace with your dynamic value
            viewModel.appendDeviceNameWithStyle(
                activity, "Device name:", deviceName
            )
            val timeLeft = "172 days"
            viewModel.appendTimeLeftWithStyle(
                activity, "Device name:", timeLeft
            )
            val inIP = "146.59.232:2323726 UDP"
            viewModel.appendInIPAddressWithStyle(
                activity, "In: ", inIP
            )
            val outIP = "00 43 65 45 653"
            viewModel.appendOutIPAddressWithStyle(
                activity, "Out: ", outIP
            )
        }
    }

    private fun setUpClickListeners() {
        binding.apply {
            btnSettings.setOnClickListener {
                findNavController().navigateWithAnimation(R.id.settingsFragment)
            }

            btnAccount.setOnClickListener {
                findNavController().navigateWithAnimation(R.id.accountFragment)
            }

            btnSelectLocation.setOnClickListener {
//                updateCurrentLocation(generateRandomCoordinates())
                findNavController().navigateWithAnimation(R.id.selectLocationFragment)
            }

            btnSecureConnection.setOnClickListener {
                viewModel.toggleVPNConnection()
            }

            btnCloseNewDeviceCreated.setOnClickListener {
                layoutNewDeviceCreated.animate().alpha(0f)
                    .setDuration(300) // Animation duration in milliseconds
                    .setInterpolator(AccelerateDecelerateInterpolator()) // Ease-in-out interpolator
                    .withEndAction {
                        layoutNewDeviceCreated.visibility = View.GONE
                    }.start()
            }
        }
    }

    private fun updateUIAsPerConnection(isConnected: Boolean) {
        binding.apply {
            btnSecureConnection.text =
                getString(if (isConnected) R.string.txt_disconnect else R.string.txt_secure_connection)
            btnSecureConnection.backgroundTintList = ContextCompat.getColorStateList(
                activity, if (isConnected) R.color.dangerRed else R.color.successGreen
            )
            txtConnectionStatus.text =
                getString(if (isConnected) R.string.txt_secured_connection else R.string.txt_not_connected)
            txtConnectionStatus.setTextColor(
                ContextCompat.getColor(
                    activity, if (isConnected) R.color.successGreen else R.color.dangerRed
                )
            )
            viewDividerNotConnected.visibility = if (isConnected) View.GONE else View.VISIBLE
            connectionStatusGroup.visibility = if (isConnected) View.VISIBLE else View.GONE
        }
    }

    private fun setUpMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frg_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style)
        googleMap.setMapStyle(mapStyleOptions)
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isCompassEnabled = false

        mGoogleMap = googleMap
        mGoogleMap?.setOnCameraMoveListener {
            lifecycleScope.launch {
                delay(4000)
                binding.apply {
                    mapTopLayerView.visibility = View.GONE
                    loader.visibility = View.GONE
                }
            }
        }
    }

    private fun requestAppPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun generateRandomCoordinates(): LatLng {
        // Define the geographical bounds of the United States
        val minLatitude = 24.396308  // Southernmost point
        val maxLatitude = 49.384358  // Northernmost point
        val minLongitude = -125.0    // Westernmost point
        val maxLongitude = -66.93457 // Easternmost point

        // Generate random latitude and longitude within the bounds
        val randomLatitude = Random.nextDouble(minLatitude, maxLatitude)
        val randomLongitude = Random.nextDouble(minLongitude, maxLongitude)

        // Return the generated coordinates as a LatLng object
        return LatLng(randomLatitude, randomLongitude)
    }

    private fun createTurnGPSOnLocationRequest() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000
        ).setMinUpdateDistanceMeters(5000F).build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            getUserLocationAndUpdateMap()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(e.resolution).build()
                    gpsEnableLauncher.launch(intentSenderRequest)
                } catch (_: java.lang.Exception) {
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocationAndUpdateMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        val result = fusedLocationClient?.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token
        )
        result?.addOnCompleteListener {
            it.result?.let { location ->
                updateCurrentLocation(LatLng(location.latitude, location.longitude))
            }
        }
    }

    private fun updateCurrentLocation(latLng: LatLng) {
        val markerIcon = BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.img_current_location_marker)
        )
        currentMarker?.remove()

        // Add a new marker at the updated location
        val markerOptions = MarkerOptions().position(latLng).icon(markerIcon)

        currentMarker = mGoogleMap?.addMarker(markerOptions)
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(5.0f)
            .bearing(0f)      // Set a default orientation of the camera (optional)
            .tilt(0f).build()
        mGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null)
    }
}
