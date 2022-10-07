package com.colaman.masklayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.colaman.mask.MaskMode
import com.colaman.masklayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.maskLayout.update {
            maskMode(MaskMode.Inside)
            mask(
                maskBitmap = context.resources.getDrawable(R.drawable.ic_mask)
                    .toBitmap(1000, 1000),
            )
        }
    }
}