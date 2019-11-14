package com.kuky.demo.wan.android.ui.wxchapter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kuky.demo.wan.android.R
import com.kuky.demo.wan.android.base.*
import com.kuky.demo.wan.android.databinding.FragmentWxChapterBinding
import com.kuky.demo.wan.android.ui.widget.ErrorReload
import com.kuky.demo.wan.android.ui.wxchapterlist.WxChapterListFragment

/**
 * @author kuky.
 * @description 首页公众号模块界面
 */
class WxChapterFragment : BaseFragment<FragmentWxChapterBinding>() {

    private val mViewModel by lazy {
        ViewModelProvider(requireActivity(), WxChapterModelFactory(WxChapterRepository()))
            .get(WxChapterViewModel::class.java)
    }
    private val mAdapter by lazy { WxChapterAdapter(null) }

    override fun getLayoutId(): Int = R.layout.fragment_wx_chapter

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        mBinding.refreshColor = R.color.colorAccent
        mBinding.refreshListener = SwipeRefreshLayout.OnRefreshListener {
            fetchWxChapter()
        }

        mBinding.adapter = mAdapter
        mBinding.listener = OnItemClickListener { position, _ ->
            mAdapter.getItemData(position)?.let {
                WxChapterListFragment.navigate(mNavController, R.id.action_mainFragment_to_wxChapterListFragment, it.id, it.name)
            }
        }

        mBinding.errorReload = ErrorReload {
            fetchWxChapter()
        }

        mBinding.gesture = DoubleClickListener(null, {
            mBinding.rcvChapter.scrollToTop()
        })

        fetchWxChapter(false)
    }

    private fun fetchWxChapter(isRefresh: Boolean = true) {
        mViewModel.getWxChapter()
        mViewModel.netState.observe(this, Observer {
            when (it.state) {
                State.RUNNING -> injectStates(refreshing = true, loading = !isRefresh)

                State.SUCCESS -> injectStates()

                State.FAILED -> {
                    mBinding.wxChapterType.text = resources.getText(R.string.text_place_holder)
                    injectStates(error = true)
                }
            }
        })

        mViewModel.mData.observe(this, Observer {
            mBinding.emptyStatus = it.isNullOrEmpty()
            mAdapter.update(it)
            mBinding.wxChapterType.text = resources.getText(R.string.wx_chapter)
        })
    }

    private fun injectStates(refreshing: Boolean = false, loading: Boolean = false, error: Boolean = false) {
        mBinding.refreshing = refreshing
        mBinding.loadingStatus = loading
        mBinding.errorStatus = error
    }
}