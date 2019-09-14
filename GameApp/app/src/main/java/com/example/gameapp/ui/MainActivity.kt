package com.example.gameapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.R
import com.example.gameapp.common.vibrate
import com.example.gameapp.data.BoxModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var baseViewModel: BaseViewModel
    lateinit var boxGridAdapter: BoxGridAdapter

    private var builder: androidx.appcompat.app.AlertDialog.Builder? = null
    private var dialog: androidx.appcompat.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel::class.java)
        boxGridAdapter = BoxGridAdapter()

        initGridLayoutWithData()

        boxGridAdapter.onBoxItemClickListener = object : BoxGridAdapter.OnBoxItemClickListener {
            override fun onBoxItemClick(boxModel: BoxModel) {
                baseViewModel.onItemSelected(boxModel)
            }
        }

        baseViewModel.onEventChangesListner = object : BaseViewModel.OnEventChangesListner {

            override fun setProgress(isEnable: Boolean) {
                pg_bar_main.visibility = if (isEnable) View.VISIBLE else View.INVISIBLE
            }

            override fun showGameVinMessge(rounds: Int) {
                showSuccessWithRatings(rounds)
            }

            override fun showMessages(msg: String, isVibrate: Boolean) {
                if (isVibrate) {
                    vibrate()
                }
                Snackbar.make(cons_main_view, msg, Snackbar.LENGTH_SHORT).show()
            }
        }

        imageView.setOnClickListener { initGridLayoutWithData() }
    }

    private fun initGridLayoutWithData() {
        tv_num_of_tries.text = ""
        tl_num_of_tries.visibility = View.GONE
        baseViewModel.list!!.observe(this, Observer {
            boxGridAdapter.setBoxItemList(it)
            val gridLayoutManager = GridLayoutManager(this, 4)
            rv_grid_box.layoutManager = gridLayoutManager
            rv_grid_box.adapter = boxGridAdapter
            pg_bar_main.visibility = View.INVISIBLE
        })

        pg_bar_main.visibility = View.VISIBLE
        baseViewModel.initDataList()

        baseViewModel.rounds.observe(this, Observer {
            tl_num_of_tries.visibility = View.VISIBLE
            tv_num_of_tries.text = it.toString()
        })
    }

    private fun showSuccessWithRatings(rounds: Int) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_win_layout, null)

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val successBtn = dialogView.findViewById<Button>(R.id.btn_success)
        val numOfRoundsTV = dialogView.findViewById<TextView>(R.id.tv_rounds)

        when (rounds) {
            1, 8 -> {
                ratingBar.progress = 3
            }
            9, 12 -> {
                ratingBar.progress = 2
            }
            else -> {
                ratingBar.progress = 1
            }
        }

        numOfRoundsTV.text = """Rounds :$rounds"""
        successBtn.setOnClickListener {
            dialog?.dismiss()
            initGridLayoutWithData()
        }

        if (builder == null) {
            builder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
        }
        builder!!.setView(dialogView)
        builder!!.setCancelable(true)
        builder!!.create()
        dialog = builder!!.show()
    }

}
