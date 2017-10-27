package pl.hypeapp.noiseview

import android.graphics.Bitmap
import android.graphics.Canvas

interface NoiseRenderable {

    var bitmap: Bitmap?
    var noiseIntensity: Float
    var grainFps: Int
    var scale: Float

    fun draw(canvas: Canvas)

    fun update()

    fun destroy() = bitmap?.let {
        if (!it.isRecycled) {
            it.recycle()
            bitmap = null
        }
    }

}