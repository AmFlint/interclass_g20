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

/**
 * Command Confirmation Screen - Summary of cart (all chemicals) + price
 */
class DeliveryConfirmation : Fragment() {

    private var mOrders: Array<OrderItem>
    private var mDescription: String

    init {
        val medicaments: Array<Medicament> = arrayOf(
                Medicament ("Pravastine", 20, "mg", "3 comprimés par jours", 3.50f, "2 jours", true, 3.50f),
                Medicament ("Paracétamol", 2, "g", "1 comprimé le soir", 4.99f, "1 jour", false, 0f),
                Medicament ("Antibiotique", 4, "g", "2 comprimé le soir", 12.99f, "", false, 0f)
        )

        medicaments[2].mReplacement = Medicament("Doliprane", 500, "mg", "2 comprimés par jours", 6.99f, "2 jours", true, 5.50f)

        mOrders =  arrayOf(
                OrderItem (medicaments[0]),
                OrderItem (medicaments[1]),
                OrderItem (medicaments[2])
        )
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

        delivery_cancel_command.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.fromCmdToSearchPharmacy))

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
}
