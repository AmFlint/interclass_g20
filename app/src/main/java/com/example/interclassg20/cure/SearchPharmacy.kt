package com.example.interclassg20.cure


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.FragmentActivity
import android.view.ContextMenu
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.android.synthetic.main.fragment_search_pharmacy.*
import com.google.android.gms.maps.model.LatLngBounds
import android.widget.Toast
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.Marker
import android.view.InflateException
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_consultation.*


/**
 * Screen - Search Pharmacy on Google Map
 */
class SearchPharmacy : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    /**
     * Reference to google map
     */
    private var gMap: GoogleMap? = null

    /**
     * Selected Pharmacy Name
     */
    private var selectedPharmacy = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_pharmacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Google Map via fragment

        val fm = childFragmentManager
        var mapFragment = fm.findFragmentByTag("mapFragment") as SupportMapFragment?

        if (mapFragment == null) {
            mapFragment = SupportMapFragment()
            val ft = fm.beginTransaction()
            ft.add(R.id.mapFragmentContainer, mapFragment, "mapFragment")
            ft.commit()
            fm.executePendingTransactions()
        }

        val mapFrag = mapFragment as SupportMapFragment
        mapFrag.getMapAsync(this)
        setUpAutocomplete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val fm = requireActivity().fragmentManager
        val fragment = fm.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        val ft = fm.beginTransaction()
        ft.remove(fragment)
        ft.commit()
    }

    /**
     * Set up Google Autocomplete
     */
    fun setUpAutocomplete() {
        // Initialize Google AutoComplete via fragment
        val autocompleteFragment = requireActivity().fragmentManager
                .findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setBoundsBias(LatLngBounds(
                LatLng(48.837992, 2.444371),
                LatLng(48.892082, 2.318417)))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                if (!placeIsPharmacy(place)) {
                    val snackBar = Snackbar.make(view as View, "Veuillez sélectionner une pharmacie s'il vous plait", Snackbar.LENGTH_LONG)
                    snackBar.show()
                    return
                }

                selectedPharmacy = place.name.toString()
                moveCamera(place.latLng)
                placeMarker(place.latLng, place.name.toString())
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                println("error: $status")
            }
        })

    }

    /**
     * Logic to be executed when map is ready (loaded)
     */
    override fun onMapReady(googleM: GoogleMap) {
        gMap = googleM
        val heticLatLng = LatLng(48.852281, 2.420647)
        moveCamera(heticLatLng)
        googleM.animateCamera(CameraUpdateFactory.zoomTo(15f))

        googleM.setOnInfoWindowClickListener(this);
    }

    /**
     * Place a Marker on Map
     */
    fun placeMarker(latLng: LatLng, title: String) {
        val map = gMap as GoogleMap
        map.addMarker(MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet("Commander à cette pharmacie")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_pharma)))
    }

    /**
     * Move Google map camera to given position
     */
    fun moveCamera(latLng: LatLng) {
        val map = gMap as GoogleMap
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    /**
     * Check that a given place is a Pharmacie (by checking the name of the place)
     */
    fun placeIsPharmacy(place: Place): Boolean {
        return place.name.contains("Pharmacie")
    }

    /**
     * Click on Info Windows
     */
    override fun onInfoWindowClick(marker: Marker) {
        // TODO: Pass with arguments
        // Navigate to command page
        findNavController().navigate(R.id.fromMapToCmdConfirmation)
    }
}
