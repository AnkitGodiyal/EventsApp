package com.ankit.insiderapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ankit.insiderapp.ViewModelpackage.MainViewModel
import com.ankit.insiderapp.adapter.EventPagerAdapter
import com.ankit.insiderapp.databinding.ActivityMainBinding
import com.ankit.insiderapp.datarepo.SharedPrefHelper
import com.ankit.insiderapp.helpers.Constants
import com.ankit.insiderapp.helpers.iConnectedToNetwork
import com.ankit.insiderapp.location.LocationBottomSheet
import com.ankit.insiderapp.model.ModelEvents
import com.google.android.material.tabs.TabLayoutMediator

const val VIEW_FOR_SUCCESS = 100
const val VIEW_FOR_FAILURE = 200
const val VIEW_FOR_NO_DATA = 300
const val VIEW_FOR_NETWORK_NOT_AVAILABLE = 400


class MainActivity : AppCompatActivity(), LocationBottomSheet.CitySelectedListener {

    private var mEventMainViewModel: MainViewModel? = null
    private lateinit var mLayoutBinding: ActivityMainBinding
    private lateinit var mEventPagerAdapter: EventPagerAdapter
    private var mEventData: ModelEvents? = null
    private var mSelectedCity: String = Constants.DEFAULT_CITY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLayoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        initObject()
        initFetchEventData()
        initObserver()
        initView()
        initListeners()
    }

    private fun initListeners() {
        mLayoutBinding.retryTv.setOnClickListener {
            initFetchEventData()
        }

        mLayoutBinding.selectedCity.setOnClickListener {
            supportFragmentManager.let {
                LocationBottomSheet.newInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }
    }

    private fun initView() {
        mLayoutBinding.selectedCity.setCompoundDrawablesRelativeWithIntrinsicBounds(
            AppCompatResources.getDrawable(this, R.drawable.ic_location_white),
            null,
            null,
            null
        )
        mLayoutBinding.selectedCity.compoundDrawablePadding =8
        if (TextUtils.isEmpty(SharedPrefHelper.readFromPref(this, Constants.CURRENT_CITY)))
            mLayoutBinding.selectedCity.text = resources.getString(R.string.choose_location)
        else
            mLayoutBinding.selectedCity.text =
                SharedPrefHelper.readFromPref(this, Constants.CURRENT_CITY).capitalize()
    }

    private fun initObject() {
        mEventMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mSelectedCity =
            if (TextUtils.isEmpty(SharedPrefHelper.readFromPref(this, Constants.CURRENT_CITY)))
                Constants.DEFAULT_CITY
            else
                SharedPrefHelper.readFromPref(this, Constants.CURRENT_CITY)
    }

    private fun initObserver() {
        if (mEventMainViewModel?.getAllEvents()?.hasActiveObservers() == false)
            mEventMainViewModel?.getAllEvents()?.observe(this, Observer {
                when (it.status) {
                    Constants.STATUS_SUCCESS -> {
                        mEventData = it
                        updateView(VIEW_FOR_SUCCESS)
                    }
                    Constants.STATUS_FAILURE -> {
                        updateView(VIEW_FOR_FAILURE)
                    }
                    Constants.STATUS_NO_DATA -> {
                        updateView(VIEW_FOR_NO_DATA)
                    }
                    else -> {
                        updateView(VIEW_FOR_FAILURE)
                    }
                }
            })
    }

    private fun initFetchEventData() {
        if (iConnectedToNetwork()) {
            mLayoutBinding.errorGroup.visibility = View.GONE
            mLayoutBinding.progressGroup.visibility = View.VISIBLE
            mEventMainViewModel?.fetchAllEvents(mSelectedCity)
        } else {
            updateView(VIEW_FOR_NETWORK_NOT_AVAILABLE)
        }
    }

    private fun updateView(viewType: Int) {
        mLayoutBinding.progressGroup.visibility = View.GONE
        when (viewType) {
            VIEW_FOR_SUCCESS -> {
                mLayoutBinding.errorGroup.visibility = View.GONE
                mLayoutBinding.selectedCity.text = mSelectedCity.capitalize()
                setEventAdapter()
                mLayoutBinding.eventViewPager.visibility = View.VISIBLE
                mLayoutBinding.eventViewPager
            }
            VIEW_FOR_NO_DATA -> {
                mLayoutBinding.errorGroup.visibility = View.VISIBLE
                mLayoutBinding.eventViewPager.visibility = View.GONE
                mLayoutBinding.errorTv.text = getString(R.string.no_events_available_text)
            }
            VIEW_FOR_FAILURE -> {
                mLayoutBinding.errorGroup.visibility = View.VISIBLE
                mLayoutBinding.eventViewPager.visibility = View.GONE
                mLayoutBinding.errorTv.text = getString(R.string.something_went_wrong)
            }
            VIEW_FOR_NETWORK_NOT_AVAILABLE -> {
                mLayoutBinding.errorGroup.visibility = View.VISIBLE
                mLayoutBinding.eventViewPager.visibility = View.GONE
                mLayoutBinding.errorTv.text = getString(R.string.no_network_text)
            }
        }
    }

    private fun setEventAdapter() {
        if (mEventData?.responseModel?.list?.masterList == null || mEventData?.responseModel?.list?.masterList?.size == 0) {
            updateView(VIEW_FOR_NO_DATA)
            return
        }
        val dataList = ArrayList<String>()
        dataList.add(0, Constants.ALL_EVENTS)
        mEventData?.responseModel?.groups?.let {
            if (it.size > 0)
                dataList.addAll(it)
        }
        if (!::mEventPagerAdapter.isInitialized) {
            mEventPagerAdapter = EventPagerAdapter(this, dataList)
        }
        mLayoutBinding.eventViewPager.adapter = mEventPagerAdapter
        mLayoutBinding.eventViewPager
        mLayoutBinding.tabLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
        TabLayoutMediator(
            mLayoutBinding.tabLayout,
            mLayoutBinding.eventViewPager
        ) { tab, position ->
            tab.text = dataList[position]
        }.attach()

    }

    override fun onCitySelected(city: String) {
        mSelectedCity = city
        SharedPrefHelper.writeToPref(this, Constants.CURRENT_CITY, city)
        initFetchEventData()
    }
}