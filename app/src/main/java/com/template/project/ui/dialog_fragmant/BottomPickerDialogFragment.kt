package com.template.project.ui.dialog_fragmant

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import com.template.project.R
import com.template.project.http.bean.AtomBean
import com.template.project.ui.base.BaseDialogFragment
import kotlinx.android.synthetic.main.frag_simple_item_picker_dinamsi.*


class BottomPickerDialogFragment : BaseDialogFragment(), View.OnClickListener {



    private var onSelectClick: OnSelectClick? = null


    fun newInstance(context: Context, listData: ArrayList<AtomBean>, title: String, valueDefaultd: String): BottomPickerDialogFragment? {
        val frag = BottomPickerDialogFragment()
        frag.data.addAll(listData)
        frag.title=title
        frag.valueDefaultd=valueDefaultd
        return frag
    }




    fun setOnSelectClick(onSelectClick: OnSelectClick?) {
        this.onSelectClick = onSelectClick
    }



    val data = ArrayList<AtomBean>()
    var title = ""
    var valueDefaultd = ""

    override fun bottomSheetMode(): Boolean {
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_simple_item_picker_dinamsi, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setView()
    }


    fun setView(){
        tv_title.text=title

        var idChecked=0

        data.forEachIndexed { index, it ->
            val radioButton = RadioButton(context)

            var id=index+1
            radioButton.id = id

            radioButton.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            radioButton.setBackgroundResource(R.color.white)
            radioButton.textSize=12f
            radioButton.tag=index
            radioButton.setOnClickListener(this)
//            radioButton.gravity= Gravity.CENTER
            val face = context?.let { it1 -> ResourcesCompat.getFont(it1, R.font.helvetica) }
            radioButton.typeface = face
//            textView.setTypeface(textView.typeface, Typeface.BOLD)

            if(valueDefaultd == it.name){
                idChecked=id
            }

            radioButton.text=it.name

            radioButton.setTextColor(Color.parseColor("#292929"))
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(50, 10, 10, 10)
            radioButton.layoutParams = params
            radioButton.setPadding(30, 20, 10, 20)

            val view = View(context)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
            view.setBackgroundResource(R.color.line_item)

            rg_root.addView(radioButton)
            rg_root.addView(view)
        }

            rg_root.check(idChecked)


        bt_close.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onClick(v: View?) {
        onSelectClick?.onSelect(data[v?.tag.toString().toInt()])
        dismissAllowingStateLoss()
    }


    interface OnSelectClick {
        fun onSelect(atomBean: AtomBean)
        fun onCancelClicked()
    }


}