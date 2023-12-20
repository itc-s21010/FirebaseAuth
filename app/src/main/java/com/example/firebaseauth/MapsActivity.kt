package com.example.firebaseauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu

import android.widget.ProgressBar
import android.widget.Toast
import android.widget.ViewAnimator
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnPoiClickListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnPoiClickListener {
    private lateinit var mGoogleMap:GoogleMap
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        Places.initialize(applicationContext,getString(R.string.google_maps_key))
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Toast.makeText(this@MapsActivity,"some Error in Search", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng!!
                zoomOnMap(latLng)
            }
        })

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val mapOptionButton: ImageButton = findViewById(R.id.mapOptionMenu)
        val popupMenu = PopupMenu(this,mapOptionButton)
        popupMenu.menuInflater.inflate(R.menu.map_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            changeMap(menuItem.itemId)
            true
        }
        mapOptionButton.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun zoomOnMap(latLng:LatLng)
    {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng,12f)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    private fun changeMap(itemId: Int) {
        when(itemId)
        {
            R.id.normal_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid_map_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        googleMap.setOnPoiClickListener(this)

        val okinawa = LatLng(26.2020623, 127.6620562)
        mGoogleMap.addMarker(MarkerOptions().position(okinawa).title("marker"))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(okinawa, 12f))
    }

    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(this, """Clicked: ${poi.name}
            Place ID:${poi.placeId}
            latitude:${poi.latLng.latitude} Longitude:${poi.latLng.longitude}""",
            Toast.LENGTH_LONG
        ).show()
    }
}