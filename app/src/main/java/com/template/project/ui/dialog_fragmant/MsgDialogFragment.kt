package com.template.project.ui.dialog_fragmant

import android.content.Context
import android.os.Bundle
import android.view.*
import com.template.project.R
import com.template.project.ui.base.BaseDialogFragment
import com.template.project.utils.DensityUtil
import kotlinx.android.synthetic.main.frag_msg_d.*


class MsgDialogFragment : BaseDialogFragment() {

    private var listener: OnClickListener? = null

    var msgTitle: CharSequence? = null
    var msgContent: CharSequence? = null
    var leftButtonText: CharSequence? = null
    var rightButtonText: CharSequence? = null
    var markId: Int = 100
    var canceledOnTouchOutside = true
    var maxContentHeight = DensityUtil.dp2px(220f) //内容区默认最大高度

    override fun bottomSheetMode(): Boolean {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_msg_d, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (!bottomSheetMode()) {
            dialog?.findViewById<View>(android.R.id.content)?.let {
                val dp = DensityUtil.dp2px(16f)
                it.setPadding(dp, 0, dp, 0)
            }

            dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside)
            dialog?.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    !canceledOnTouchOutside
                else
                    false
            }
        }
    }

    fun setOnClickTouchOutsideCancelable(cancel: Boolean) {
        canceledOnTouchOutside = cancel
        dialog?.setCanceledOnTouchOutside(cancel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentFrame.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                contentFrame.viewTreeObserver.removeOnPreDrawListener(this)
                if (contentFrame.measuredHeight > maxContentHeight) {
                    gradientMaskView.visibility = View.VISIBLE
                    contentFrame.layoutParams.let {
                        it.height = maxContentHeight
                        contentFrame.layoutParams = it
                    }
                } else {
                    gradientMaskView.visibility = View.GONE
                }
                //Return true to proceed with the current drawing pass, or false to cancel.
                return false
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        leftButton.setOnClickListener {
            dismiss()
            listener?.onClick(false, markId)
        }

        rightButton.setOnClickListener {
            dismiss()
            listener?.onClick(true, markId)
        }

        title.text = msgTitle
        content.text = msgContent
        leftButton.text = leftButtonText
        rightButton.text = rightButtonText

        if (title.text.isEmpty()) {
            title.visibility = View.GONE
        }

        if (rightButton.text.isEmpty()) {
            rightButton.visibility = View.GONE
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.listener = null
    }

    interface OnClickListener {
        fun onClick(onClickRightBtn: Boolean, markId: Int)
    }
}