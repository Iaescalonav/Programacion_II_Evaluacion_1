package com.ejemplo.restaurant.model

import org.junit.Assert.*
import org.junit.Test

class ItemMenuTest {

    @Test
    fun crearItemMenu_valoresCorrectos() {
        val item = ItemMenu("Pastel de Choclo", "12000")
        assertEquals("Pastel de Choclo", item.nombre)
        assertEquals("12000", item.precio)
    }

    @Test
    fun crearItemMenu_cazuela() {
        val item = ItemMenu("Cazuela", "10000")
        assertEquals("Cazuela", item.nombre)
        assertEquals("10000", item.precio)
    }

    @Test
    fun itemMenu_esDataClass_equalsCorrectos() {
        val item1 = ItemMenu("Pastel de Choclo", "12000")
        val item2 = ItemMenu("Pastel de Choclo", "12000")
        assertEquals(item1, item2)
    }

    @Test
    fun itemMenu_distintos_noSonIguales() {
        val pastel = ItemMenu("Pastel de Choclo", "12000")
        val cazuela = ItemMenu("Cazuela", "10000")
        assertNotEquals(pastel, cazuela)
    }

    @Test
    fun itemMenu_precioConvertibleAInt() {
        val item = ItemMenu("Pastel de Choclo", "12000")
        assertEquals(12000, item.precio.toInt())
    }
}
