package com.example.interclassg20.cure.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.interclassg20.cure.R
import com.example.interclassg20.cure.models.Medicament
import com.example.interclassg20.cure.models.OrderItem

/**
 * Order Adapter - ListView adapter for order items (chemical drugs)
 */
class OrderAdapter(context: Context, orderItems: Array<OrderItem>): BaseAdapter() {
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
        val indicatorColor = getIndicatorColor(item.mMedicament.mAvailability, item.mMedicament.mApprovDate)
        indicator.setBackgroundColor(indicatorColor)

        // description
        val description = row.findViewById<TextView>(R.id.order_item_description)

        // title
        val title = row.findViewById<TextView>(R.id.order_item_title)

        // price
        val itemPrice = row.findViewById<TextView>(R.id.order_item_price)

        // reduction
        val reduction = row.findViewById<TextView>(R.id.order_item_reduction)


        // total price
        val total = row.findViewById<TextView>(R.id.order_item_final_price)



        // Check chemical's availability -> if not available but replacement is, display the replacement to user
        val replacement: Medicament = item.mMedicament.getAvailableChemical()
        if (replacement.mName !== item.mMedicament.mName) {
            title.text = replacement.getInformations() + " (équivalent)"
            description.text = "${item.mMedicament.mName} n'est pas disponible malheureusement"
            total.text = formatFloat(replacement.getTotalPrice()) + "€"
            reduction.text = "- " + formatFloat(item.getReduction()) + "€"
            itemPrice.text = formatFloat(item.getPrice()) + "€"
        } else {
            title.text = item.mMedicament.getInformations()
            description.text = getItemDescription(item.mMedicament)
            total.text = formatFloat(item.getTotalPrice()) + "€"
            reduction.text = "- " + formatFloat(item.getReduction()) + "€"
            itemPrice.text = formatFloat(item.mMedicament.mPrice) + "€"
        }


        return row
    }

    /**
     * Get Background color from item's availability
     */
    private fun getIndicatorColor(availabilty: Boolean, approvDate: String): Int {
        var indicatorColor = ""
        if (availabilty) {
            indicatorColor = "#0bb07b"
        } else if (!availabilty && (approvDate.length > 0)) {
            indicatorColor = "#f8e71c"
        } else {
            indicatorColor = "#f56969"
        }
        return Color.parseColor(indicatorColor)
    }

    private fun getItemDescription(medicament: Medicament): String {
        if (medicament.mAvailability) {
            return "En Stock"
        } else if (!medicament.mAvailability && medicament.mReplacement !== null) {
            return "Remplacé par un autre médicament car l'orginal n'est pas en stock"
        } else {
            return "Disponible sous ${medicament.mApprovDate}"
        }

        return "Non Disponible dans cette pharmacie"
    }

    private fun formatFloat(nb: Float): String {
        return "%.2f".format(nb)
    }
}