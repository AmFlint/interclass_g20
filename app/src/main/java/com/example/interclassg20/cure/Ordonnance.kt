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
import kotlinx.android.synthetic.main.fragment_consultation_history.*
import kotlinx.android.synthetic.main.fragment_ordonnance.*
import kotlinx.android.synthetic.main.fragment_test.*

class Ordonnance : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ordonnance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delivery_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCardLoader))
        pharmacy_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toDeliveryLoader))

        val medicaments: Array<Medicament> = arrayOf(
                Medicament ("Pravastine", 20, "mg", "3 comprimés par jours", 3.50f, "tomorrow", true, 0f),
                Medicament ("Paracétamol", 2, "g", "1 comprimé le soir", 4.99f, "tommorow", false, 0f)
        )

        medical_list.adapter = MedicamentAdapter(requireActivity(), medicaments)

        commandActions.visibility = View.INVISIBLE

        command_btn.setOnClickListener(View.OnClickListener {
            commandActions.visibility = View.VISIBLE
        })

//        val actionBar = requireActivity().actionBar
////        actionBar.title = R.string.ordonnance_name.toString()
        requireActivity().setTitle(R.string.ordonnance_name)
    }

    /**
     * Adapter for Medicament List
     */
    private class MedicamentAdapter(context: Context, medicaments: Array<Medicament>): BaseAdapter() {
        private val mContext: Context
        private val mMedicaments: Array<Medicament>

        init {
            // assign given context (Activity) to adapter's context
            mContext = context
            mMedicaments = medicaments
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Any {
            return "test"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
            val medicament = mMedicaments[position]
            // Initialize Text View for row informations
            val layoutInflater = LayoutInflater.from(mContext)
            val row = layoutInflater.inflate(R.layout.medicament_row, viewGroup, false)

            val informationsTxt = row.findViewById<TextView>(R.id.informations)
            informationsTxt.text = "- " + medicament.getInformations()

            val treatmentTxt = row.findViewById<TextView>(R.id.treament)
            treatmentTxt.text = medicament.getTreatment()


            return row
        }

    }
}

class Medicament(
        name: String,
        dose: Int,
        indice: String,
        treatment: String,
        price: Float,
        approvDate: String,
        availability: Boolean,
        reduction: Float)
{
    /**
     * Name of Chemical
     */
    val mName: String

    /**
     * Dose - Quantity in Chemical package
     */
    val mDose: Int

    /**
     * Unit indicator - g/mg
     */
    val mIndice: String

    /**
     * Treament description (e.g. "1 comprimé le soir")
     */
    val mTreament: String

    /**
     * Price of given chemical for 1 qty
     */
    val mPrice: Float

    /**
     * Approvisionning date
     */
    val mApprovDate: String

    /**
     * Reduction applied to chemical
     */
    val mReduction: Float

    /**
     *
     */
    var mReplacement: Medicament?

    /**
     *
     */
    val mAvailability: Boolean

    init {
        mName = name
        mDose = dose
        mIndice  = indice
        mTreament = treatment
        mPrice = price
        mApprovDate = approvDate
        mAvailability = availability
        mReduction = reduction
        mReplacement = null
    }

    /**
     * Get informations about a given Chemical - Name, dose and unit indicator
     */
    fun getInformations(): String {
        return "$mName $mDose$mIndice"
    }

    /**
     * Get Treatment as a string
     */
    fun getTreatment(): String {
        return ": $mTreament"
    }

    /**
     * Get Available chemical - Return replacement chemical if not available
     */
    fun getAvailableChemical(): Medicament {
        if (!mAvailability && mReplacement !== null) {
            return mReplacement as Medicament
        }
        return this
    }

    /**
     * Get Total price of an order item -> Apply reduction to medicament price
     */
    fun getTotalPrice(): Float {
        val price = mPrice - mReduction
        return price
    }
}
