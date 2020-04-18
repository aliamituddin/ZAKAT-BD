package com.cyclicsoft.zakatbd

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//        supportActionBar?.setCustomView(R.layout.main_toolbar)
        setupBlurView()
        setupViws()
    }

    private fun setupBlurView() {
        val radius = 12f
        val minBlurRadius = 10f
        val step = 4f
        //set background, if your root layout doesn't have one
        val windowBackground = window.decorView.background
        blur_view.setupWith(root)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(true)
    }

    private fun setupViws() {
        img_toolbar_left?.setImageResource(R.drawable.ic_help_outline_black_24dp)
        img_toolbar_left?.visibility = View.VISIBLE

        img_toolbar_right?.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        img_toolbar_right?.visibility = View.VISIBLE
    }
}
