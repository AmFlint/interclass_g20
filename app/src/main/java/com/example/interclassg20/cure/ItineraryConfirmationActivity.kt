package com.example.interclassg20.cure


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_itinerary_confirmation.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import java.util.*


class ItineraryConfirmationActivity : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itinerary_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pharmacyName = arguments!!.getString("pharmacy_name")
        itinerary_pharmacy_name.text = pharmacyName

        okButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toOrdonnance))
        itineraryButton.setOnClickListener({
            val address = arguments!!.getString("pharmacy_address")
            val uri = String.format(Locale.FRANCE, "geo:0,0?q=%s", address)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            context!!.startActivity(intent)
        })
    }
}
