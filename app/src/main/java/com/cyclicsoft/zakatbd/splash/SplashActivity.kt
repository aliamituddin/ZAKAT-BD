package com.cyclicsoft.zakatbd.splash

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cyclicsoft.zakatbd.MainActivity
import com.cyclicsoft.zakatbd.R
import com.cyclicsoft.zakatbd.Util
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        bindObservers()
        initListener()
        bindData()
    }

    private fun bindData() {
        splashViewModel.loadData()
    }

    private fun bindObservers() {
        splashViewModel.isDataLoaded.observe(this, Observer {
            anim_loading.repeatCount = 1
        })
    }

    private fun initListener() {
        anim_loading?.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                Util.gotoActivity(this@SplashActivity, MainActivity(), false)
            }

            override fun onAnimationCancel(animation: Animator?) {
                Util.gotoActivity(this@SplashActivity, MainActivity(), false)
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
}
