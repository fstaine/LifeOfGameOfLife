package fr.fstaine.lifeofgameoflife.gui

import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics
import fr.fstaine.lifeofgameoflife.genetic.fitness.DefaultFitnessComputer
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import kotlin.math.roundToInt

class GameStatisticsView : Pane() {

    /**
     * Index of the last element on the gridPane
     */
    private var lastIdx: Int = 0

    private val gridPane = GridPane()

    private val sizeLabel: Label = Label()
    private val stepLabel: Label = Label()
    private val bornCellCountLabel: Label = Label()
    private val deadCellsCountLabel: Label = Label()
    private val livingCellsCountLabel: Label = Label()
    private val maxLivingCellsLabel: Label = Label()
    private val touchedCellsLabel: Label = Label()

    private val reachedBorderCheckbox: CheckBox = CheckBox()
    private val isBlockedCheckbox: CheckBox = CheckBox()
    private val isLoopingCheckbox: CheckBox = CheckBox()

    private val fitnellValueLabel: Label = Label()

    init {
        this.children.add(gridPane)
        gridPane.prefWidth = 230.0

        this.addStat("Size", sizeLabel)
        this.addStat("Step", stepLabel)
        this.addStat("Borned", bornCellCountLabel)
        this.addStat("Dead", deadCellsCountLabel)
        this.addStat("currently living", livingCellsCountLabel)
        this.addStat("Maximum population", maxLivingCellsLabel)
        this.addStat("Cells touched (changed)", touchedCellsLabel)
        this.addStat("Reached the border", reachedBorderCheckbox)
        this.addStat("Blocked", isBlockedCheckbox)
        this.addStat("Reached static loop", isLoopingCheckbox)
        this.addStat("Fitness", fitnellValueLabel)

        reachedBorderCheckbox.isDisable = true
        isBlockedCheckbox.isDisable = true
        isLoopingCheckbox.isDisable = true
    }

    private fun addStat(text: String, node: Node) {
        this.gridPane.add(Label("$text: "), 0, lastIdx)
        this.gridPane.add(node, 1, lastIdx)
        this.lastIdx += 1
    }

    /* Public methods */

    fun update(stats: SimulationStatistics) {
        this.sizeLabel.text = "${stats.size}"
        this.stepLabel.text = "${stats.step}"

        this.bornCellCountLabel.text = "${stats.bornCellCount}"
        this.deadCellsCountLabel.text = "${stats.deadCellsCount}"
        this.livingCellsCountLabel.text = "${stats.livingCellsCount}"
        this.maxLivingCellsLabel.text = "${stats.maxLivingCells}"
        this.touchedCellsLabel.text = "${stats.touchedCells.count()}"

        this.reachedBorderCheckbox.isSelected = stats.bordedReached
        this.isBlockedCheckbox.isSelected = stats.isBlocked
        this.isLoopingCheckbox.isSelected = stats.isLooping

        val fitness = DefaultFitnessComputer().compute(stats)
        this.fitnellValueLabel.text = "${fitness.roundToInt()}"
    }
}
