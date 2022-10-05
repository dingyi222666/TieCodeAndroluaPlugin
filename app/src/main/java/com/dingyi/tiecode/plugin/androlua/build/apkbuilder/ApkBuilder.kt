package com.dingyi.tiecode.plugin.androlua.build.apkbuilder

import apksigner.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FilterOutputStream
import java.io.OutputStream
import java.io.PrintStream
import java.security.DigestOutputStream
import java.security.MessageDigest
import java.util.jar.Attributes
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.jar.Manifest
import java.util.regex.Pattern
import kotlin.properties.Delegates


class ApkBuilder(
    private val targetFile: File
) {

    private lateinit var signatureV1: SignatureV1
    private lateinit var jarOutputStream: JarOutputStream

    private lateinit var manifest: Manifest

    private var timestamp = 0L

    fun start() {
        try {
            signatureV1.sign()
        } catch (e: Exception) {
            error("请设置签名")
        }
        jarOutputStream = JarOutputStream(targetFile.outputStream())
        manifest = Manifest()
        val attributes = manifest.mainAttributes
        attributes.putValue("Manifest-Version", "1.0");

        attributes.putValue("Created-By", "TieCodeAndroLuaPlugin 1.0.0")
    }

    fun finish() {

        // MANIFEST.MF
        writeManifest()
        writeOtherSignFile()

        jarOutputStream.finish()

        jarOutputStream.close()

    }

    private fun writeOtherSignFile() {

        // CERT.SF
        var entry = JarEntry(CERT_SF_NAME)
        entry.time = timestamp
        jarOutputStream.putNextEntry(entry)
        val byteArrayOutputStream = ByteArrayOutputStream()
        writeSignatureFile(manifest, byteArrayOutputStream)
        val signedData = byteArrayOutputStream.toByteArray()
        jarOutputStream.write(signedData)
        signatureV1.update(signedData)


        // CERT.RSA
        entry = JarEntry(CERT_RSA_NAME);
        entry.time = timestamp;
        jarOutputStream.putNextEntry(entry);
        writeSignatureBlock()

    }


    private fun writeSignatureBlock() {
        jarOutputStream.write(signatureV1.signBlock)
        jarOutputStream.write(signatureV1.sign())
    }


    /**
     * Write to another stream and track how many bytes have been written.
     */
    private class CountOutputStream(out: OutputStream) : FilterOutputStream(out) {
        private var mCount = 0

        override fun write(b: Int) {
            super.write(b)
            mCount++
        }


        override fun write(b: ByteArray, off: Int, len: Int) {
            super.write(b, off, len)
            mCount += len
        }

        fun size(): Int {
            return mCount
        }
    }

    /** Write a .SF file with a digest of the specified manifest.  */

    private fun writeSignatureFile(manifest: Manifest, out: OutputStream) {
        val sf = Manifest()
        val main = sf.mainAttributes
        main.putValue("Signature-Version", "1.0")
        main.putValue("Created-By", "TieCodeAndroLuaPlugin 1.0.0")
        val md = MessageDigest.getInstance("SHA1")
        val print = PrintStream(DigestOutputStream(ByteArrayOutputStream(), md), true, "UTF-8")

        // Digest of the entire manifest
        manifest.write(print)
        print.flush()
        main.putValue("SHA1-Digest-Manifest", Base64.encode(md.digest()))


        val entries = manifest.entries

        entries.forEach { (key, value) ->
            // Digest of the manifest stanza for this entry.
            print.print("Name: $key\r\n");

            for (att in value.entries) {
                print.print((att.key.toString() + ": " + att.value) + "\r\n")
            }
            print.print("\r\n")
            print.flush()

            val sfAttr = Attributes()
            sfAttr.putValue("SHA1-Digest", Base64.encode(md.digest()))
            sf.entries[key] = sfAttr
        }


        val count = CountOutputStream(out)
        sf.write(count)

        // A bug in the java.util.jar implementation of Android platforms
        // up to version 1.6 will cause a spurious IOException to be thrown
        // if the length of the signature file is a multiple of 1024 bytes.
        // As a workaround, add an extra CRLF in this case.
        if (count.size() % 1024 == 0) {
            count.write('\r'.code)
            count.write('\n'.code)
        }
    }

    private fun writeManifest() {
        val entry = JarEntry(JarFile.MANIFEST_NAME);
        entry.time = timestamp;
        jarOutputStream.putNextEntry(entry)
        manifest.write(jarOutputStream);
    }

    fun setSignature(signatureV1: SignatureV1) {
        this.signatureV1 = signatureV1
        // Assume the certificate is valid for at least an hour.
        timestamp = signatureV1.publicKey.notBefore.time + 3600L * 1000;
    }


    fun addFile(sourceFile: File, targetPath: String) {
        addFile(sourceFile.inputStream(), targetPath)
    }

    private fun addFile(sourceFile: FileInputStream, targetPath: String) {

        val entry = JarEntry(targetPath)

        entry.time = timestamp

        jarOutputStream.putNextEntry(entry)

        if (entry.isDirectory) {
            //skip
            return
        }

        val sha1 = MessageDigest.getInstance("SHA1")

        val buffer = ByteArray(2048)

        val calcSHA1 = (
                targetPath != JarFile.MANIFEST_NAME && targetPath != CERT_SF_NAME &&
                        targetPath != CERT_RSA_NAME && targetPath != OTACERT_NAME &&
                        (!stripPattern.matcher(targetPath).matches())
                )

        var readLen = sourceFile.read(buffer)

        while (readLen > 0) {
            jarOutputStream.write(buffer, 0, readLen)

            if (calcSHA1) {
                sha1.update(buffer, 0, readLen)
            }
            readLen = sourceFile.read(buffer)
        }


        val attr = Attributes()
        attr.putValue("SHA1-Digest", Base64.encode(sha1.digest()))

        manifest.entries[targetPath] = attr

    }


    companion object {
        private const val CERT_SF_NAME = "META-INF/CERT.SF"
        private const val CERT_RSA_NAME = "META-INF/CERT.RSA"

        private const val OTACERT_NAME = "META-INF/com/android/otacert"


        // Files matching this pattern are not copied to the output.
        private val stripPattern = Pattern.compile("^META-INF/(.*)[.](SF|RSA|DSA)$")

    }

}