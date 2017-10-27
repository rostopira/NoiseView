package pl.hypeapp.noiseview

import android.graphics.*

@Suppress("MemberVisibilityCanPrivate")
internal class RasterNoise(override var bitmap: Bitmap?,
                           override var grainFps: Int,
                           override var scale: Float,
                           intensity: Float): NoiseRenderable {

    private val paint = Paint()

    private var shader: BitmapShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

    private var matrix: Matrix = Matrix()

    private var lastGrainOffset: Long

    override var noiseIntensity: Float = intensity
        set(value) {
            paint.alpha = (255f * value).toInt()
        }

    init {
        shader.setLocalMatrix(matrix)
        paint.shader = shader
        paint.alpha = 144
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        lastGrainOffset = System.currentTimeMillis()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPaint(paint)
    }

    override fun update() {
        if (lastGrainOffset + grainFps < System.currentTimeMillis()) {
            matrix.reset()
            matrix.setScale(scale, scale)
            bitmap?.let {
                matrix.postTranslate(MathHelper.randomRange(-it.width * 10f, it.width * 10f),
                        MathHelper.randomRange(-it.height * 10f, it.height * 10f))
            }
            shader.setLocalMatrix(matrix)
            lastGrainOffset = System.currentTimeMillis()
        }
    }

}
