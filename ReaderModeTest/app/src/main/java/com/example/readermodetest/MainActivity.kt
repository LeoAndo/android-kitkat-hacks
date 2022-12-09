package com.example.readermodetest

import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ReaderCallback
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

/**
 * 外部タグ検出時の通知を受けるため、ReaderCallbackのIFを実装する必要がある。
 */
class MainActivity : AppCompatActivity(), ReaderCallback {
    var mNfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    public override fun onResume() {
        super.onResume()
        // HCEアプリを検出対象にする場合、FLAG_READER_NFC_A / FLAG_READER_SKIP_NDEF_CHECKの設定が推奨されている
        mNfcAdapter?.enableReaderMode(
            this, this, NfcAdapter.FLAG_READER_NFC_A
                    or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null
        )
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.disableReaderMode(this)
    }

    /**
     * 外部タグ検出時に呼ばれるコールバックメソッド.
     * ここでは、外部タグを検出したらSELECT FILEコマンドを送信します。
     * SELECT FILEコマンドとは、AIDを指定して外部タグ内のサービスに接続するためのAPDUコマンドです。
     * フォーマットは以下の通り。
     * ヘッダ{0x00, 0xA4, 0x04, 0x00} + AID長 + AID値
     */
    override fun onTagDiscovered(tag: Tag) {
        val cmdLength = SELECT_CMD.size
        val aidLength = TARGET_AID.size
        val selectCmd = ByteArray(cmdLength + aidLength + 1)
        val responseCmd: ByteArray
        var responseString = ""
        val isoDep = IsoDep.get(tag) ?: return
        try {
            isoDep.connect()
            System.arraycopy(SELECT_CMD, 0, selectCmd, 0, cmdLength)
            selectCmd[cmdLength] = aidLength.toByte()
            System.arraycopy(
                TARGET_AID, 0, selectCmd, cmdLength + 1,
                aidLength
            )
            // SELECT FILEコマンドの送信。戻り値が外部タグが変えした応答値
            responseCmd = isoDep.transceive(selectCmd)
            for (i in responseCmd.indices) {
                responseString += String.format("0x%02x ", responseCmd[i])
            }
            Log.d(TAG, "responseCmd:$responseString")
            isoDep.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "ReaderModeTest"

        // 接続先の対象AID値(Host Card Emulationアプリケーションで登録しているAID値と一致させておく)
        private val TARGET_AID = byteArrayOf(
            0x01.toByte(),
            0x02.toByte(),
            0x03.toByte(),
            0x04.toByte(),
            0x05.toByte(),
            0x06.toByte(),
            0x07.toByte(),
            0x08.toByte()
        )

        // 外部タグへの接続コマンド (SELECT FILEコマンド)のヘッダ値
        private val SELECT_CMD =
            byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte())
    }
}