package fr.fstaine.lifeofgameoflife.gui

import fr.fstaine.lifeofgameoflife.genetic.GeneticAlgorithmExecutor
import fr.fstaine.lifeofgameoflife.genetic.GenomeParameter
import fr.fstaine.lifeofgameoflife.genetic.Individual
import fr.fstaine.lifeofgameoflife.genetic.utils.ProbabilityPropagation
import fr.fstaine.lifeofgameoflife.utils.Logger
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.*
import kotlin.math.roundToInt

class GeneticAlgorithmManagerView : View("Genetic Algorithm") {

    private val genomeParams: GenomeParameter = GenomeParameter(
        boardSize = 100,
        individualMaxSize = 12,
        liveProbability = 0.3,
        liveProbabilityPropagation = ProbabilityPropagation.CONSTANT
    )

    private val gaExecutor: GeneticAlgorithmExecutor = GeneticAlgorithmExecutor(genomeParams)

    private lateinit var populationView: GridPane
    private lateinit var centerPane: Pane

    override val root = borderpane {
        prefWidth = 800.0
        prefHeight = 600.0
        top = hbox {
            button("Init population") {
                action {
                    gaExecutor.initPopulation()
                    updatePopulationView()
                }
            }
            button("Run generation") {
                action {
                    gaExecutor.runPopulation()
                    updatePopulationScores()
                }
            }
        }
        center = scrollpane {
            populationView = gridpane {

            }
        }
    }

    private fun updatePopulationView() {
        populationView.clear()
        populationView.add(Label("Individual"), 0, 0)
        populationView.add(Label("Fitness score"), 1, 0)
        gaExecutor.population.forEachIndexed { index, individual ->
            val bt = Button("Inidvidual $index")
            bt.action {
                showIndividual(individual)
            }
            populationView.add(bt, 0, index + 1)
        }
    }

    private fun showIndividual(individual: Individual) {
        Logger.d("$individual")
        val v = RunningGameView(individual.params)
        val stage = Stage()
        stage.scene = Scene(v.root, 1200.0, 1000.0)
        stage.show()
    }

    private fun updatePopulationScores() {
        var fitnessSum: Double = 0.0
        gaExecutor.population.forEachIndexed { index, individual ->
            val results = gaExecutor.getResult(individual)


            results?.let { res ->
                val label = Label("${res.fitness.roundToInt()}")
                if (!res.isCompleted) {
                    label.style = "-fx-text-fill: red;"
                }
                populationView.add(label, 1, index + 1)
                fitnessSum += res.fitness
            }
        }
        var fitnessMean = fitnessSum / gaExecutor.population.size
        populationView.add( Label("Mean: "), 0, gaExecutor.population.size + 1)
        populationView.add( Label("${fitnessMean.roundToInt()}"), 1, gaExecutor.population.size + 1)
    }
}
