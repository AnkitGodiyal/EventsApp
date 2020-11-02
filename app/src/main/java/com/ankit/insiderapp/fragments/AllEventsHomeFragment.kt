package com.ankit.insiderapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankit.insiderapp.BannersWebViewActivity
import com.ankit.insiderapp.R
import com.ankit.insiderapp.ViewModelpackage.MainViewModel
import com.ankit.insiderapp.databinding.FragmentAllEventsHomeBinding
import com.ankit.insiderapp.helpers.Constants
import com.ankit.insiderapp.model.HomePageModel
import com.example.demopaytminsider.homesection.adapter.AllEventsAdapter

class AllEventsHomeFragment : Fragment() {
    private lateinit var mAdapter: AllEventsAdapter
    private var homePageModel: HomePageModel? = null
    private lateinit var mContext: Context
    private lateinit var mBinding: FragmentAllEventsHomeBinding
    private var mEventMainViewModel: MainViewModel? = null
    private var groupTitle: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupTitle = it.getString(Constants.GROUP_TITLE)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAllEventsHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        if (activity != null && activity is FragmentActivity) {
            mEventMainViewModel =
                    ViewModelProvider(activity as FragmentActivity).get(MainViewModel::class.java)
            Log.e("TAG AllEventsFrag", "" + mEventMainViewModel)
            if (mEventMainViewModel?.getModelForHome()?.hasActiveObservers() == false)
                mEventMainViewModel?.getModelForHome()
                        ?.observe(viewLifecycleOwner, Observer {
                            homePageModel = it as HomePageModel?
                            setupEventData()
                        })
        }
    }

    private fun initViews() {
        mAdapter = AllEventsAdapter(homePageModel,::openBannerWebView)
        mBinding.rvEvents.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mBinding.rvEvents.adapter = mAdapter
    }


    private fun setupEventData() {
        mAdapter.update(homePageModel)
    }

    private fun openBannerWebView(url: String) {
            val intent = Intent(context, BannersWebViewActivity::class.java)
            intent.putExtra("url", url)
            mContext.startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(groupTitle: String) =
                AllEventsHomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(Constants.GROUP_TITLE, groupTitle)
                    }
                }
    }
}