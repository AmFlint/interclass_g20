package com.example.interclassg20.cure.models

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
