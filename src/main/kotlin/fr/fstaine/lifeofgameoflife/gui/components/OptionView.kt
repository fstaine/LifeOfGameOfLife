package fr.fstaine.lifeofgameoflife.gui.components

import javafx.beans.binding.Binding
import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import tornadofx.hbox
import tornadofx.label
import tornadofx.slider

class OptionView : Pane() {

    val delayProperty: DoubleProperty = SimpleDoubleProperty()

    private lateinit var delaySlider: Slider
    private lateinit var delayLabel: Label

    init {
        hbox {
            delaySlider = slider(1.0, 25.0, 6.0)
            delayLabel = label()

        }

        delayProperty.bind(Bindings.multiply(delaySlider.valueProperty(), delaySlider.valueProperty()))
        delayLabel.textProperty().bind(Bindings.format("%.0f", delayProperty))
    }
}
