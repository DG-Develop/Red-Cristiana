package com.david.redcristianauno.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.david.redcristianauno.R

/**
 * A simple [Fragment] subclass.
 */
class UploadNoticeConfigurationDialogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_upload_notice_configuration_dialog,
            container,
            false
        )
    }

}
