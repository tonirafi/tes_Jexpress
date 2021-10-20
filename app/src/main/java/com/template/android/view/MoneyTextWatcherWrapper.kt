package com.template.android.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.template.android.utils.LogUtil
import java.text.NumberFormat

/**
 *   Created by 5Mall<zhangwei> on 2019/1/2
 *   Email:zhangwei@qingsongchou.com
 *   描述：
 */
class MoneyTextWatcherWrapper(
    val goalMoney: EditText,
    val goalMoneySuffix: TextView,
    private val textWatcher: TextWatcher
) : TextWatcher {

    var preTxt = ""
    var preCleanString = -1L


    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        LogUtil.logE("TXT", "onTextChanged =s=$s start=$start ; before=$before; count=$count")
        if (s == null || preTxt == s.toString()) return

        goalMoney.removeTextChangedListener(this)

        LogUtil.logE("TXT", "onTextChanged= preTxt=$preTxt ; s=$s")
        if (preTxt.isEmpty() && s.isNotEmpty()) {
            goalMoneySuffix.text = ".000"
            LogUtil.logE("TXT", " .000")
        } else if (preTxt.isNotEmpty() && s.isEmpty()) {
            goalMoneySuffix.text = null
            goalMoney.text = null
            preTxt = ""
            LogUtil.logE("TXT", " ")
            textWatcher.afterTextChanged(goalMoney.text)
            goalMoney.addTextChangedListener(this)
            return
        }

        val cleanString = s.replace("[.,]".toRegex(), "").toLongOrNull()
            ?: 0

        preCleanString = if (preCleanString == cleanString) {
            val tmp =
                s.removeRange(start - before, start - before + 1).replace("[.,]".toRegex(), "")
                    .toLongOrNull()
                    ?: 0
            LogUtil.logE("TXT", "onTextChanged= tmp=$tmp ; s=$s")
            tmp
        } else if (cleanString >= 1_000_000_000) {
            cleanString.toString().substring(1).toLongOrNull() ?: cleanString
        } else cleanString


        val formatted = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 0 }
            .format(preCleanString).replace("[,]".toRegex(), ".")

        val delta = formatted.length - preTxt.length

        val sel =
            if (delta < 0) goalMoney.selectionStart + delta + 1 else goalMoney.selectionStart + delta - 1

        val selection = when {
            sel > formatted.length -> formatted.length
            sel < 0 -> 1
            else -> sel
        }
        LogUtil.logE(
            "TXT",
            "onTextChanged= sel=$sel ;selectionStart=${goalMoney.selectionStart} ;selection=$selection"
        )
        goalMoney.setText(formatted)
        goalMoney.setSelection(selection)
        textWatcher.afterTextChanged(goalMoney.text)

        preTxt = formatted

        goalMoney.addTextChangedListener(this)
    }
}