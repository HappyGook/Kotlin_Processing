import processing.core.PApplet
import kotlin.random.Random

/**
 * Repräsentiert ein Quadrat
 * Quadrat ist eine spezielle Form des Rechtecks mit gleichen Seitenlängen.
 */

internal class Square : Rectangle {
    /**
     * Konstruktor für ein Quadrat mit Koordinaten, Seitenlänge und grafischen Eigenschaften.
     */
    constructor(processing: PApplet, x: Float, y: Float, side: Float, color: Int, borderColor: Int, fillColor: Int)
            : super(processing, x, y, side, side, color, borderColor, fillColor)

    /**
     * Standardkonstruktor für ein Quadrat mit randomen Werten
     */
    constructor(processing: PApplet) : super(processing) {
        val rand = Random
        val side = rand.nextFloat() * 80 + 20
        side_a = side
        side_b = side
    }

    var side: Float
        get() = side_a
        set(value) {
            side_a = value
            side_b = value
        }

    override fun showAtts() {
        super.showAtts()
        println("Side length: $side")
    }
}
