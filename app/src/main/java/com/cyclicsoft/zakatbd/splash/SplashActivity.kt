package com.cyclicsoft.zakatbd.splash

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cyclicsoft.zakatbd.MainActivity
import com.cyclicsoft.zakatbd.R
import com.cyclicsoft.zakatbd.Util
import kotlinx.android.synthetic.main.activity_main.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
        anim_loading?.playAnimation()
    }

    private fun init() {
        anim_loading?.repeatCount=1 //Number of loading animation will play
        anim_loading?.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                Util.gotoActivity(this@SplashActivity, MainActivity(), false)
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
}
