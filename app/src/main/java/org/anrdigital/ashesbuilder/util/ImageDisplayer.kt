package org.anrdigital.ashesbuilder.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.anrdigital.ashesbuilder.game.Card
import java.io.File
import java.lang.Exception
import java.net.URL

class ImageDisplayer {
    private var mContext: Context? = null
    private var mImageView: ImageView? = null
    fun fillImageViewWithCardImage(
        imageView: ImageView,
        card: Card,
        context: Context,
        small: Boolean
    ) {
        mContext = context
        mImageView = imageView

        val imageFileName: String = if (small) card.smallImageFileName else card.imageFileName
        val sourceUrl: URL = if (small) card.smallImageSrc else card.imageSrc

        // Get the image in a thread and display in the ImageView
        var result: Bitmap? = null
        if (File(context.cacheDir, imageFileName).exists())
            try {
                result = getImage(context, imageFileName)
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Unable to read image file: $imageFileName")
            }

        if (result != null) {
            imageView.setImageBitmap(result)
        } else {
            // Remove the image prior to download
            imageView.setImageResource(
                context.resources
                    .getIdentifier("card_back", "drawable", context.packageName)
            )

            // coroutine download
            val uiScope = CoroutineScope(Dispatchers.Main)
            uiScope.launch {
                fetchImage(card, sourceUrl, imageFileName)
            }
        }
    }

    private suspend fun fetchImage(card: Card, source: URL, destination: String) {
        withContext(Dispatchers.IO) {
            val result =  ImageDownloadUtil.downloadImageToCache(
                mContext!!,
                source,
                destination
            )

            withContext(Dispatchers.Main){
                mImageView!!.setImageBitmap(result)
            }
        }
    }

    companion object {
        fun fill(
            imageView: ImageView,
            card: Card?,
            context: Context
        ) {
            if (card == null) return
            val im =
                ImageDisplayer()
            im.fillImageViewWithCardImage(imageView, card, context, false)
        }

        fun fillSmall(
            imageView: ImageView,
            card: Card?,
            context: Context
        ) {
            if (card == null) return
            val im =
                ImageDisplayer()
            im.fillImageViewWithCardImage(imageView, card, context, true)
        }

        fun getImage(context: Context, imageFileName: String?): Bitmap {
            return BitmapFactory.decodeFile(
                File(context.cacheDir, imageFileName).absolutePath
            )
        }

        fun getSmallImage(context: Context, imageFileName: String?): Bitmap {
            val options = BitmapFactory.Options()
            options.inSampleSize = 4
            return BitmapFactory.decodeFile(
                File(context.cacheDir, imageFileName).absolutePath, options
            )
        }
    }
}