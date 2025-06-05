import processing.core.PApplet
import kotlin.random.Random

/**
 * Superklasse für alle Formen. Enthält gemeinsame Attribute und Methoden, die dann überschrieben werden
 */
abstract class Form {
    var x = 0.0f
    var y = 0.0f
    var color = color(255, 255, 255)
    var borderColor = color(0, 0, 0)
    var fillColor = color(128, 128, 128)
    var borderWidth = 1f
    protected var processing: PApplet

    /**
     * Konstruktor für die Form mit gegebenen Koordinaten und grafischen Eigenschaften.
     */
    constructor(processing: PApplet, x: Float, y: Float, color: Int, borderColor: Int, fillColor: Int) {
        this.processing = processing
        this.x = x
        this.y = y
        this.color = color
        this.borderColor = borderColor
        this.fillColor = fillColor
    }

    /**
     * Standardkonstruktor mit randomen Werten und zufälligen Farben
     */
    constructor(processing: PApplet) {
        this.processing = processing
        val rand = Random
        this.x = rand.nextFloat() * processing.width
        this.y = rand.nextFloat() * processing.height
        this.color = getRandomColor(processing)
        this.borderColor = getRandomColor(processing)
        this.fillColor = getRandomColor(processing)
    }

    open fun isMouseOver(mouseX: Float, mouseY: Float, scale: Float): Boolean {
        val bounds = getBounds()
        val mouseXScaled = mouseX / scale
        val mouseYScaled = mouseY / scale

        return mouseXScaled >= bounds.left &&
                mouseXScaled <= bounds.right &&
                mouseYScaled >= bounds.top &&
                mouseYScaled <= bounds.bottom
    }

    open fun onDrag(dx: Float, dy: Float) {
        x += dx
        y += dy
    }


    open fun constrainToWindow(windowWidth: Int, windowHeight: Int) {
        val bounds = getBounds()
        val width = bounds.right - bounds.left
        val height = bounds.bottom - bounds.top

        // Constrain x and y so that the shape stays within the window
        x = x.coerceIn(0f, (windowWidth - width).coerceAtLeast(0f))
        y = y.coerceIn(0f, (windowHeight - height).coerceAtLeast(0f))
    }


    protected val handleSize = 8f
    open fun drawSelectionHighlight() {
        processing.apply {
            pushStyle()
            noFill()
            stroke(0f, 0f, 255f)
            strokeWeight(1f)

            val bounds = getBounds()
            rect(
                bounds.left,
                bounds.top,
                bounds.right - bounds.left,
                bounds.bottom - bounds.top
            )

            fill(255f)
            stroke(0f, 0f, 255f)
            strokeWeight(1f)


            rect(bounds.left - handleSize/2, bounds.top - handleSize/2, handleSize, handleSize)

            rect(bounds.right - handleSize/2, bounds.top - handleSize/2, handleSize, handleSize)

            rect(bounds.left - handleSize/2, bounds.bottom - handleSize/2, handleSize, handleSize)

            rect(bounds.right - handleSize/2, bounds.bottom - handleSize/2, handleSize, handleSize)

            popStyle()
        }
    }

    fun adjustColor(component: Char, increase: Boolean) {
        val r = processing.red(fillColor)
        val g = processing.green(fillColor)
        val b = processing.blue(fillColor)

        val change = if (increase) 10 else -10

        fillColor = when (component.lowercaseChar()) {
            'r' -> processing.color(maxOf(0f, minOf(255f, r + change)), g, b)
            'g' -> processing.color(r, maxOf(0f, minOf(255f, g + change)), b)
            'b' -> processing.color(r, g, maxOf(0f, minOf(255f, b + change)))
            else -> fillColor
        }
    }

    fun adjustBorder(component: Float) {
        borderWidth = component
    }

    fun getResizeHandle(mouseX: Float, mouseY: Float, scale: Float): ResizeHandle? {
        val bounds = getBounds()
        val mouseXScaled = mouseX / scale
        val mouseYScaled = mouseY / scale

        return when {
            isOverHandle(mouseXScaled, mouseYScaled, bounds.left, bounds.top) -> ResizeHandle.TOP_LEFT
            isOverHandle(mouseXScaled, mouseYScaled, bounds.right, bounds.top) -> ResizeHandle.TOP_RIGHT
            isOverHandle(mouseXScaled, mouseYScaled, bounds.left, bounds.bottom) -> ResizeHandle.BOTTOM_LEFT
            isOverHandle(mouseXScaled, mouseYScaled, bounds.right, bounds.bottom) -> ResizeHandle.BOTTOM_RIGHT
            else -> null
        }
    }

    private fun isOverHandle(mouseX: Float, mouseY: Float, handleX: Float, handleY: Float): Boolean {
        return mouseX >= handleX - handleSize/2 &&
                mouseX <= handleX + handleSize/2 &&
                mouseY >= handleY - handleSize/2 &&
                mouseY <= handleY + handleSize/2
    }

    enum class ResizeHandle {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }



    /**
     * abstrakte Methode für Grenzen
     */
    abstract fun getBounds(): Bounds

    /**
     * Berechnet die Fläche der Form. Form hat keine Fläche in sich selbst, also nur für override
     */
    abstract fun countArea(): Float

    /**
     * Ausgabefunktion
     */
    open fun showAtts() {
        println("X-Coordinate: $x,\nY-Coordinate: $y,\nArea: ${countArea()}")
    }

    private fun getRandomColor(p: PApplet): Int {
        return p.color(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }

    abstract fun draw()

    companion object {
        fun color(r: Int, g: Int, b: Int): Int {
            return (r shl 16) or (g shl 8) or b
        }
    }
}