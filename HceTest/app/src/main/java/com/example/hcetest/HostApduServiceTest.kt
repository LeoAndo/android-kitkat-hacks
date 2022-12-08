package com.example.hcetest

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import java.lang.StringBuilder
import java.util.*

class HostApduServiceTest : HostApduService() {
    /**
     * 外部のReader / Writerから対象のAIDを含むAPDUコマンドが送られてきた場合、Android FWからコールされます。
     * 戻り値はそのままReader / Writerへの応答APDUコマンドになります。
     */
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle): ByteArray {
        var ret = RESPONSE_NG
        val apduString = StringBuilder()
        val cmdLength = SELECT_CMD.size
        val commandApduHeader = ByteArray(cmdLength)
        for (b in commandApdu) {
            apduString.append(String.format("0x%02x ", b))
        }
        Log.d(TAG, "commandApdu:$apduString")
        System.arraycopy(commandApdu, 0, commandApduHeader, 0, cmdLength)
        if (Arrays.equals(commandApduHeader, SELECT_CMD)) {
            ret = RESPONSE_OK
        }
        return ret
    }

    /**
     * 外部のReader / Writerとの接続が切断された場合、Android FWからコールされます。
     */
    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "onDeactivated: IN $reason")
    }

    companion object {
        private const val TAG = "HostApduServiceTest"
        private val RESPONSE_OK = byteArrayOf(0x90.toByte(), 0x00.toByte())
        private val RESPONSE_NG = byteArrayOf(0x00.toByte(), 0x00.toByte())
        private val SELECT_CMD =
            byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte())
    }
}