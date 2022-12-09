package com.example.printhtmlsample

import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintAttributes.Resolution
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnPrint).setOnClickListener {
            // Googleページをロード
            webView.loadUrl("https://www.google.co.jp/")
        }
        webView = findViewById<WebView>(R.id.webview)
        // WebView Clientを登録し、ページ読み込み完了時のメソッドをオーバーライドする
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // ページ読み込み完了で印刷処理実行
                startPrint(webView)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

    private fun startPrint(webView: WebView) {
        // PrintManager取得
        val printManager = getSystemService(PrintManager::class.java)

        // PrintDocumentAdapter生成
        val printDocumentAdapter = webView.createPrintDocumentAdapter("docTest")

        // PrintAttributes生成
        val printAttributes = PrintAttributes.Builder() //
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR) //
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4) //
            .setResolution(Resolution("Brilliant", "Service", 600, 600)) //
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS) //
            .build()

        // 印刷実行
        printManager.print("Google page - test print", printDocumentAdapter, printAttributes)
    }
}