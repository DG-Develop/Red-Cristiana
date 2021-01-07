package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.david.redcristianauno.R
import com.david.redcristianauno.application.AppConstants.CAPTURE_USER_FRAGMENT
import com.david.redcristianauno.presentation.viewmodel.CaptureUserViewModel
import com.david.redcristianauno.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_capture_user.*

@AndroidEntryPoint
class CaptureUserFragment : DialogFragment() {

    private val captureViewModel by viewModels<CaptureUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarCaptureUser.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarCaptureUser.setNavigationOnClickListener {
            dismiss()
        }

      /*  filled_exposed_dropdown_capture
        fab_send_capture*/

        captureViewModel.getCells()

        setupObservers()
    }

    fun captureUser(view: View?){
        val names = etNamesCapture.text.toString().trim()
        val last_name = etLastNamesCapture.text.toString().trim()
        val address = etAddressCapture.text.toString().trim()
        val telephone = etTelephoneCapture.text.toString().trim()
        val email = etEmailCapture.text.toString().trim()
        val password = etPassCapture.text.toString().trim()
        val confirm_password = etConfirmPassCapture.text.toString().trim()
    }

    private fun setupObservers(){
        captureViewModel.getCells().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading -> Log.i(CAPTURE_USER_FRAGMENT, "Cargando...")
                is Resource.Success -> {
                    Log.i(CAPTURE_USER_FRAGMENT, "List: ${result.data}")
                }
                is Resource.Failure -> Log.e(CAPTURE_USER_FRAGMENT, "Error: ${result.exception}")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}