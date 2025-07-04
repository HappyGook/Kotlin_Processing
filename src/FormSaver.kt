import java.io.File
import java.io.IOException
import java.text.ParseException
import processing.core.PApplet
import kotlin.jvm.Throws

/**
 * Verwaltet das Speichern und Laden von Formen in bzw. aus Dateien.
 * Unterstützt die Formate: Round, Square und Rectangle.
 *
 * @property processing Die Processing-Instanz zur Farberzeugung bei Bedarf.
 */
class FormSaver(private val processing: PApplet) {
    /**
     * Speichert die übergebene Liste von Formen in einer Datei.
     *
     * @param forms Liste der zu speichernden Formen
     * @param filename Name der Zieldatei
     * @throws IOException Wenn ein Fehler beim Schreiben in die Datei auftritt
     */
    @Throws(IOException::class)
    fun saveFormsToFile(forms: List<Form?>, filename: String){
        File(filename).printWriter().use { out ->
            forms.filterNotNull().forEach { form ->
                val line = when (form) {
                    is Round -> "round|${form.x}|${form.y}|${form.radius}|${form.fillColor}|${form.borderWidth}|${form.borderColor}"
                    is Square -> "square|${form.x}|${form.y}|${form.sideA}|${form.fillColor}|${form.borderWidth}|${form.borderColor}"
                    is Rectangle -> "rectangle|${form.x}|${form.y}|${form.sideA}|${form.sideB}|${form.fillColor}|${form.borderWidth}|${form.borderColor}"
                    else -> throw IllegalArgumentException("Unknown form type found")
                }
                out.println(line)
            }
        }
    }

    /**
     * Lädt Formen aus einer Datei und gibt sie als Liste zurück.
     *
     * @param filename Name der Quelldatei
     * @return Liste der geladenen Formen
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt
     * @throws ParseException Wenn das Dateiformat ungültig oder unvollständig ist
     */
    @Throws(IOException::class, ParseException::class)
    fun loadFormsFromFile(filename: String): List<Form>{
        val forms = mutableListOf<Form>()
        File(filename).forEachLine { line ->
            val parts = line.split("|")
            val form = when (parts[0]) {
                "round"-> {if(parts.size != 7) throw ParseException("Wrong format for Round",0)
                Round(processing).apply {
                    x=parts[1].toFloat()
                    y=parts[2].toFloat()
                    radius=parts[3].toFloat()
                    fillColor=parts[4].toInt()
                    borderWidth=parts[5].toFloat()
                    borderColor=parts[6].toInt()
                }
                }
                "square"-> {if(parts.size != 7) throw ParseException("Wrong format for Square",0)
                    Square(processing).apply {
                        x=parts[1].toFloat()
                        y=parts[2].toFloat()
                        sideA=parts[3].toFloat()
                        fillColor=parts[4].toInt()
                        borderWidth=parts[5].toFloat()
                        borderColor=parts[6].toInt()
                    }
                }
                "rectangle"-> {if(parts.size != 8) throw ParseException("Wrong format for Rectangle",0)
                    Rectangle(processing).apply {
                        x=parts[1].toFloat()
                        y=parts[2].toFloat()
                        sideA=parts[3].toFloat()
                        sideB=parts[4].toFloat()
                        fillColor=parts[5].toInt()
                        borderWidth=parts[6].toFloat()
                        borderColor=parts[7].toInt()
                    }
                }
                else -> throw ParseException("Unknown form type found${parts[0]}",0)
            }
            forms.add(form)
        }
        return forms
    }
}
