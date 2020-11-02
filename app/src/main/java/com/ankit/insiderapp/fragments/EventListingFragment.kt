package com.ankit.insiderapp.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankit.insiderapp.ViewModelpackage.MainViewModel
import com.ankit.insiderapp.adapter.EventAdapter
import com.ankit.insiderapp.databinding.FragmentGenericAdapterBinding
import com.ankit.insiderapp.helpers.Constants
import com.ankit.insiderapp.model.MasterListEventModel
import java.util.*

class EventListingFragment : Fragment() {
    private var eventList: ArrayList<MasterListEventModel>? = null
    private var groupTitle: String? = null
    private var mEventMainViewModel: MainViewModel? = null
    private lateinit var mContext: Context
    private lateinit var mEventAdapter: EventAdapter
    private lateinit var mBinding: FragmentGenericAdapterBinding

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
        mBinding = FragmentGenericAdapterBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() {
        mEventAdapter = EventAdapter(eventList)
        mBinding.rvEvents.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mBinding.rvEvents.adapter = mEventAdapter
    }

    private fun initObservers() {
        if (activity != null && activity is FragmentActivity) {
            mEventMainViewModel =
                ViewModelProvider(activity as FragmentActivity).get(MainViewModel::class.java)
            if (!TextUtils.isEmpty(groupTitle)) {
                if (mEventMainViewModel?.getEventsForGroup(groupTitle!!)
                        ?.hasActiveObservers() == false
                )
                    mEventMainViewModel?.getEventsForGroup(groupTitle!!)
                        ?.observe(viewLifecycleOwner, Observer {
                            eventList = it.list
                            setupEventData()
                        })
            }
        }
    }

    private fun setupEventData() {
        eventList?.let {
            mEventAdapter.updateList(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(groupTitle: String) =
            EventListingFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(Constants.GROUP_TITLE, groupTitle)
                    }
                }
    }

}