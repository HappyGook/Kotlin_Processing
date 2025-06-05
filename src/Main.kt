import processing.core.PApplet

var window_width=800
var window_height=600

//Hauptklasse für Processing
class Main : PApplet() {
    private lateinit var formManager: FormManager

    override fun settings() {
        size(window_width, window_height)
    }

    override fun setup() {
        surface.setResizable(true)
        background(240)
        formManager = FormManager(this)
        printInstructions()
    }

    override fun keyPressed() {
        when (key) {
            'k', 'q', 'v' -> formManager.createForm(key)
            'r', 'R', 'g', 'G', 'b', 'B' -> {
                formManager.adjustSelectedFormsColor(
                    key.lowercaseChar(),
                    key.isUpperCase()
                )
            }
            '0','1','2','3','4','5','6','7','8','9' -> {
                formManager.adjustSelectedFormsBorder(key.code.toFloat()-48)
            }
        }

        if (keyCode.toChar() == DELETE || keyCode.toChar() == BACKSPACE) {
            formManager.removeSelectedForms()
        }
    }

    override fun mouseClicked() {
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)


    }

    override fun mousePressed() {
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)

        formManager.handleMousePress(mouseX.toFloat(), mouseY.toFloat(), scale)
    }

    override fun mouseDragged() {
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)

        val dx = (mouseX - pmouseX) / scale
        val dy = (mouseY - pmouseY) / scale

        formManager.handleDrag(dx, dy, mouseX.toFloat(), mouseY.toFloat(), scale, window_width, window_height)
    }

    override fun mouseReleased() {
        formManager.handleMouseRelease()
    }

    override fun draw() {
        background(240)
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)

        pushMatrix()
        scale(scale)
        formManager.drawForms(scale)
        popMatrix()
    }

    private fun printInstructions() {
        println("--------Steuerung-------")
        println("k - Kreis erstellen")
        println("q - Quadrat erstellen")
        println("v - Rechteck erstellen")
        println("Klicken um Form auszuwählen")
        println("Gedrückte Form mit Maus ziehen zum Bewegen")
    }
}


/**
 * Quadratische Grenzen eines Forms
 */

data class Bounds(
    val left: Float,
    val right: Float,
    val top: Float,
    val bottom: Float
)


fun main() {
    PApplet.main("Main")
}