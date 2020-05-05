package com.david.redcristianauno.model.network

import java.lang.Exception

interface Callback <T>{

    fun OnSucces(result: T?)

    fun onFailure(exception: Exception)
}