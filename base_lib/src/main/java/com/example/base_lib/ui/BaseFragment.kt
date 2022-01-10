package com.example.base_lib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.base_lib.ext.getViewBinding


interface BaseFragmentInterface {
    /**
     * Init UI
     * Đặt hàm này trong [Fragment.onViewCreated]
     */
    fun initView()

    /**
     * Init những gì liên quan tới UI
     *
     * Đặt hàm này trong [Fragment.onViewCreated], nằm dưới [initView]
     */
    fun setupFirst()

    /**
     * Đặt hàm này trong [Fragment.onResume]
     */
    fun setupLogic()

    /**
     * Đặt hàm này trong [Fragment.onStop]
     */
    fun cleanUp()
}

abstract class BaseFragment<VB : ViewBinding>(val clazz: Class<VB>) : Fragment(),
    BaseFragmentInterface {
    lateinit var viewBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getViewBinding(inflater, clazz).apply {
            viewBinding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupFirst()
    }

    override fun onResume() {
        super.onResume()
        setupLogic()
    }

    override fun onStop() {
        super.onStop()
        cleanUp()
    }

    override fun initView() {}

    override fun setupFirst() {}

    override fun setupLogic() {}

    override fun cleanUp() {}
}