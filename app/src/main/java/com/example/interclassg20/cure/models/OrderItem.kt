package com.example.interclassg20.cure.models

/**
 * Order Item Object
 */
class OrderItem(medicament: Medicament) {
    val mMedicament: Medicament

    init {
        mMedicament = medicament
    }

    fun getTotalPrice(): Float {
        return mMedicament.getAvailableChemical().getTotalPrice()
    }

    fun getReduction(): Float {
        return mMedicament.getAvailableChemical().mReduction
    }

    fun getPrice(): Float {
        return mMedicament.getAvailableChemical().mPrice
    }
}