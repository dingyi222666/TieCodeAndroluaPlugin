package com.dingyi.tiecode.plugin.androlua.build.apkbuilder

import java.security.MessageDigest
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.crypto.Cipher

class SignatureV1(
    val privateKey: PrivateKey,
    val publicKey: X509Certificate,
    val signBlock: ByteArray
) {

    private var afterAlgorithmIdBytes = byteArrayOf(4.toByte(), 20.toByte())
    private var algorithmIdBytes = byteArrayOf(
        48.toByte(),
        9.toByte(),
        6.toByte(),
        5.toByte(),
        43.toByte(),
        14.toByte(),
        3.toByte(),
        2.toByte(),
        26.toByte(),
        5.toByte(),
        0.toByte()
    )
    private val beforeAlgorithmIdBytes = byteArrayOf(48.toByte(), 33.toByte())
    private val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
        init(1, privateKey)
    }
    private val md = MessageDigest.getInstance("SHA1")


    fun sign(): ByteArray {
        cipher.update(beforeAlgorithmIdBytes)
        cipher.update(algorithmIdBytes)
        cipher.update(afterAlgorithmIdBytes)
        cipher.update(md.digest())
        return cipher.doFinal()
    }

    fun update(data: ByteArray) {
        md.update(data)
    }

    fun update(data: ByteArray, offset: Int, len: Int) {
        md.update(data, offset, len)
    }
}