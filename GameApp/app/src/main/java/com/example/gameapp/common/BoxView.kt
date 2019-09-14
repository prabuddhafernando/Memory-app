package com.example.gameapp.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.gameapp.R
import com.example.gameapp.data.BoxModel
import com.example.gameapp.data.BoxStatus
import kotlinx.android.synthetic.main.box_view.view.*

class BoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var boxModel: BoxModel

    open fun setBoxView(boxModel: BoxModel) {
        this.boxModel = boxModel
        setUpUI()
        invalidate()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.box_view, this, true)
        attrs?.let {

        }

        initUI()
    }

    private fun initUI() {
        tv_char.text = "A"
        tv_char.visibility = View.GONE
        cons_box_background.background = ContextCompat.getDrawable(context, R.drawable.btn_bg_gray_rounded_corner)
    }

    private fun setUpUI() {
        when (boxModel.Status) {
            BoxStatus.IDLE -> {
                tv_char.visibility = View.GONE
                cons_box_background.background = ContextCompat.getDrawable(context, R.drawable.btn_bg_gray_rounded_corner)
            }
            BoxStatus.ACTIVE -> {
                tv_char.text = boxModel.letter.toString()
                tv_char.visibility = View.VISIBLE
                cons_box_background.background = ContextCompat.getDrawable(context, R.drawable.btn_bg_gray_rounded_corner)
            }
            BoxStatus.MATCHED -> {
                tv_char.text = ""
                tv_char.visibility = View.GONE
                cons_box_background.background = ContextCompat.getDrawable(context, R.drawable.btn_bg_transparent)
            }
        }
        invalidate()
    }


}