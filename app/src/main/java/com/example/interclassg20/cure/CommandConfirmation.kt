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
import kotlinx.android.synthetic.main.fragment_command_confirmation.*
import org.w3c.dom.Text

class CommandConfirmation : Fragment() {

    private var mOrders: Array<OrderItem>

    init {
        val medicaments: Array<Medicament> = arrayOf(
                Medicament ("Pravastine", 20, "mg", "3 comprimés par jours", 3.50f),
                Medicament ("Paracétamol", 2, "g", "1 comprimé le soir", 4.99f),
                Medicament ("Antibiotique", 4, "g", "2 comprimé le soir", 12.99f)
        )

        mOrders =  arrayOf(
                OrderItem (medicaments[0], 3.50f, true),
                OrderItem (medicaments[1], 0f, false),
                OrderItem (medicaments[2], 0f, true)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_command_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        order_items_list.adapter = OrderAdapter(requireActivity(), mOrders)

        var listParams = order_items_list.layoutParams
        listParams.height = (mOrders.size * 370) + (20 * mOrders.size)
        setUpTotal()
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
        price.text = "63.33€"

        // set up reduction
        val reduction = total_price.findViewById<TextView>(R.id.order_item_reduction)
        reduction.text = "- 52€"

        // Set up total price
        val total = total_price.findViewById<TextView>(R.id.order_item_final_price)
        val totalPrice = getTotalPrice()
        total.text = totalPrice.toString() + "€"
        total.textSize = 24f
    }

    /**
     * get Total price form order items list
     */
    private fun getTotalPrice(): Float {
        var totalPrice = 0f

        // loop on items to get total price
        for (item in mOrders) {
            totalPrice += item.getTotalPrice()
        }

        return totalPrice
    }

    /**
     * Order Adapter - ListView adapter for order items (chemical drugs)
     */
    private class OrderAdapter(context: Context, orderItems: Array<OrderItem>): BaseAdapter() {
        private val mContext: Context
        private val mOrders: Array<OrderItem>

        init {
            mContext = context
            mOrders = orderItems
        }

        override fun getCount(): Int {
            return mOrders.size
        }

        override fun getItem(position: Int): Any {
            return "test"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        /**
         * Create row Display for list Item
         */
        override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val item = mOrders[position]

            val row = layoutInflater.inflate(R.layout.order_item_row, viewGroup, false)

            // set up informations to display

            // Availability Indicator
            val indicator = row.findViewById<View>(R.id.availability_indicator)
            val indicatorColor = getIndicatorColor(item.mAvailability)
            indicator.setBackgroundColor(indicatorColor)

            // title
            val title = row.findViewById<TextView>(R.id.order_item_title)
            title.text = item.mMedicament.getInformations()

            // price
            val itemPrice = row.findViewById<TextView>(R.id.order_item_price)
            itemPrice.text = item.mMedicament.mPrice.toString() + "€"

            // reduction
            val reduction = row.findViewById<TextView>(R.id.order_item_reduction)
            reduction.text = item.mReduction.toString() + "€"

            // total price
            val total = row.findViewById<TextView>(R.id.order_item_final_price)
            total.text = item.getTotalPrice().toString() + "€"

            return row
        }

        /**
         * Get Background color from item's availability
         */
        private fun getIndicatorColor(availabilty: Boolean): Int {
            var indicatorColor = ""
            when (availabilty) {
                true -> indicatorColor = "#0bb07b"
                false -> indicatorColor = "#f56969"

            }
            return Color.parseColor(indicatorColor)
        }

    }
}

/**
 * Order Item Object
 */
class OrderItem(medicament: Medicament, reduction: Float, availability: Boolean) {
    val mMedicament: Medicament
    val mReduction: Float
    val mAvailability: Boolean

    init {
        mReduction = reduction
        mMedicament = medicament
        mAvailability = availability
    }

    /**
     * Get Total price of an order item -> Apply reduction to medicament price
     */
    fun getTotalPrice(): Float {
        val price = mMedicament.mPrice - mReduction
        return price
    }
}
