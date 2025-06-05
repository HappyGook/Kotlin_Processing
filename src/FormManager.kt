import processing.core.PApplet

/**
 * Verwaltet die Formen in der Anwendung.
 * Diese Klasse ist verantwortlich für die Erstellung, Verwaltung und Manipulation von geometrischen Formen.
 *
 * @property processing Die Processing-Instanz für Zeichenoperationen
 * @property forms Liste aller Formen in der Anwendung
 * @property selectedForms Menge der aktuell ausgewählten Formen
 * @property activeResizeHandle Aktiver Größenänderungsgriff, falls vorhanden
 * @property resizingForm Form, die gerade in der Größe verändert wird
 * @property draggingForm Form, die gerade gezogen wird
 */
class FormManager(private val processing: PApplet) {
    private val forms = mutableListOf<Form?>()
    private val selectedForms = mutableSetOf<Form>()
    private var activeResizeHandle: Form.ResizeHandle? = null
    private var resizingForm: Form? = null
    private var draggingForm: Form? = null

    /**
     * Erstellt eine neue Form
     * @param type Typ der zu erstellenden Form ('k' für Kreis, 'q' für Quadrat, 'v' für Rechteck)
     * @return Die erstellte Form oder null, wenn der Typ nicht erkannt wurde
     */
    fun createForm(type: Char): Form? {
        val newForm = when (type) {
            'k' -> Round(processing)
            'q' -> Square(processing)
            'v' -> Rectangle(processing)
            else -> null
        }
        if (newForm != null) {
            forms.add(newForm)
        }
        return newForm
    }

    /**
     * Verarbeitet das Drücken der Maustaste.
     * Prüft, ob eine Form getroffen wurde und handhabt die Auswahl sowie die Initiierung von
     * Größenänderungs- oder Bewegungsoperationen.
     *
     * @param mouseX X-Koordinate der Maus
     * @param mouseY Y-Koordinate der Maus
     * @param scale Aktueller Skalierungsfaktor des Fensters
     */
    fun handleMousePress(mouseX: Float, mouseY: Float, scale: Float) {
        val clickedForm = forms.findLast { it?.isMouseOver(mouseX, mouseY, scale) == true }

        if (clickedForm != null) {
            // Erst checken, ob der User resizen will
            if (clickedForm in selectedForms) {
                activeResizeHandle = clickedForm.getResizeHandle(mouseX, mouseY, scale)
                if (activeResizeHandle != null) {
                    resizingForm = clickedForm
                    return
                }
            }

            // Wenn nicht, draggen
            draggingForm = clickedForm

            if (processing.keyPressed && processing.keyCode == PApplet.SHIFT) {
                if (clickedForm in selectedForms) {
                    selectedForms.remove(clickedForm)
                } else {
                    selectedForms.add(clickedForm)
                }
            } else {
                if (clickedForm !in selectedForms) {
                    selectedForms.clear()
                }
                selectedForms.add(clickedForm)
            }
        } else {
            selectedForms.clear()
        }
    }

    /**
     * Verarbeitet Mausbewegungen während des Ziehens.
     * Handhabt sowohl das Verschieben als auch die Größenänderung von Formen.
     *
     * @param dx Horizontale Bewegung der Maus
     * @param dy Vertikale Bewegung der Maus
     * @param mouseX Aktuelle X-Position der Maus
     * @param mouseY Aktuelle Y-Position der Maus
     * @param scale Aktueller Skalierungsfaktor
     * @param windowWidth Fensterbreite
     * @param windowHeight Fensterhöhe
     */
    fun handleDrag(dx: Float, dy: Float, mouseX: Float, mouseY: Float, scale: Float, windowWidth: Int, windowHeight: Int) {
        if (resizingForm != null && activeResizeHandle != null) {
            resizingForm?.resize(activeResizeHandle!!, dx, dy)
            resizingForm?.constrainToWindow(windowWidth, windowHeight)
        } else if (draggingForm != null) {
            draggingForm?.onDrag(dx, dy)
            draggingForm?.constrainToWindow(windowWidth, windowHeight)
        }
    }


    /**
     * Verarbeitet das Loslassen der Maustaste.
     * Beendet laufende Größenänderungs- oder Bewegungsoperationen und
     * aktualisiert die Formauswahl bei einfachen Klicks.
     */
    fun handleMouseRelease() {
        if (resizingForm == null && draggingForm == null) {
            // Clicked without dragging or resizing
            val clickedForm = forms.findLast { it?.isMouseOver(processing.mouseX.toFloat(), processing.mouseY.toFloat(), 1f) == true }
            if (clickedForm != null && !processing.keyPressed) {
                // Toggle selection on click without drag
                if (selectedForms.size == 1 && clickedForm in selectedForms) {
                    selectedForms.clear()
                } else {
                    selectedForms.clear()
                    selectedForms.add(clickedForm)
                }
            }
        }
        activeResizeHandle = null
        resizingForm = null
        draggingForm = null
    }

    /**
     * Passt die Farbe der ausgewählten Formen an.
     *
     * @param component Farbkomponente ('r' für Rot, 'g' für Grün, 'b' für Blau)
     * @param increase True für Erhöhung, False für Verringerung der Farbkomponente
     */
    fun adjustSelectedFormsColor(component: Char, increase: Boolean) {
        selectedForms.forEach { it.adjustColor(component, increase) }
    }

    /**
     * Ändert die Randbreite der ausgewählten Formen.
     *
     * @param component Neue Randbreite (0-9)
     */
    fun adjustSelectedFormsBorder(component: Float){
        selectedForms.forEach { it.adjustBorder(component) }
    }

    /**
     * Zeichnet alle Formen und markiert die ausgewählten Formen.
     *
     * @param scale Aktueller Skalierungsfaktor für die korrekte Darstellung
     */
    fun drawForms(scale: Float) {
        forms.forEach { it?.draw() }
        selectedForms.forEach { it.drawSelectionHighlight() }
    }

    /**
     * Entfernt ausgewählte Formen aus der Anwendung.
     */
    fun removeSelectedForms() {
        forms.removeAll { it in selectedForms }
        selectedForms.clear()
    }

    /**
     * Größenänderungen den verschiedenen Formen.
     */
}








