package com.dingyi.tiecode.plugin.androlua.build.apkbuilder

import com.dingyi.tiecode.plugin.androlua.PluginApplication
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.EncryptedPrivateKeyInfo
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object SignatureReader {


    private fun readPublicKey(input: InputStream): X509Certificate {

        return try {
            val cf = CertificateFactory.getInstance("X.509")
            cf.generateCertificate(input) as X509Certificate
        } finally {
            input.close()
        }
    }

    fun readSignatureForAssets(name: String):SignatureV1 {

        val privateKey = readPrivateKey(
            PluginApplication.application.assets.open("keys/$name.pk8")
        )

        val publicKey = readPublicKey(
            PluginApplication.application.assets.open("keys/$name.x509.pem")
        )

        val signBlock =  PluginApplication.application.assets.open("keys/$name.sbt").readBytes()

        return SignatureV1(
            privateKey = privateKey,
            publicKey = publicKey,
            signBlock = signBlock
        )

    }

    private fun readPrivateKey(input: InputStream): PrivateKey {
        val input = DataInputStream(input)
        return try {
            val bytes = input.readBytes()
            var spec: KeySpec? = decryptPrivateKey(bytes, "")
            if (spec == null) {
                spec = PKCS8EncodedKeySpec(bytes)
            }
            try {
                KeyFactory.getInstance("RSA").generatePrivate(spec)
            } catch (ex: InvalidKeySpecException) {
                KeyFactory.getInstance("DSA").generatePrivate(spec)
            }
        } finally {
            input.close()
        }
    }


    private fun decryptPrivateKey(bArr: ByteArray, password: String): KeySpec? {
        return try {
            val encryptedPrivateKeyInfo = EncryptedPrivateKeyInfo(bArr)
            val generateSecret =
                SecretKeyFactory.getInstance(encryptedPrivateKeyInfo.algName)
                    .generateSecret(PBEKeySpec(password.toCharArray()))
            val cipher = Cipher.getInstance(encryptedPrivateKeyInfo.algName)
            cipher.init(2, generateSecret, encryptedPrivateKeyInfo.algParameters)
            try {
                encryptedPrivateKeyInfo.getKeySpec(cipher)
            } catch (e: InvalidKeySpecException) {
                System.err.println("PrivateKey may be bad.")
                throw e
            }
        } catch (e2: IOException) {
            null
        }
    }

}