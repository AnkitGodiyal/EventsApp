package com.example.demopaytminsider.homesection.adapter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.text.method.Touch.onTouchEvent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ankit.insiderapp.R
import com.ankit.insiderapp.adapter.BannersAdapter
import com.ankit.insiderapp.adapter.SectionEventAdapter
import com.ankit.insiderapp.databinding.LayoutBannersBinding
import com.ankit.insiderapp.databinding.LayoutProductSectionBinding
import com.ankit.insiderapp.model.HomePageModel
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList


class AllEventsAdapter(
    private var mHomePageModel: HomePageModel? = null,
    var openBannerWebView: (String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    init {
        setupItemList()
    }

    private lateinit var mContext: Context
    private var itemList: List<Int> = ArrayList()
    private val viewTypeBanners = 1
    private val viewTypeFeatureSection = 2
    private val viewTypePopularSection = 3


    fun update(homePageModel: HomePageModel?) {
        mHomePageModel = homePageModel
        setupItemList()
        notifyDataSetChanged()
    }

    private fun setupItemList() {
        val tempItemList: ArrayList<Int> = ArrayList()
        if (mHomePageModel?.bannersList?.isEmpty() == false)
            tempItemList.add(viewTypeBanners)
        if (mHomePageModel?.featuredEventList?.isEmpty() == false)
            tempItemList.add(viewTypeFeatureSection)
        if (mHomePageModel?.popularEventList?.isEmpty() == false)
            tempItemList.add(viewTypePopularSection)
        itemList = tempItemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        var binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_product_section, parent, false
        )
        return if (viewType == viewTypePopularSection)
            FeaturedViewHolder(binding as LayoutProductSectionBinding)
        else if (viewType == viewTypeFeatureSection)
            PopularViewHolder(binding as LayoutProductSectionBinding)
        else {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_banners, parent, false
            )
            BannerViewHolder(binding as LayoutBannersBinding)

        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position]
    }

    inner class FeaturedViewHolder(itemBinding: LayoutProductSectionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.sectionHeading.text =
                mContext.resources.getString(R.string.featured_event_heading)
            val adapter = SectionEventAdapter(mHomePageModel?.featuredEventList)
            itemBinding.sectionRV.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            itemBinding.sectionRV.adapter = adapter
            val listener = object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val action = e.action
                    Log.e("TouchEvent:", action.toString())
                    if (itemBinding.sectionRV.canScrollHorizontally(RecyclerView.FOCUS_FORWARD)) {
                        when (action) {
                            MotionEvent.ACTION_MOVE ->
                                rv.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        return false
                    } else {
                        when (action) {
                            MotionEvent.ACTION_MOVE -> rv.parent
                                .requestDisallowInterceptTouchEvent(false)
                        }
                        itemBinding.sectionRV.removeOnItemTouchListener(this)
                        return true
                    }
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            }

            itemBinding.sectionRV.addOnItemTouchListener(listener)
        }
    }

    inner class BannerViewHolder(itemBinding: LayoutBannersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            lateinit var slidingImageDots: Array<ImageView?>
            var slidingDotsCount = mHomePageModel?.bannersList?.size ?: 0

            var myViewPager2 = itemBinding.viewPager
            var bannersAdapter = BannersAdapter(mHomePageModel?.bannersList,openBannerWebView)
            myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
            myViewPager2.setAdapter(bannersAdapter)
            myViewPager2.setOffscreenPageLimit(3)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myViewPager2.setNestedScrollingEnabled(true)
                myViewPager2.startNestedScroll(ViewPager2.SCROLL_AXIS_HORIZONTAL)
            }

            val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    for (i in 0 until slidingDotsCount) {
                        slidingImageDots[i]?.setImageDrawable(
                            ContextCompat.getDrawable(
                                mContext,
                                R.drawable.non_active_dot
                            )
                        )
                    }

                    slidingImageDots[position]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            mContext,
                            R.drawable.active_dot
                        )
                    )
                }
            }
            myViewPager2.registerOnPageChangeCallback(slidingCallback)

            slidingImageDots = arrayOfNulls(slidingDotsCount)
            for (i in 0 until slidingDotsCount) {
                slidingImageDots[i] = ImageView(mContext)
                slidingImageDots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.non_active_dot
                    )
                )
                val params =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                params.setMargins(8, 0, 8, 0)
                itemBinding.sliderDots.addView(slidingImageDots[i], params)
            }

            slidingImageDots[0]?.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.active_dot
                )
            )

            var currentPage = 0
            var pageOffset = 4
            var pageMargin = 8

            val handler = Handler()
            val update = Runnable {
                if (currentPage == slidingDotsCount) {
                    currentPage = 0
                }

                //The second parameter ensures smooth scrolling
                myViewPager2.setCurrentItem(currentPage++, true)
            }

            Timer().schedule(object : TimerTask() {
                // task to be scheduled
                override fun run() {
                    handler.post(update)
                }
            }, 3500, 3500)

            myViewPager2.setPageTransformer(object : ViewPager2.PageTransformer {
                override fun transformPage(page: View, position: Float) {
                    val myOffset = position
                    if (myViewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                        if (ViewCompat.getLayoutDirection(myViewPager2) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            page.setTranslationX(-myOffset)
                        } else {
                            page.setTranslationX(myOffset)
                        }
                    } else {
                        page.setTranslationY(myOffset)
                    }
                }
            })
        }
    }


    inner class PopularViewHolder(itemBinding: LayoutProductSectionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.sectionHeading.text =
                mContext.resources.getString(R.string.popular_event_heading)
            val adapter = SectionEventAdapter(mHomePageModel?.popularEventList)
            itemBinding.sectionRV.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            itemBinding.sectionRV.adapter = adapter

            val listener = object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val action = e.action
                    if (itemBinding.sectionRV.canScrollHorizontally(RecyclerView.FOCUS_FORWARD) && MotionEvent.ACTION_MOVE == action) {
                        rv.parent.parent.requestDisallowInterceptTouchEvent(true)
                        return false
                    } else {
                        when (action) {
                            MotionEvent.ACTION_MOVE -> rv.parent.parent
                                .requestDisallowInterceptTouchEvent(false)
                        }
                        itemBinding.sectionRV.removeOnItemTouchListener(this)
                        return true
                    }
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            }

            itemBinding.sectionRV.addOnItemTouchListener(listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}
