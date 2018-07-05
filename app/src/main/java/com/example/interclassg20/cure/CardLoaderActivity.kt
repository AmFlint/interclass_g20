package com.example.interclassg20.cure


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_card_loader.*
import kotlinx.android.synthetic.main.fragment_test.*

class CardLoaderActivity : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_loader, container, false)
    }

    /**
     * View created - Display pharmacy name and navigation on click listeners
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display Pharmacy Name
        val pharmacyName = arguments!!.getString("pharmacy_name")
        cl_verification_text.text = pharmacyName

        // Navigate to itinerary laoder screen with pharmacy name as arg
        cl_icon_loader.setOnClickListener({
            val bundle = Bundle()
            bundle.putString("pharmacy_name", pharmacyName)
            bundle.putString("pharmacy_address", arguments!!.getString("pharmacy_address"))
            it.findNavController().navigate(R.id.fromClToItinerary, bundle)
        })
        cancelButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toOrdonnance))

        requireActivity().setTitle(R.string.preparation)
    }
}
