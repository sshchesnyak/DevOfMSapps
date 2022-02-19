package ru.iu3.maplab

import com.google.android.gms.maps.model.LatLng

data class Locations(val name: String, val location: LatLng, val population: Int) {}