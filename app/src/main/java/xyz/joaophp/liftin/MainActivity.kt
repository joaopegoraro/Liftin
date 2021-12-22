package xyz.joaophp.liftin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var onBackPressedListener: (() -> Unit)? = null

    fun setOnBackPressedListener(listener: (() -> Unit)?) {
        onBackPressedListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener?.invoke()
        } else {
            super.onBackPressed()
        }
    }

}