import processing.core.PApplet
import kotlin.random.Random

/**
 * Factory-Klasse zur Erzeugung den zufälligen Formen.
 */
class FormFactory(private val processing: PApplet) {
    var rand: Random = Random
    private val window_width = processing.width
    private val window_height = processing.height


    /**
     * Damit die Formen nicht übereinander produziert werden
     */

    //Checken, ob alle Formen overlappen
    private fun isOverlapping(newForm: Form, existingForms: Array<Form?>): Boolean {
        for (existingForm in existingForms) {
            if (existingForm != null && doFormsOverlap(newForm, existingForm)) {
                return true
            }
        }
        return false
    }

    // Formen für Overlap vergleichen
    private fun doFormsOverlap(form1: Form, form2: Form): Boolean {
        val bounds1 = form1.getBounds()
        val bounds2 = form2.getBounds()

        return !(bounds1.right < bounds2.left ||
                bounds1.left > bounds2.right ||
                bounds1.bottom < bounds2.top ||
                bounds1.top > bounds2.bottom)
    }

    private fun isWithinBounds(form: Form): Boolean {
        val bounds = form.getBounds()
        return bounds.left >= 0 &&
                bounds.right <= window_width &&
                bounds.top >= 0 &&
                bounds.bottom <= window_height
    }




    /**
     * Erzeugt ein Array von zufälligen Formen.
     *
     * @param n Die Anzahl der Formen, die erzeugt werden sollen.
     * @return Ein Array von zufälligen Formen.
     */
    fun produce(n: Int, maxAttempts: Int = 100): Array<Form?> {
        val forms: Array<Form?> = arrayOfNulls(n)

        for (i in 0 until n) {
            var attempts = 0
            var newForm: Form? = null

            while (attempts < maxAttempts) {
                // Create a new form
                val formId: Int = rand.nextInt(3)
                newForm = when (formId) {
                    0 -> Round(processing)
                    1 -> Square(processing)
                    2 -> Rectangle(processing)
                    else -> null
                }


                if (newForm != null && !isOverlapping(newForm, forms)) {
                    break
                }

                attempts++
            }

            forms[i] = newForm
        }

        return forms
    }
}