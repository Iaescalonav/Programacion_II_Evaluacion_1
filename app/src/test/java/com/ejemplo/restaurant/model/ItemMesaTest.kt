package com.ejemplo.restaurant.model

import org.junit.Assert.*
import org.junit.Test

class ItemMesaTest {

    private val pastel = ItemMenu("Pastel de Choclo", "12000")
    private val cazuela = ItemMenu("Cazuela", "10000")

    // ===== TESTS FUNCIONALES =====

    @Test
    fun calcularSubtotal_cantidadCero() {
        val item = ItemMesa(pastel, 0)
        assertEquals(0, item.calcularSubtotal())
    }

    @Test
    fun calcularSubtotal_cantidadUno() {
        val item = ItemMesa(pastel, 1)
        assertEquals(12000, item.calcularSubtotal())
    }

    @Test
    fun calcularSubtotal_cantidadMultiple() {
        val item = ItemMesa(pastel, 5)
        assertEquals(60000, item.calcularSubtotal())
    }

    @Test
    fun calcularSubtotal_cazuela() {
        val item = ItemMesa(cazuela, 3)
        assertEquals(30000, item.calcularSubtotal())
    }

    @Test
    fun calcularSubtotal_cantidadGrande() {
        val item = ItemMesa(pastel, 100)
        assertEquals(1200000, item.calcularSubtotal())
    }

    @Test
    fun calcularSubtotal_actualizaCantidad() {
        val item = ItemMesa(pastel, 2)
        assertEquals(24000, item.calcularSubtotal())
        item.cantidad = 5
        assertEquals(60000, item.calcularSubtotal())
    }

    @Test
    fun calcularSubtotal_cantidadCambiaACero() {
        val item = ItemMesa(cazuela, 3)
        assertEquals(30000, item.calcularSubtotal())
        item.cantidad = 0
        assertEquals(0, item.calcularSubtotal())
    }

    // ===== TESTS DE BORDE =====

    @Test
    fun calcularSubtotal_cantidadNegativa() {
        // Caso borde: la UI no permite negativos (inputType=number)
        // pero el modelo no lo restringe — documenta comportamiento
        val item = ItemMesa(pastel, -1)
        assertEquals(-12000, item.calcularSubtotal())
    }

    @Test
    fun calcularSubtotal_cantidadMuyGrande_noOverflow() {
        // Verificar que no hay overflow con cantidades razonables
        val item = ItemMesa(pastel, 10000)
        assertEquals(120000000, item.calcularSubtotal())
        assertTrue(item.calcularSubtotal() > 0)
    }

    // ===== TESTS DE RENDIMIENTO =====

    @Test
    fun rendimiento_calculoSubtotal_1000Iteraciones() {
        val item = ItemMesa(pastel, 5)
        val inicio = System.nanoTime()
        for (i in 1..1000) {
            item.calcularSubtotal()
        }
        val duracion = (System.nanoTime() - inicio) / 1_000_000.0
        println("RENDIMIENTO: 1000 calcularSubtotal() en ${duracion}ms")
        assertTrue("Debe completar en menos de 100ms", duracion < 100)
    }
}
