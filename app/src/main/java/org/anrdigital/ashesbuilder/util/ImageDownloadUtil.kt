package org.anrdigital.ashesbuilder.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ImageDownloadUtil {
    @Throws(Exception::class)
    fun downloadImageToCache(
        context: Context,
        imageSrc: URL,
        localFileName: String?
    ): Bitmap {
        return try {
            val conn = imageSrc.openConnection()
            if (conn is HttpsURLConnection) {
                conn.sslSocketFactory = trustAllSocketFactory?.socketFactory
            }
            val theImage = BitmapFactory.decodeStream(conn.getInputStream())
            //FileOutputStream out = context.openFileOutput(card.getImageFileName(), Context.MODE_PRIVATE);
            val out =
                FileOutputStream(File(context.cacheDir, localFileName))
            theImage.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            theImage
        } catch (e: Exception) {
            Log.e("LOG", "Could not download image: $imageSrc")
            throw e
        }
    }

    private val trustAllSocketFactory: SSLContext?
        private get() {
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return emptyArray()
                        }
                    }
                )
            try {
                val sc = SSLContext.getInstance("SSL")
                sc.init(null, trustAllCerts, SecureRandom())
                return sc
            } catch (ignored: Exception) {
            }
            return null
        }
}