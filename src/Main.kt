import processing.core.PApplet

var window_width=800
var window_height=600

/**
 * Hauptklasse der Anwendung.
 * Verwaltet das Hauptfenster und die Benutzerinteraktionen mittels Processing.
 */
class Main : PApplet() {
    private lateinit var formManager: FormManager

    /**
     * Initialisiert die Fenstereinstellungen.
     * Setzt die Fenstergröße auf die definierten Maße.
     */
    override fun settings() {
        size(window_width, window_height)
    }

    override fun setup() {
        surface.setResizable(true)
        background(240)
        formManager = FormManager(this)
        printInstructions()
    }

    /**
     * Behandelt Tastatureingaben.
     * Unterstützt folgende Befehle:
     * - 'k': Erstellt einen Kreis
     * - 'q': Erstellt ein Quadrat
     * - 'v': Erstellt ein Rechteck
     * - 'r/R', 'g/G', 'b/B': Ändert die Farbkomponenten
     * - '0-9': Ändert die Randstärke
     * - DELETE/BACKSPACE: Löscht ausgewählte Formen
     */
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

    /**
     * Behandelt Mausklick-Ereignisse.
     * Berechnet den Skalierungsfaktor für die korrekte Positionierung der Elemente.
     */
    override fun mouseClicked() {
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)


    }

    /**
     * Behandelt das Drücken der Maustaste.
     * Ermöglicht die Auswahl von Formen und initiiert Größenänderungs- oder Bewegungsoperationen.
     * Mit gedrückter SHIFT-Taste können mehrere Formen ausgewählt werden.
     */
    override fun mousePressed() {
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)

        formManager.handleMousePress(mouseX.toFloat(), mouseY.toFloat(), scale)
    }

    /**
     * Behandelt das Ziehen der Maus.
     * Ermöglicht das Verschieben ausgewählter Formen oder deren Größenänderung,
     * abhängig davon, ob ein Größenänderungsgriff aktiv ist.
     * Die Position der Formen wird auf den sichtbaren Fensterbereich beschränkt.
     */
    override fun mouseDragged() {
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)

        val dx = (mouseX - pmouseX) / scale
        val dy = (mouseY - pmouseY) / scale

        formManager.handleDrag(dx, dy, mouseX.toFloat(), mouseY.toFloat(), scale, window_width, window_height)
    }

    /**
     * Behandelt das Loslassen der Maustaste.
     * Beendet aktuelle Größenänderungs- oder Bewegungsoperationen.
     * Bei einem einfachen Klick ohne Bewegung wird die Auswahl der Form umgeschaltet.
     */
    override fun mouseReleased() {
        formManager.handleMouseRelease()
    }
    /**
     * Behandelt das Scrollen.
     * Überprüft, in welcher Richtung gescrollt wird und davon abhängig wird Delta positiv oder negativ.
     */
    override fun mouseWheel(event: processing.event.MouseEvent) {
        val scaleX = width.toFloat() / window_width
        val scaleY = height.toFloat() / window_height
        val scale = minOf(scaleX, scaleY)

        val delta = if (event.count > 0) 5f else -5f

        formManager.handleScroll(delta)
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
 * Datenklasse zur Darstellung der Grenzen einer Form.
 *
 * @property left Linke Grenze
 * @property right Rechte Grenze
 * @property top Obere Grenze
 * @property bottom Untere Grenze
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