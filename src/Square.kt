import processing.core.PApplet
import kotlin.math.abs
import kotlin.random.Random

/**
 * Repräsentiert ein Quadrat
 * ist eine spezielle Form des Rechtecks mit gleichen Seitenlängen.
 */

internal class Square : Rectangle {

    /**
     * Standardkonstruktor für ein Quadrat mit randomen Werten
     */
    constructor(processing: PApplet) : super(processing) {
        val rand = Random
        val side = rand.nextFloat() * 80 + 20
        sideA = side
        sideB = side
    }

    var side: Float
        get() = sideA
        set(value) {
            sideA = value
            sideB = value
        }

    override fun showAtts() {
        super.showAtts()
        println("Side length: $side")
    }

    /**
     *  Überschriebene Methode zur Größenänderung.
     *
     */
    override fun resize(handle: ResizeHandle, dx: Float, dy: Float) {
        val delta = maxOf(abs(dx), abs(dy)) * if (dx + dy > 0) 1 else -1

        when (handle) {
            ResizeHandle.BOTTOM_RIGHT -> {
                side = (side + delta).coerceAtLeast(20f)
            }
            ResizeHandle.BOTTOM_LEFT -> {
                val oldSize = side
                side = (side - delta).coerceAtLeast(20f)
                x += oldSize - side
            }
            ResizeHandle.TOP_RIGHT -> {
                val oldSize = side
                side = (side + delta).coerceAtLeast(20f)
                y -= side - oldSize
            }
            ResizeHandle.TOP_LEFT -> {
                val oldSize = side
                side = (side - delta).coerceAtLeast(20f)
                x += oldSize - side
                y += oldSize - side
            }
        }
    }
}
