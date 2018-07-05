package com.example.interclassg20.cure.services

import com.example.interclassg20.cure.models.OrderItem

class OrderItemService {

    /**
     * Get A list of Order Items
     */
    public fun getOrderItems(): Array<OrderItem> {
        val medicamentSvc = MedicamentService()
        val medicaments = medicamentSvc.getMedicaments()

        return arrayOf(
                OrderItem (medicaments[0]),
                OrderItem (medicaments[1]),
                OrderItem (medicaments[2])
        )
    }
}