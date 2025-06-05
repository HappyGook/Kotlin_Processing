import processing.core.PApplet
import kotlin.random.Random

/**
 * Repräsentiert ein Rechteck.
 */
internal open class Rectangle : Form {
    var side_a = 0.0f
    var side_b = 0.0f

    /**
     * Konstruktor für ein Rechteck mit Koordinaten, Seitenlängen und grafischen Eigenschaften.
     */
    constructor(processing: PApplet, x: Float, y: Float, side_a: Float, side_b: Float, color: Int, borderColor: Int, fillColor: Int)
            : super(processing, x, y, color, borderColor, fillColor) {
        this.side_a = side_a
        this.side_b = side_b
    }

    /**
     * Standardkonstruktor für ein Rechteck mit randomen Werten und grafischen Eigenschaften
     */
    constructor(processing: PApplet) : super(processing) {
        val rand = Random
        this.side_a = rand.nextFloat() * 100 + 30
        this.side_b = rand.nextFloat() * 60 + 20
    }

    /**
     * Berechnet die Fläche des Rechtecks.
     */
    override fun countArea(): Float {
        return side_a * side_b
    }

    /**
     * Zeichnet das Rechteck im Processing-Fenster
     */
    override fun draw() {
        processing.apply {
            stroke(borderColor)
            strokeWeight(borderWidth)
            fill(fillColor)
            rect(x, y, side_a, side_b)
        }
    }

    override fun showAtts() {
        super.showAtts()
        println("Length: $side_a\nWidth: $side_b")
    }
    override fun getBounds(): Bounds {
        return Bounds(
            x,
            x + side_a,
            y,
            y + side_b
        )
    }

}