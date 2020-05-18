package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.fragment_biblie.*

class BiblieFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_biblie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wvBiblieFragment.webViewClient = WebViewClient()
        wvBiblieFragment.loadUrl("http://www.iglesiabautistarecoleta.cl/Biblia_texto_y_audio/indice_general.htm")
    }
}