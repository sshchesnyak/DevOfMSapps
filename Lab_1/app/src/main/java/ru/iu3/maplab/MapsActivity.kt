package ru.iu3.maplab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.iu3.maplab.databinding.MapContainerBinding

var mapReady: Boolean = false;

class MapsActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

        private lateinit var mMap: GoogleMap
        private lateinit var binding: MapContainerBinding
        private lateinit var myLocations: ArrayList<Locations>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = MapContainerBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

            //Initialize location data
            myLocations = locationInit()

            //supply the spinner with the array of elements
            val places: ArrayList<String> = arrayListOf()
            for (i in 0 until myLocations.size) places.add(myLocations[i].name)
            val spinner: Spinner = findViewById<Spinner>(R.id.place_spinner)
            val placeAdapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,places)
            spinner.adapter = placeAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    val selected = myLocations.firstNotNullOfOrNull { item -> item.takeIf { item.name == parent.getItemAtPosition(pos)} }
                    if (selected?.name == getString(R.string.DefaultCity)) return
                    else{
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selected?.location,5.0F))
                        if (selected != null) Toast.makeText(applicationContext,getString(R.string.getPopulation)+selected.population.toString(),Toast.LENGTH_LONG).show()
                        else Toast.makeText(applicationContext,"Erroneous behaviour",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }
        }

        private fun locationInit(): ArrayList<Locations>{
            val places: ArrayList<Locations> = arrayListOf();
            places.add(Locations(getString(R.string.DefaultCity),LatLng(0.00,0.00),0))
            places.add(Locations(getString(R.string.Moscow), LatLng(55.755814,37.617635),12632409))
            places.add(Locations(getString(R.string.SaintPetersburgh),LatLng(59.939095,30.315868),5376672))
            places.add(Locations(getString(R.string.Sochi),LatLng(43.585525,39.723062),432322))
            places.add(Locations(getString(R.string.SouthSahalinsk),LatLng(46.9641,142.7285),200235))
            places.add(Locations(getString(R.string.Yaroslavl),LatLng(57.6261,39.8845),601403))
            return places;
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
            for (i in 1 until myLocations.size){
                mMap.addMarker(MarkerOptions().position(myLocations.elementAt(i).location).title(myLocations.elementAt(i).name+". "+getString(R.string.getPopulation)+" "+myLocations.elementAt(i).population))
            }
            mapReady=true;
            // Add a marker in Sydney and move the camera
            //val sydney = LatLng(-34.0, 151.0)
            //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }