import processing.core.PApplet
import kotlin.math.abs
import kotlin.random.Random

/**
 * Repräsentiert einen Kreis.
 */
internal class Round : Form {
    private var _radius = 0.0f
    var radius: Float
        get() = _radius
        set(value) {
            _radius = value.coerceAtLeast(10f)
        }

    /**
     * Standardkonstruktor für einen Kreis mit randomen Werten und grafischen Eigenschaften
     */
    constructor(processing: PApplet) : super(processing) {
        val rand = Random
        this.radius = rand.nextFloat() * 50 + 10
    }

    /**
     * Berechnet die Fläche des Kreises.
     */
    override fun countArea(): Float {
        return (Math.PI * radius * radius).toFloat()
    }

    /**
     * Zeichnet den Kreis im Processing-Fenster.
     * Zentrum des Kreises wird verschoben, um bessere Bounds zu erhalten.
     */
    override fun draw() {
        processing.apply {
            stroke(borderColor)
            strokeWeight(borderWidth)
            fill(fillColor)
            circle(x + radius, y + radius, radius * 2)
        }
    }


    override fun showAtts() {
        super.showAtts()
        println("Radius: $radius")
    }

    /**
     * Verbesserte Bounds-Funktion,
     * die richtig runde Grenzen eines Kreises berechnet
     */

    override fun getBounds(): Bounds {
        return Bounds(
            x,
            x + radius * 2,
            y,
            y + radius * 2
        )
    }

    /**
     * Override isMouseOver, weil Runde Selektion nötig
     */


    override fun isMouseOver(mouseX: Float, mouseY: Float, scale: Float): Boolean {
        val mouseXScaled = mouseX / scale
        val mouseYScaled = mouseY / scale

        val centerX = x + radius
        val centerY = y + radius
        val distance = kotlin.math.sqrt(
            (mouseXScaled - centerX) * (mouseXScaled - centerX) +
                    (mouseYScaled - centerY) * (mouseYScaled - centerY)
        )

        return distance <= radius
    }

    /**
    * Auch override, damit die runde Grenze angezeigt wird
     */
    override fun drawSelectionHighlight() {
        processing.apply{
            pushStyle()
            noFill()
            stroke(0f,0f,255f)
            strokeWeight(1f)

            circle(x + radius, y + radius, radius * 2)

            fill(255f)
            stroke(0f,0f,255f)
            strokeWeight(1f)

            rect(x + radius - handleSize/2, y - handleSize/2, handleSize, handleSize)
            rect(x + radius * 2 - handleSize/2, y + radius - handleSize/2, handleSize, handleSize)
            rect(x + radius - handleSize/2, y + radius * 2 - handleSize/2, handleSize, handleSize)
            rect(x - handleSize/2, y + radius - handleSize/2, handleSize, handleSize)

            popStyle()
        }
    }

    override fun getResizeHandle(mouseX: Float, mouseY: Float, scale: Float): ResizeHandle? {
        val mouseXScaled = mouseX / scale
        val mouseYScaled = mouseY / scale

        val centerX = x + radius
        val centerY = y + radius
        return when {
            // Top handle
            isOverHandle(mouseXScaled, mouseYScaled, centerX, y) -> ResizeHandle.TOP_LEFT
            // Right handle
            isOverHandle(mouseXScaled, mouseYScaled, x + radius * 2, centerY) -> ResizeHandle.TOP_RIGHT
            // Bottom handle
            isOverHandle(mouseXScaled, mouseYScaled, centerX, y + radius * 2) -> ResizeHandle.BOTTOM_RIGHT
            // Left handle
            isOverHandle(mouseXScaled, mouseYScaled, x, centerY) -> ResizeHandle.BOTTOM_LEFT
            else -> null
        }
    }

    /**
     *  Überschriebene Methode zur Größenänderung.
     *
     */
    override fun resize(handle: ResizeHandle, dx: Float, dy: Float) {
        val delta = maxOf(abs(dx), abs(dy)) * if (dx + dy > 0) 1 else -1

        when (handle) {
            ResizeHandle.BOTTOM_RIGHT -> {
                radius = (radius + delta/2).coerceAtLeast(10f)
            }
            ResizeHandle.BOTTOM_LEFT -> {
                val oldDiameter = radius * 2
                radius = (radius - delta/2).coerceAtLeast(10f)
                x += oldDiameter - radius * 2
            }
            ResizeHandle.TOP_RIGHT -> {
                val oldDiameter = radius * 2
                radius = (radius + delta/2).coerceAtLeast(10f)
                y -= radius * 2 - oldDiameter
            }
            ResizeHandle.TOP_LEFT -> {
                val oldDiameter = radius * 2
                radius = (radius - delta/2).coerceAtLeast(10f)
                x += oldDiameter - radius * 2
                y += oldDiameter - radius * 2
            }
        }
    }
}