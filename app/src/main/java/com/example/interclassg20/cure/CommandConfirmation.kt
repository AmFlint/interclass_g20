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
import kotlinx.android.synthetic.main.fragment_command_confirmation.*
import org.w3c.dom.Text
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import com.example.interclassg20.cure.models.Medicament
import com.example.interclassg20.cure.models.OrderItem
import com.example.interclassg20.cure.services.MedicamentService
import com.example.interclassg20.cure.services.OrderItemService
import com.example.interclassg20.cure.adapters.OrderAdapter

/**
 * Command Confirmation Screen - Summary of cart (all chemicals) + price
 */
class CommandConfirmation : Fragment() {

    private var mOrders: Array<OrderItem>
    private var mDescription: String

    /**
     * Order Item service - interact with order items
     */
    private var mOrderItemService: OrderItemService

    init {
        mOrderItemService = OrderItemService()

        mOrders = mOrderItemService.getOrderItems()

        // Initialize Command description
        mDescription = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_command_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display Pharmacy Name from which user is ordering chemicals
        val pharmacyName = arguments!!.getString("pharmacy_name")
        command_confirmation_pharmacy_name.text = pharmacyName

        // Navigate to Pharmacy Loader Screen with pharmacy name as arg
        command_button.setOnClickListener({
            val bundle = Bundle()
            bundle.putString("pharmacy_name", pharmacyName)
            bundle.putString("pharmacy_address", arguments!!.getString("pharmacy_address"))
            it.findNavController().navigate(R.id.toCardLoader, bundle)
        })

        cancel_command.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.fromCmdToSearchPharmacy))

        order_items_list.adapter = OrderAdapter(requireActivity(), mOrders)

        var listParams = order_items_list.layoutParams
        listParams.height = (mOrders.size * 370) + (20 * mOrders.size)
        setUpTotal()

        requireActivity().setTitle(R.string.confirmation)
    }

    /**
     * Display Total price section
     */
    private fun setUpTotal() {
        // hide availability indicator
        val indicator = total_price.findViewById<View>(R.id.availability_indicator)
        indicator.visibility = View.INVISIBLE

        // Set up Titlte
        val title = total_price.findViewById<TextView>(R.id.order_item_title)
        title.text = "Total"
        title.textSize = 20f


        // Set up price
        val price = total_price.findViewById<TextView>(R.id.order_item_price)
        price.text = "%.2f".format(getNotReductedPrice()) + "€"

        // set up reduction
        val reduction = total_price.findViewById<TextView>(R.id.order_item_reduction)
        reduction.text = "- " + "%.2f".format(getTotalReduction()) + "€"

        // Set up total price
        val total = total_price.findViewById<TextView>(R.id.order_item_final_price)
        val totalPrice = getTotalPrice()
        total.text = "%.2f".format(totalPrice) + "€"
        total.textSize = 24f
    }

    /**
     * get Total price form order items list
     */
    private fun getTotalPrice(): Float {
        var totalPrice = 0f

        // loop on items to get total price
        return getNotReductedPrice() - getTotalReduction()
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
            totalPrice += item.mMedicament.mPrice
        }

        return totalPrice
    }
}
