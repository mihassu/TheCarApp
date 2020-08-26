package ru.mihassu.thecarapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_select_car.*
import ru.mihassu.thecarapp.R

class SelectCarActivity : AppCompatActivity() {

    companion object {
        val CURRENT_CAR_ID = "current_car"

        fun start(context: Context) {
            val intent = Intent(context, SelectCarActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_car)

        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOf("car1", "car3", "car3")).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner_cars.apply { adapter = spinnerAdapter }

        btn_select.setOnClickListener { MainActivity.start(this) }
    }


}

