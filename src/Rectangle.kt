import processing.core.PApplet
import kotlin.random.Random

/**
 * Repräsentiert ein Rechteck.
 */
internal open class Rectangle : Form {
    var sideA = 0.0f
    var sideB = 0.0f

    /**
     * Konstruktor für ein Rechteck mit Koordinaten, Seitenlängen und grafischen Eigenschaften.
     */
    constructor(processing: PApplet, x: Float, y: Float, sideA: Float, sideB: Float, color: Int, borderColor: Int, fillColor: Int)
            : super(processing, x, y, color, borderColor, fillColor) {
        this.sideA = sideA
        this.sideB = sideB
    }

    /**
     * Standardkonstruktor für ein Rechteck mit randomen Werten und grafischen Eigenschaften
     */
    constructor(processing: PApplet) : super(processing) {
        val rand = Random
        this.sideA = rand.nextFloat() * 100 + 30
        this.sideB = rand.nextFloat() * 60 + 20
    }

    /**
     * Berechnet die Fläche des Rechtecks.
     */
    override fun countArea(): Float {
        return sideA * sideB
    }

    /**
     * Zeichnet das Rechteck im Processing-Fenster
     */
    override fun draw() {
        processing.apply {
            stroke(borderColor)
            strokeWeight(borderWidth)
            fill(fillColor)
            rect(x, y, sideA, sideB)
        }
    }

    override fun showAtts() {
        super.showAtts()
        println("Length: $sideA\nWidth: $sideB")
    }
    override fun getBounds(): Bounds {
        return Bounds(
            x,
            x + sideA,
            y,
            y + sideB
        )
    }

    /**
     *  Überschriebene Methode zur Größenänderung.
     *
     * Behandelt jede Grenze des Vierecks einzeln.
     */
    override fun resize(handle: ResizeHandle, dx: Float, dy: Float) {
        when (handle) {
            ResizeHandle.BOTTOM_RIGHT -> {
                sideA = (sideA + dx).coerceAtLeast(20f)
                sideB = (sideB + dy).coerceAtLeast(20f)
            }
            ResizeHandle.BOTTOM_LEFT -> {
                val oldWidth = sideA
                sideA = (sideA - dx).coerceAtLeast(20f)
                x += oldWidth - sideA
                sideB = (sideB + dy).coerceAtLeast(20f)
            }
            ResizeHandle.TOP_RIGHT -> {
                sideA = (sideA + dx).coerceAtLeast(20f)
                val oldHeight = sideB
                sideB = (sideB - dy).coerceAtLeast(20f)
                y += oldHeight - sideB
            }
            ResizeHandle.TOP_LEFT -> {
                val oldWidth = sideA
                val oldHeight = sideB
                sideA = (sideA - dx).coerceAtLeast(20f)
                sideB = (sideB - dy).coerceAtLeast(20f)
                x += oldWidth - sideA
                y += oldHeight - sideB
            }
        }
    }
}