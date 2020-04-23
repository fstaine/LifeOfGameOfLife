package fr.fstaine.lifeofgameoflife.gui

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Slider
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

class OptionView : Pane() {
    private val root = VBox()

    private val delaySlider = Slider(2.0, 500.0, 200.0)
    val delayProperty = SimpleIntegerProperty(200)

    init {
        this.children.add(delaySlider)
    }
}
