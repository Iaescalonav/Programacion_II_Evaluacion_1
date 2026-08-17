package com.ejemplo.restaurant.model

// Gestiona la cuenta completa de una mesa, incluyendo items y propina
class CuentaMesa(val mesa: Int) {

    private val _items: MutableList<ItemMesa> = mutableListOf()
    var aceptaPropina: Boolean = true

    // Agrega un item a la cuenta de la mesa
    fun agregarItem(item: ItemMesa) {
        _items.add(item)
    }

    // Limpia la lista de items para recalcular desde cero
    fun limpiarItems() {
        _items.clear()
    }

    // Suma los subtotales de todos los items de la mesa
    fun calcularTotalSinPropina(): Int {
        var total = 0
        for (item in _items) {
            total += item.calcularSubtotal()
        }
        return total
    }

    // Calcula la propina del 10% si está habilitada
    fun calcularPropina(): Int {
        return if (aceptaPropina) {
            (calcularTotalSinPropina() * 0.10).toInt()
        } else {
            0
        }
    }

    // Retorna el total final incluyendo propina si corresponde
    fun calcularTotalConPropina(): Int {
        return calcularTotalSinPropina() + calcularPropina()
    }
}
