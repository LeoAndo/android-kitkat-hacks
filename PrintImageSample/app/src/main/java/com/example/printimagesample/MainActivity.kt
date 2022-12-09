package com.example.printimagesample

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.print.PrintHelper

/**
 * 画像の印刷 PDF保存
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonPrint).setOnClickListener {
            startPrint()
        }
    }

    private fun startPrint() {
        val printHelper = PrintHelper(this).apply {
            colorMode = PrintHelper.COLOR_MODE_COLOR
            orientation = PrintHelper.ORIENTATION_LANDSCAPE
            // SCALE_MODE_FIT: 画像を印刷サイズ内に全て収まるように配置する
            // SCALE_MODE_FILL: 画像を印刷サイズ全てを埋めるように拡大して表示する. 画像が全て印刷されず切れてしまう可能性あり
            scaleMode = PrintHelper.SCALE_MODE_FIT
        }
        val targetImageView: ImageView = findViewById(R.id.imageView)
        val bitmapDrawable: BitmapDrawable = targetImageView.drawable as? BitmapDrawable ?: return
        printHelper.printBitmap("test print image", bitmapDrawable.bitmap)

    }
}