package com.example.currencyconverterapp

import android.content.Intent
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private val egyptianPound = "Egyptian Pound"
    private val americanDollar = "American Dollar"
    private val AED = "AED"
    private val GBP = "GBP"
    private val Euro = "Euro"

    val values = mapOf(
        americanDollar to 1.0,
        egyptianPound to 19.44,
        AED to 3.672,
        GBP to 0.925,
        Euro to 1.038
    )

    lateinit var convert :Button
    lateinit var amountET :TextInputEditText
    lateinit var resultET :TextInputEditText
    lateinit var toAC :AutoCompleteTextView
    lateinit var fromAC :AutoCompleteTextView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        populateDropDownMenu()
        toolbar.inflateMenu(R.menu.options_menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.share_action){

                val message = "${amountET.text.toString()} ${fromAC.text} is equal to ${resultET.text.toString()} ${toAC.text}"

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, message)

                if (shareIntent.resolveActivity(packageManager) != null){
                    startActivity(shareIntent)
                }else{
                    Toast.makeText(this, "No Activity Found to Handle this intent", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }

        amountET.addTextChangedListener {
            calculateResult()
        }

        fromAC.setOnItemClickListener { adapterView, view, i, l -> calculateResult() }
        toAC.setOnItemClickListener { adapterView, view, i, l -> calculateResult() }
    }

    private fun calculateResult(){
        if(amountET.text!!.isNotEmpty()) {
            val amount = amountET.text.toString().toDouble()
            val toValue = values[toAC.text.toString()]
            val fromValue = values[fromAC.text.toString()]
            val result = amount.times(toValue!!.div(fromValue!!))
            val formattedResult = String.format("%.2f", result)
            resultET.setText(formattedResult)
        }else{
            amountET.setError("Amount Is Required")
        }
    }

    private fun initializeViews(){
        convert = findViewById(R.id.convert_button)
        amountET = findViewById(R.id.editT)
        resultET = findViewById(R.id.result)
        toAC = findViewById(R.id.to)
        fromAC = findViewById(R.id.From_ac)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun populateDropDownMenu(){
        val listOfCountry = listOf(egyptianPound, americanDollar, AED, GBP, Euro)
        val adapter = ArrayAdapter(this, R.layout.drop_down_list_item, listOfCountry)
        toAC.setAdapter(adapter)
        fromAC.setAdapter(adapter)
    }
}