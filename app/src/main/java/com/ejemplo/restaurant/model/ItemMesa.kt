package com.ejemplo.restaurant.model

// Representa un platillo agregado a la mesa con su cantidad
data class ItemMesa(
    val itemMenu: ItemMenu,
    var cantidad: Int
) {
    // Calcula el subtotal multiplicando precio unitario por cantidad
    fun calcularSubtotal(): Int {
        return itemMenu.precio.toInt() * cantidad
    }
}
