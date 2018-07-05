package com.example.interclassg20.cure.services
import com.example.interclassg20.cure.models.Medicament

class MedicamentService {

    /**
     * get Array of Medicament Object
     */
    public fun getMedicaments(): Array<Medicament> {
        val medicaments: Array<Medicament> = arrayOf(
                Medicament ("Pravastine", 20, "mg", "3 comprimés par jours", 3.50f, "2 jours", true, 3.50f),
                Medicament ("Paracétamol", 2, "g", "1 comprimé le soir", 4.99f, "1 jour", false, 0f),
                Medicament ("Antibiotique", 4, "g", "2 comprimé le soir", 12.99f, "", false, 0f)
        )

        medicaments[2].mReplacement = Medicament("Doliprane", 500, "mg", "2 comprimés par jours", 6.99f, "2 jours", true, 5.50f)

        return medicaments
    }
}