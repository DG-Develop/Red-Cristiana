package com.david.redcristianauno.presentation.ui.UtilUI

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

object SnackBarMD {

    fun getSBIndefinite(view: View, message: String){
        return Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Cerrar"){

            }.show()
    }


    fun showSBWithMargin(snackbar: Snackbar, side: Int, bottom: Int){
        val snackBarView = snackbar.view

        val params: CoordinatorLayout.LayoutParams = snackBarView.layoutParams as CoordinatorLayout.LayoutParams

        params.setMargins(params.leftMargin + side, params.topMargin, params.rightMargin + side, params.bottomMargin + bottom)

        snackBarView.layoutParams = params

        snackbar.show()
    }
}