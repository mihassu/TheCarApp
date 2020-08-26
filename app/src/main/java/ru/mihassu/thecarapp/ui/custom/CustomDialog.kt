package ru.mihassu.thecarapp.ui.custom

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.extensions.LayoutContainer
import ru.mihassu.thecarapp.R

class CustomDialog(override val containerView: View?) : LayoutContainer {

    fun getDialog(context: Context, dialogView: View) = AlertDialog.Builder(context)
    .setTitle(context.resources.getString(R.string.add_note))
    .setView(dialogView)
    .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, which ->


        dialog.dismiss()

    }
    .setNegativeButton(context.resources.getString(R.string.cancel)){ dialog, which ->

        dialog.dismiss()}
    .create()
}

