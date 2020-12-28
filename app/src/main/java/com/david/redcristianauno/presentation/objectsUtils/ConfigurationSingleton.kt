package com.david.redcristianauno.presentation.objectsUtils

object ConfigurationSingleton {
    private var config: List<String>? = null

    fun setConfig(config: List<String>?){
        this.config = config
    }

    fun getConfig(): List<String>? = config
}