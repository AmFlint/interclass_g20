package com.example.interclassg20.cure


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_delivery_1.*


class Delivery1Activity : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pillsBottleImage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toDelivery2))
        deliveryManImage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toDelivery2))
        deliveryDoneImage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toDelivery2))
        backButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toOrdonnance))

        requireActivity().setTitle(R.string.delivery)
    }
}
