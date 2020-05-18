package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.R
import com.david.redcristianauno.domain.NoticeUseCaseImpl
import com.david.redcristianauno.data.network.NoticeRepositoryImpl
import com.david.redcristianauno.presentation.ui.adapters.NoticeConfigurationAdapter
import com.david.redcristianauno.presentation.viewmodel.HomeViewModel
import com.david.redcristianauno.presentation.viewmodel.NoticeViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {

    private lateinit var listNewsAdapter: NoticeConfigurationAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            NoticeViewModelFactory(NoticeUseCaseImpl(NoticeRepositoryImpl()))
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listNewsAdapter = NoticeConfigurationAdapter()
        rvHome.adapter = listNewsAdapter
        viewModel.refresh()
        observedViewModel()
    }

    private fun observedViewModel() {
        viewModel.listNews.observe(viewLifecycleOwner, Observer { schedule ->
            listNewsAdapter.updateNews(schedule)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                rlBaseHome.visibility = View.GONE
            }
        })
    }
}