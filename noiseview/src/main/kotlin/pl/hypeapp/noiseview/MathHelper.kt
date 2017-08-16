package pl.hypeapp.noiseview

import java.util.*

internal object MathHelper {

    internal var random = Random()

    internal fun randomRange(min: Float, max: Float): Float {
        val randomNum = random.nextInt(max.toInt() - min.toInt() + 1) + min.toInt()
        return randomNum.toFloat()
    }

}
