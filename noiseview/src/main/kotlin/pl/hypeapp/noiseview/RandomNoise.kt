package pl.hypeapp.noiseview

import android.content.Context
import android.graphics.*
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.RenderScript

/** Noise drawable based on RenderScript **/

internal class RandomNoise(ctx: Context): NoiseRenderable {

    private val rs = RenderScript.create(ctx)
    private val script = ScriptC_noise(rs)
    private val paint = Paint().apply {
        color = Color.WHITE
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    }
    private var matrix = Matrix()
    private var width = 0
        get() = Math.round(field * scale)
        set(value) { // TODO: rewrite repeating setter with delegate
            if (field != value)
                destroy()
            field = value
        }
    private var height = 0
        get() = Math.round(field * scale)
        set(value) {
            if (field != value)
                destroy()
            field = value
        }

    override var bitmap: Bitmap? = null

    /** Here - probability of noise dot appearing **/
    override var noiseIntensity = 1f
        set(value) {
            if (value !in 0f..1f)
                throw IllegalArgumentException("Intensity must be in 0f..1f")
            field = value / 10
            script._intensity = Math.round(25.5f * value).toShort()
        }

    private var lastGrainOffset = System.currentTimeMillis()
    override var grainFps = 0

    /** That's obvious **/
    var colored = false
        set(value) {
            if (field != value)
                destroy()
            field = value
        }

    override var scale = 1f
        set(value) {
            if (field != value)
                destroy()
            field = value
            matrix.setScale(1f / value, 1f / value)
        }

    override fun draw(canvas: Canvas) {
        width = canvas.width
        height = canvas.height
        if (bitmap == null)
            update()
        canvas.drawBitmap(bitmap, matrix, paint)
    }

    //TODO: check for race-condition issues
    override fun update() {
        if (bitmap != null && lastGrainOffset + grainFps > System.currentTimeMillis())
            return
        val b = bitmap ?: Bitmap.createBitmap(width, height,
            if (colored)
                Bitmap.Config.ARGB_8888
            else
                Bitmap.Config.ALPHA_8
        )
        bitmap = b
        val alloc = Allocation.createFromBitmap(rs, b)
        if (colored)
            script.forEach_color(alloc, alloc)
        else
            script.forEach_mono(alloc, alloc)
        alloc.copyTo(b)
        lastGrainOffset = System.currentTimeMillis()
    }

}