import processing.core.PApplet
import kotlin.random.Random

/**
 * Repräsentiert einen Kreis.
 */
internal class Round : Form {
    var radius = 0.0f // Radius des Kreises

    /**
     * Konstruktor für einen Kreis mit Koordinaten, Radius und grafischen Eigenschaften.
     */
    constructor(processing: PApplet, x: Float, y: Float, radius: Float, color: Int, borderColor: Int, fillColor: Int)
            : super(processing, x, y, color, borderColor, fillColor) {
        this.radius = radius
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
     * Zeichnet den Kreis im Processing-Fenster
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
     * die richtig quadratische Grenzen eines Kreises berechnet
     */

    override fun getBounds(): Bounds {
        return Bounds(
            x,
            x + radius * 2,
            y,
            y + radius * 2
        )
    }


}