package com.ejemplo.restaurant

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ejemplo.restaurant.model.CuentaMesa
import com.ejemplo.restaurant.model.ItemMenu
import com.ejemplo.restaurant.model.ItemMesa
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Vistas de la UI
    private lateinit var etCantidadPastel: EditText
    private lateinit var etCantidadCazuela: EditText
    private lateinit var tvSubtotalPastel: TextView
    private lateinit var tvSubtotalCazuela: TextView
    private lateinit var tvTotalSinPropina: TextView
    private lateinit var tvPropina: TextView
    private lateinit var tvTotalConPropina: TextView
    private lateinit var swPropina: Switch
    private lateinit var btnBorrarPedido: ImageButton

    // Modelo de dominio
    private val pastelDeChoclo = ItemMenu("Pastel de Choclo", "12000")
    private val cazuela = ItemMenu("Cazuela", "10000")
    private val itemPastel = ItemMesa(pastelDeChoclo, 0)
    private val itemCazuela = ItemMesa(cazuela, 0)
    private val cuentaMesa = CuentaMesa(1)

    // Formateador de moneda chilena
    private val formatoCLP: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()
        configurarListeners()
        actualizarTotales()
    }

    // Enlaza cada variable con su vista del layout
    private fun inicializarVistas() {
        etCantidadPastel = findViewById(R.id.etCantidadPastel)
        etCantidadCazuela = findViewById(R.id.etCantidadCazuela)
        tvSubtotalPastel = findViewById(R.id.tvSubtotalPastel)
        tvSubtotalCazuela = findViewById(R.id.tvSubtotalCazuela)
        tvTotalSinPropina = findViewById(R.id.tvTotalSinPropina)
        tvPropina = findViewById(R.id.tvPropina)
        tvTotalConPropina = findViewById(R.id.tvTotalConPropina)
        swPropina = findViewById(R.id.swPropina)
        btnBorrarPedido = findViewById(R.id.btnBorrarPedido)
    }

    // Configura TextWatcher en ambos EditText y listener en el Switch
    private fun configurarListeners() {
        etCantidadPastel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val cantidad = s.toString().toIntOrNull() ?: 0
                itemPastel.cantidad = cantidad
                actualizarTotales()
            }
        })

        etCantidadCazuela.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val cantidad = s.toString().toIntOrNull() ?: 0
                itemCazuela.cantidad = cantidad
                actualizarTotales()
            }
        })

        swPropina.setOnCheckedChangeListener { _, isChecked ->
            cuentaMesa.aceptaPropina = isChecked
            actualizarTotales()
        }

        btnBorrarPedido.setOnClickListener {
            mostrarDialogoBorrar()
        }
    }

    // Muestra un diálogo de confirmación antes de borrar el pedido
    private fun mostrarDialogoBorrar() {
        AlertDialog.Builder(this)
            .setTitle("Borrar pedido")
            .setMessage("¿Está seguro que desea borrar el pedido actual?")
            .setPositiveButton("Sí") { _, _ ->
                borrarPedido()
            }
            .setNegativeButton("No", null)
            .show()
    }

    // Reinicia todos los campos y el modelo a su estado inicial
    private fun borrarPedido() {
        etCantidadPastel.setText("")
        etCantidadCazuela.setText("")
        swPropina.isChecked = true
    }

    // Recalcula todos los montos y actualiza las vistas con formato CLP
    private fun actualizarTotales() {
        val subtotalPastel = itemPastel.calcularSubtotal()
        val subtotalCazuela = itemCazuela.calcularSubtotal()

        tvSubtotalPastel.text = "Subtotal: ${formatoCLP.format(subtotalPastel)}"
        tvSubtotalCazuela.text = "Subtotal: ${formatoCLP.format(subtotalCazuela)}"

        // Limpiar y re-agregar items para evitar duplicados en la lista
        cuentaMesa.limpiarItems()
        cuentaMesa.agregarItem(itemPastel)
        cuentaMesa.agregarItem(itemCazuela)

        val totalSinPropina = cuentaMesa.calcularTotalSinPropina()
        val propina = cuentaMesa.calcularPropina()
        val totalConPropina = cuentaMesa.calcularTotalConPropina()

        tvTotalSinPropina.text = "Total sin propina: ${formatoCLP.format(totalSinPropina)}"
        tvPropina.text = "Propina: ${formatoCLP.format(propina)}"
        tvTotalConPropina.text = "TOTAL: ${formatoCLP.format(totalConPropina)}"
    }
}
