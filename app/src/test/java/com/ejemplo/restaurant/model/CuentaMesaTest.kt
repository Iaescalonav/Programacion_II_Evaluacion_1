package com.ejemplo.restaurant.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CuentaMesaTest {

    private lateinit var cuenta: CuentaMesa
    private lateinit var itemPastel: ItemMesa
    private lateinit var itemCazuela: ItemMesa

    @Before
    fun setUp() {
        cuenta = CuentaMesa(1)
        itemPastel = ItemMesa(ItemMenu("Pastel de Choclo", "12000"), 0)
        itemCazuela = ItemMesa(ItemMenu("Cazuela", "10000"), 0)
    }

    // ===== TESTS: agregarItem =====

    @Test
    fun agregarItem_listaInicialmenteVacia() {
        assertEquals(0, cuenta.calcularTotalSinPropina())
    }

    @Test
    fun agregarItem_unItem() {
        itemPastel.cantidad = 2
        cuenta.agregarItem(itemPastel)
        assertEquals(24000, cuenta.calcularTotalSinPropina())
    }

    @Test
    fun agregarItem_dosItems() {
        itemPastel.cantidad = 2
        itemCazuela.cantidad = 3
        cuenta.agregarItem(itemPastel)
        cuenta.agregarItem(itemCazuela)
        assertEquals(54000, cuenta.calcularTotalSinPropina())
    }

    // ===== TESTS: limpiarItems =====

    @Test
    fun limpiarItems_reseteoCompleto() {
        itemPastel.cantidad = 2
        cuenta.agregarItem(itemPastel)
        assertEquals(24000, cuenta.calcularTotalSinPropina())

        cuenta.limpiarItems()
        assertEquals(0, cuenta.calcularTotalSinPropina())
    }

    @Test
    fun limpiarItems_puedeReagregar() {
        itemPastel.cantidad = 2
        cuenta.agregarItem(itemPastel)
        cuenta.limpiarItems()

        itemPastel.cantidad = 5
        cuenta.agregarItem(itemPastel)
        assertEquals(60000, cuenta.calcularTotalSinPropina())
    }

    // ===== TESTS: calcularTotalSinPropina =====

    @Test
    fun calcularTotalSinPropina_sinItems() {
        assertEquals(0, cuenta.calcularTotalSinPropina())
    }

    @Test
    fun calcularTotalSinPropina_cantidadesCero() {
        cuenta.agregarItem(itemPastel)
        cuenta.agregarItem(itemCazuela)
        assertEquals(0, cuenta.calcularTotalSinPropina())
    }

    @Test
    fun calcularTotalSinPropina_pedidoNormal() {
        itemPastel.cantidad = 1
        itemCazuela.cantidad = 1
        cuenta.agregarItem(itemPastel)
        cuenta.agregarItem(itemCazuela)
        assertEquals(22000, cuenta.calcularTotalSinPropina())
    }

    // ===== TESTS: calcularPropina =====

    @Test
    fun calcularPropina_activadaPorDefecto() {
        assertTrue(cuenta.aceptaPropina)
    }

    @Test
    fun calcularPropina_10Porciento() {
        itemPastel.cantidad = 2
        itemCazuela.cantidad = 3
        cuenta.agregarItem(itemPastel)
        cuenta.agregarItem(itemCazuela)
        // Total: 54000, Propina 10%: 5400
        assertEquals(5400, cuenta.calcularPropina())
    }

    @Test
    fun calcularPropina_desactivada() {
        itemPastel.cantidad = 2
        cuenta.agregarItem(itemPastel)
        cuenta.aceptaPropina = false
        assertEquals(0, cuenta.calcularPropina())
    }

    @Test
    fun calcularPropina_sinItems_esCero() {
        assertEquals(0, cuenta.calcularPropina())
    }

    @Test
    fun calcularPropina_montoChico() {
        itemCazuela.cantidad = 1
        cuenta.agregarItem(itemCazuela)
        // 10000 * 0.10 = 1000
        assertEquals(1000, cuenta.calcularPropina())
    }

    @Test
    fun calcularPropina_truncamiento() {
        // Verificar que (total * 0.10).toInt() no produce errores
        // Con precios múltiplos de 1000, siempre es exacto
        itemPastel.cantidad = 1
        cuenta.agregarItem(itemPastel)
        // 12000 * 0.10 = 1200.0 → 1200
        assertEquals(1200, cuenta.calcularPropina())
    }

    // ===== TESTS: calcularTotalConPropina =====

    @Test
    fun calcularTotalConPropina_conPropina() {
        itemPastel.cantidad = 2
        itemCazuela.cantidad = 3
        cuenta.agregarItem(itemPastel)
        cuenta.agregarItem(itemCazuela)
        // 54000 + 5400 = 59400
        assertEquals(59400, cuenta.calcularTotalConPropina())
    }

    @Test
    fun calcularTotalConPropina_sinPropina() {
        itemPastel.cantidad = 2
        itemCazuela.cantidad = 3
        cuenta.agregarItem(itemPastel)
        cuenta.agregarItem(itemCazuela)
        cuenta.aceptaPropina = false
        assertEquals(54000, cuenta.calcularTotalConPropina())
    }

    @Test
    fun calcularTotalConPropina_totalIgualSinPropinaMasPropina() {
        itemPastel.cantidad = 3
        cuenta.agregarItem(itemPastel)
        val totalSin = cuenta.calcularTotalSinPropina()
        val propina = cuenta.calcularPropina()
        val totalCon = cuenta.calcularTotalConPropina()
        assertEquals(totalSin + propina, totalCon)
    }

    @Test
    fun calcularTotalConPropina_togglePropinaRecalcula() {
        itemPastel.cantidad = 2
        cuenta.agregarItem(itemPastel)

        cuenta.aceptaPropina = true
        val conPropina = cuenta.calcularTotalConPropina()

        cuenta.aceptaPropina = false
        val sinPropina = cuenta.calcularTotalConPropina()

        assertTrue(conPropina > sinPropina)
        assertEquals(24000, sinPropina)
        assertEquals(26400, conPropina)
    }

    // ===== TESTS: atributo mesa =====

    @Test
    fun mesa_valorCorrecto() {
        assertEquals(1, cuenta.mesa)
    }

    @Test
    fun mesa_distintasInstancias() {
        val mesa2 = CuentaMesa(2)
        assertNotEquals(cuenta.mesa, mesa2.mesa)
    }

    // ===== TESTS: simulación patrón reactivo (TextWatcher) =====

    @Test
    fun patronReactivo_limpiarYReagregar_sinAcumulacion() {
        // Simula 10 invocaciones del TextWatcher
        for (i in 1..10) {
            itemPastel.cantidad = 2
            itemCazuela.cantidad = 3
            cuenta.limpiarItems()
            cuenta.agregarItem(itemPastel)
            cuenta.agregarItem(itemCazuela)
        }
        // Tras 10 ciclos, el total debe ser el mismo que tras 1 ciclo
        assertEquals(54000, cuenta.calcularTotalSinPropina())
    }

    @Test
    fun patronReactivo_sinLimpiar_acumulaDuplicados() {
        // Demuestra el bug que se corrigió: sin limpiarItems(), se acumulan
        val cuentaBuggy = CuentaMesa(99)
        val item = ItemMesa(ItemMenu("Test", "10000"), 1)

        cuentaBuggy.agregarItem(item)
        assertEquals(10000, cuentaBuggy.calcularTotalSinPropina())

        cuentaBuggy.agregarItem(item)
        // Sin limpiar, ahora tiene 2 refs al mismo item = total duplicado
        assertEquals(20000, cuentaBuggy.calcularTotalSinPropina())
    }

    // ===== TESTS DE RENDIMIENTO =====

    @Test
    fun rendimiento_cicloCompletoCalculo() {
        itemPastel.cantidad = 5
        itemCazuela.cantidad = 3
        cuenta.agregarItem(itemPastel)
        cuenta.agregarItem(itemCazuela)

        val inicio = System.nanoTime()
        for (i in 1..10000) {
            cuenta.limpiarItems()
            cuenta.agregarItem(itemPastel)
            cuenta.agregarItem(itemCazuela)
            cuenta.calcularTotalSinPropina()
            cuenta.calcularPropina()
            cuenta.calcularTotalConPropina()
        }
        val duracion = (System.nanoTime() - inicio) / 1_000_000.0
        println("RENDIMIENTO: 10000 ciclos completos en ${duracion}ms")
        assertTrue("Debe completar en menos de 500ms", duracion < 500)
    }

    @Test
    fun rendimiento_togglePropina() {
        itemPastel.cantidad = 3
        cuenta.agregarItem(itemPastel)

        val inicio = System.nanoTime()
        for (i in 1..10000) {
            cuenta.aceptaPropina = (i % 2 == 0)
            cuenta.calcularTotalConPropina()
        }
        val duracion = (System.nanoTime() - inicio) / 1_000_000.0
        println("RENDIMIENTO: 10000 toggles propina en ${duracion}ms")
        assertTrue("Debe completar en menos de 200ms", duracion < 200)
    }
}
