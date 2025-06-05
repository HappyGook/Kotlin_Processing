import processing.core.PApplet
import kotlin.math.abs

/**
 * Verwaltet die Formen in der Anwendung.
 * Diese Klasse ist verantwortlich für die Erstellung, Verwaltung und Manipulation von Formen.
 */

class FormManager(private val processing: PApplet) {
    private val forms = mutableListOf<Form?>()
    private val selectedForms = mutableSetOf<Form>()
    private var activeResizeHandle: Form.ResizeHandle? = null
    private var resizingForm: Form? = null
    private var draggingForm: Form? = null

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

    fun handleDrag(dx: Float, dy: Float, mouseX: Float, mouseY: Float, scale: Float, windowWidth: Int, windowHeight: Int) {
        if (resizingForm != null && activeResizeHandle != null) {
            when (resizingForm) {
                is Rectangle -> handleRectangleResize(resizingForm as Rectangle, dx, dy)
                is Square -> handleSquareResize(resizingForm as Square, dx, dy)
                is Round -> handleRoundResize(resizingForm as Round, dx, dy)
            }
            resizingForm?.constrainToWindow(windowWidth, windowHeight)
        } else if (draggingForm != null) {
            draggingForm?.onDrag(dx, dy)
            draggingForm?.constrainToWindow(windowWidth, windowHeight)
        }
    }

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

    fun adjustSelectedFormsColor(component: Char, increase: Boolean) {
        selectedForms.forEach { it.adjustColor(component, increase) }
    }

    fun adjustSelectedFormsBorder(component: Float){
        selectedForms.forEach { it.adjustBorder(component) }
    }

    fun drawForms(scale: Float) {
        forms.forEach { it?.draw() }
        selectedForms.forEach { it.drawSelectionHighlight() }
    }

    fun removeSelectedForms() {
        forms.removeAll { it in selectedForms }
        selectedForms.clear()
    }
    private fun handleRectangleResize(rectangle: Rectangle, dx: Float, dy: Float) {
        when (activeResizeHandle) {
            Form.ResizeHandle.BOTTOM_RIGHT -> {
                rectangle.side_a = (rectangle.side_a + dx).coerceAtLeast(20f)
                rectangle.side_b = (rectangle.side_b + dy).coerceAtLeast(20f)
            }
            Form.ResizeHandle.BOTTOM_LEFT -> {
                val oldWidth = rectangle.side_a
                rectangle.side_a = (rectangle.side_a - dx).coerceAtLeast(20f)
                rectangle.x += oldWidth - rectangle.side_a
                rectangle.side_b = (rectangle.side_b + dy).coerceAtLeast(20f)
            }
            Form.ResizeHandle.TOP_RIGHT -> {
                rectangle.side_a = (rectangle.side_a + dx).coerceAtLeast(20f)
                val oldHeight = rectangle.side_b
                rectangle.side_b = (rectangle.side_b - dy).coerceAtLeast(20f)
                rectangle.y += oldHeight - rectangle.side_b
            }
            Form.ResizeHandle.TOP_LEFT -> {
                val oldWidth = rectangle.side_a
                val oldHeight = rectangle.side_b
                rectangle.side_a = (rectangle.side_a - dx).coerceAtLeast(20f)
                rectangle.side_b = (rectangle.side_b - dy).coerceAtLeast(20f)
                rectangle.x += oldWidth - rectangle.side_a
                rectangle.y += oldHeight - rectangle.side_b
            }
            null -> {}
        }
    }
    private fun handleSquareResize(square: Square, dx: Float, dy: Float) {

        val delta = maxOf(abs(dx), abs(dy)) * if (dx + dy > 0) 1 else -1

        when (activeResizeHandle) {
            Form.ResizeHandle.BOTTOM_RIGHT -> {
                square.side = (square.side + delta).coerceAtLeast(20f)
            }
            Form.ResizeHandle.BOTTOM_LEFT -> {
                val oldSize = square.side
                square.side = (square.side - delta).coerceAtLeast(20f)
                square.x += oldSize - square.side
            }
            Form.ResizeHandle.TOP_RIGHT -> {
                val oldSize = square.side
                square.side = (square.side + delta).coerceAtLeast(20f)
                square.y -= square.side - oldSize
            }
            Form.ResizeHandle.TOP_LEFT -> {
                val oldSize = square.side
                square.side = (square.side - delta).coerceAtLeast(20f)
                square.x += oldSize - square.side
                square.y += oldSize - square.side
            }
            null -> {}
        }
    }
    private fun handleRoundResize(round: Round, dx: Float, dy: Float) {

        val delta = maxOf(abs(dx), abs(dy)) * if (dx + dy > 0) 1 else -1

        when (activeResizeHandle) {
            Form.ResizeHandle.BOTTOM_RIGHT -> {
                round.radius = (round.radius + delta/2).coerceAtLeast(10f)
            }
            Form.ResizeHandle.BOTTOM_LEFT -> {
                val oldDiameter = round.radius * 2
                round.radius = (round.radius - delta/2).coerceAtLeast(10f)
                round.x += oldDiameter - round.radius * 2
            }
            Form.ResizeHandle.TOP_RIGHT -> {
                val oldDiameter = round.radius * 2
                round.radius = (round.radius + delta/2).coerceAtLeast(10f)
                round.y -= round.radius * 2 - oldDiameter
            }
            Form.ResizeHandle.TOP_LEFT -> {
                val oldDiameter = round.radius * 2
                round.radius = (round.radius - delta/2).coerceAtLeast(10f)
                round.x += oldDiameter - round.radius * 2
                round.y += oldDiameter - round.radius * 2
            }
            null -> {}
        }
    }

}








