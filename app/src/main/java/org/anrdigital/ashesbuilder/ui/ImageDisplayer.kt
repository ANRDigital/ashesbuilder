package org.anrdigital.ashesbuilder.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import org.anrdigital.ashesbuilder.game.Card
import org.anrdigital.ashesbuilder.util.ImageDownloadUtil
import java.io.File

class ImageDisplayer {
    private var mContext: Context? = null
    private var mImageView: ImageView? = null
    fun fillImageWithCard(
        imageView: ImageView,
        card: Card,
        context: Context,
        small: Boolean
    ) {
        mContext = context
        mImageView = imageView

        // Get the image in a thread and display in the ImageView
        var theImage: Bitmap? = null
        //todo: don't rely on exceptions - rewrite to check if file exists (this used to return nulls in java).
        try {
            theImage = if (small)
                getSmallImage(context, card.imageFileName)
            else
                getImage(context, card.imageFileName)
        }
        catch (e: java.lang.Exception){
            Log.e(this.javaClass.simpleName, "Unable to read image file: ${card.imageFileName}")
        }


        if (theImage != null) {
            imageView.setImageBitmap(theImage)
        } else {
            // Remove the image prior to download
            imageView.setImageResource(
                context.resources
                    .getIdentifier("card_back", "drawable", context.packageName)
            )

            // Download
            singleCardDownloader =
                SingleCardDownloader()
            singleCardDownloader!!.execute(
                card
            )
        }
    }

    inner class SingleCardDownloader :
        AsyncTask<Card?, Void?, Bitmap?>() {
        override fun doInBackground(vararg params: Card?): Bitmap? {
            val card = params[0]
            return try {
                ImageDownloadUtil.downloadImageToCache(
                    mContext!!,
                    card?.imageSrc!!,
                    card?.imageFileName
                )
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            // Only display if still in the queue
            if (!this.isCancelled) mImageView!!.setImageBitmap(result)
            //mImageView.setVisibility(View.VISIBLE);
        }
    }

    companion object {
        private var singleCardDownloader: SingleCardDownloader? =
            null

        fun fill(
            imageView: ImageView,
            card: Card?,
            context: Context
        ) {
            if (card == null) return
            val im =
                ImageDisplayer()
            im.fillImageWithCard(imageView, card, context, false)
        }

        fun fillSmall(
            imageView: ImageView,
            card: Card?,
            context: Context
        ) {
            if (card == null) return
            val im =
                ImageDisplayer()
            im.fillImageWithCard(imageView, card, context, true)
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