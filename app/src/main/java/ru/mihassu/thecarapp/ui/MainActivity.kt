package ru.mihassu.thecarapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import ru.mihassu.thecarapp.R

class MainActivity : AppCompatActivity(), FragmentActionListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    var homeButtonAction: (() -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setImageResource(R.drawable.ic_add_black_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_select_car -> SelectCarActivity.start(this).let { true }
            16908332 -> homeButtonAction?.invoke() ?: true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setFabListener(action: () -> Unit) {
        if (!fab.isVisible) {
            fab.show()
        }
        fab.setOnClickListener{ action.invoke() }
    }

    override fun hideFab() {
        fab.hide()
    }

    override fun showFab() {
        fab.show()
    }

    override fun setHomeAction(isShow: Boolean, action: () -> Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isShow)
        homeButtonAction = action
    }

}
