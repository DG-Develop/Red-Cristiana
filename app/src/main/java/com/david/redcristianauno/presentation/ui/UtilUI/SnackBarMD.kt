package com.david.redcristianauno.presentation.ui.UtilUI

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarMD {

    fun getSBIndefinite(view: View, message: String){
        return Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Cerrar"){

            }.show()
    }

    fun getSBNormal(view: View, message: String){
        return Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }


}