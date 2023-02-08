package com.example.cc_kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.cc_kotlin.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var station: station

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Appel du intent
        station = intent.getParcelableExtra<station>("station")!!

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Ajout d'un marqueur (celui sélectionné dans la MainActivity)
        val marqueur_station = LatLng(station.getLatitude(), station.getLongitude())
        // Options du marqueur
        mMap.addMarker(MarkerOptions()
            .position(marqueur_station)
            .title(station.getName())) // Affichage pas bon pour les places, je ne sais pas pourquoi...
        // Moove de la caméra sur le marqueur
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marqueur_station))

        // Affichage du nom de la station dans le textView de la Map
        val textViewMap = findViewById<TextView>(R.id.textViewMap)
        textViewMap.text = toString()
    }

    // toString pour l'affichage dans le textView
    override fun toString(): String{
        return "${station.getName()}"
    }
}