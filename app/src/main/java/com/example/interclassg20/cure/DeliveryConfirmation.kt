package com.example.interclassg20.cure


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_delivery_confirmation.*
import kotlinx.android.synthetic.main.fragment_delivery_confirmation.*
import org.w3c.dom.Text
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import com.example.interclassg20.cure.models.Medicament
import com.example.interclassg20.cure.models.OrderItem
import com.example.interclassg20.cure.services.OrderItemService
import com.example.interclassg20.cure.adapters.OrderAdapter

/**
 * Command Confirmation Screen - Summary of cart (all chemicals) + price
 */
class DeliveryConfirmation : Fragment() {

    /**
     * Activity Order lines
     */
    private var mOrders: Array<OrderItem>

    /**
     * Main Description
     */
    private var mDescription: String

    /**
     * Order Item Service - Interact with order items
     */
    val mOrderItemService: OrderItemService

    init {
        mOrderItemService = OrderItemService()

        mOrders = mOrderItemService.getOrderItems()
        // Initialize Command description
        mDescription = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delivery_total_price.findViewById<TextView>(R.id.order_item_description).text = "Le prix total prend en compte les réductions ainsi que les frais de livraison"


        // Navigate to Pharmacy Loader Screen with pharmacy name as arg
        delivery_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.fromDeliveryConfirmationToDelivery1))

        delivery_cancel_command.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.fromDeliveryConfirmationToOrdonnance))

        delivery_order_items_list.adapter = OrderAdapter(requireActivity(), mOrders)

        var listParams = delivery_order_items_list.layoutParams
        listParams.height = (mOrders.size * 370) + (20 * mOrders.size)
        setUpTotal()
        delivery_price.text = "%.2f".format(getDeliveryPrice()) + "€"
    }

    /**
     * Delivery Price - calculated from total price
     */
    fun getDeliveryPrice(): Float {
        return getNotReductedPrice() * .15f
    }

    /**
     * Display Total price section
     */
    private fun setUpTotal() {
        // hide availability indicator
        val indicator = delivery_total_price.findViewById<View>(R.id.availability_indicator)
        indicator.visibility = View.INVISIBLE

        // Set up Titlte
        val title = delivery_total_price.findViewById<TextView>(R.id.order_item_title)
        title.text = "Total"
        title.textSize = 20f


        // Set up price
        val price = delivery_total_price.findViewById<TextView>(R.id.order_item_price)
        price.text = "%.2f".format(getNotReductedPrice()) + "€"

        // set up reduction
        val reduction = delivery_total_price.findViewById<TextView>(R.id.order_item_reduction)
        reduction.text = "- " + "%.2f".format(getTotalReduction()) + "€"

        // Set up total price
        val total = delivery_total_price.findViewById<TextView>(R.id.order_item_final_price)
        val totalPrice = getTotalPrice()
        total.text = "%.2f".format(totalPrice) + "€"
        total.textSize = 24f
    }

    /**
     * get Total price form order items list
     */
    private fun getTotalPrice(): Float {
        return getNotReductedPrice() + getDeliveryPrice() - getTotalReduction()
    }

    /**
     * Get Total reductions applied together
     */
    private fun getTotalReduction(): Float {
        var totalRed = 0f

        // add all reductions
        for (item in mOrders) {
            totalRed += item.mMedicament.getAvailableChemical().mReduction
        }

        return totalRed
    }

    /**
     * Get Order price before applying reductions
     */
    private fun getNotReductedPrice(): Float {
        var totalPrice = 0f

        // Add all prices
        for (item in mOrders) {
            totalPrice += item.mMedicament.getAvailableChemical().mPrice
        }

        return totalPrice
    }
}
