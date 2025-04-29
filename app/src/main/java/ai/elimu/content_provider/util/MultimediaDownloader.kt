package ai.elimu.content_provider.util

import android.util.Log
import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object MultimediaDownloader {
    
    private const val TAG = "MultimediaDownloader"
    
    fun downloadFileBytes(urlValue: String): ByteArray? {
        Log.i(TAG, "downloadFileBytes")

        Log.i(TAG, "Downloading from $urlValue")

        var bytes: ByteArray? = null

        try {
            val url = URL(urlValue)

            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.connect()

            val responseCode = httpURLConnection.responseCode
            Log.i(
                TAG,
                "responseCode: $responseCode"
            )
            val inputStream: InputStream?
            if (responseCode == 200) {
                inputStream = httpURLConnection.inputStream
                bytes = IOUtils.toByteArray(inputStream)
            } else {
                inputStream = httpURLConnection.errorStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var response = ""
                var line: String
                while ((bufferedReader.readLine().also { line = it }) != null) {
                    response += line
                }
                Log.e(
                    TAG,
                    "responseCode: $responseCode, response: $response"
                )
            }
        } catch (e: IOException) {
            Log.e(TAG, null, e)
        }

        return bytes
    }
}
