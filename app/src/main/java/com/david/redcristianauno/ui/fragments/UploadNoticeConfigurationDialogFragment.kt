package com.david.redcristianauno.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

import com.david.redcristianauno.R
import com.david.redcristianauno.domain.NoticeUseCaseImpl
import com.david.redcristianauno.model.network.FirebaseService
import com.david.redcristianauno.model.network.NoticeRepositoryImpl
import com.david.redcristianauno.viewmodel.NoticeViewModel
import com.david.redcristianauno.viewmodel.NoticeViewModelFactory
import kotlinx.android.synthetic.main.fragment_upload_notice_configuration_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class UploadNoticeConfigurationDialogFragment : DialogFragment() {
    private val PICK_IMAGE = 1001
    val firebaseService = FirebaseService()
    var path:Uri? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            NoticeViewModelFactory(NoticeUseCaseImpl(NoticeRepositoryImpl()))
        ).get(NoticeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackUploadNoticeConfiguration.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackUploadNoticeConfiguration.setNavigationOnClickListener {
            dismiss()
        }

        ivUploadNoticeConfigurationDialogFragment.setOnClickListener {
            loadImage()
        }
        btnEditProfileConfigurationDialog.setOnClickListener {
            uploadImage()
        }

    }

    private fun uploadImage() {
        val title = etNameConfigurationDialogFragment.text.toString().trim()
        val description = etLastNamesConfigurationDialogFragment.text.toString().trim()
        rlBaseNotice.visibility = View.VISIBLE
        viewModel.compressImage(path!!, title, description, ivUploadNoticeConfigurationDialogFragment,pbLoadImage, rlBaseNotice)
        cleanField()
    }

    private fun cleanField() {
        etNameConfigurationDialogFragment.setText("")
        etLastNamesConfigurationDialogFragment.setText("")
        ivUploadNoticeConfigurationDialogFragment.setImageDrawable(resources.getDrawable(R.drawable.ic_add_notice))
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
             path = data?.data
            ivUploadNoticeConfigurationDialogFragment.setImageURI(path)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}
