package fr.fstaine.lifeofgameoflife.gui

import fr.fstaine.lifeofgameoflife.game.Simulation
import fr.fstaine.lifeofgameoflife.game.NormalSimulation
import fr.fstaine.lifeofgameoflife.game.SimulationParameter
import fr.fstaine.lifeofgameoflife.game.component.World
import fr.fstaine.lifeofgameoflife.game.component.WorldCell
import fr.fstaine.lifeofgameoflife.game.component.WorldCellState
import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics
import fr.fstaine.lifeofgameoflife.persistence.ManualSimulationFileStorage
import fr.fstaine.lifeofgameoflife.persistence.SimulationStorage
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*
import java.util.*

class RunningGameView: View("Game Of Life"), Observer {

    private var size = 50
    private var windowSize = 1000
    private var delay = 200

    private var ratio = 0.0

    private lateinit var can: Canvas
    private lateinit var gc: GraphicsContext

    private val playPauseBtn = Button("Play (enter)")
    private val saveBtn = Button("Save")
    private val loadBtn = Button("Load")
    private val statsView = GameStatisticsView()
    private val optionsView = OptionView()

    private val fileStorage: SimulationStorage = ManualSimulationFileStorage()
    private var simulationManager: Simulation? = null
    var timer: Timer = Timer()
    private var started = false

    override val root = hbox {
        vbox {
            playPauseBtn.attachTo(this)
            saveBtn.attachTo(this)
            loadBtn.attachTo(this)
            statsView.attachTo(this)
            optionsView.attachTo(this)
        }
        can = canvas(windowSize.toDouble(), windowSize.toDouble())

        gc = can.graphicsContext2D
        gc.stroke = Color.GREY
        playPauseBtn.font = Font(17.0)
        saveBtn.font = Font(17.0)
        loadBtn.font = Font(17.0)
        onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.ENTER) {
                if (started) {
                    stopGame()
                } else {
                    startGame()
                }
            } else if (event.code == KeyCode.C) {
                simulationManager?.clean()
            }
        }
        can.onMouseClicked = EventHandler { event ->
            val x = (event.x.toInt() / ratio).toInt()
            val y = (event.y.toInt() / ratio).toInt()
            simulationManager?.invert(x, y)
        }
        primaryStage.onCloseRequest = EventHandler {
            if (started) {
                stopGame()
            }
        }

        saveBtn.action {
            simulationManager?.let {
                fileStorage.store(it)
            }
        }

        loadBtn.action {
            fileStorage.load("")?.let {
                initGame(it)
            }
        }

        playPauseBtn.onAction = EventHandler {
            if (started) {
                stopGame()
            } else {
                startGame()
            }
        }

        initGame()
    }

    private fun initGame(params: SimulationParameter = SimulationParameter(size)) {
        simulationManager = NormalSimulation(params)
        simulationManager?.addObserver(this)
        ratio = 1.0 * windowSize / size

        simulationManager?.let { realGame ->
            Platform.runLater {
                draw(realGame.world)
                updateStats(realGame.stats)
            }
        }
    }

    private fun startGame() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                simulationManager?.update()
                if (simulationManager?.isBlocked != false) {
                    stopGame()
                }
            }
        }, 0, delay.toLong())
        Platform.runLater {
            playPauseBtn.text = "Pause"
            loadBtn.isDisable = true
            loadBtn.isDisable = true
        }
        started = true
    }

    private fun stopGame() {
        timer.cancel()
        Platform.runLater {
            playPauseBtn.text = "Play (enter)"
            loadBtn.isDisable = false
            loadBtn.isDisable = false
        }
        started = false
    }

    /**
     * Draw the whole world
     */
    private fun draw(world: World) {
        for (elem in world.allIterable) {
            val (pos, cell) = elem
            val (i, j) = pos
            drawCell(i, j, cell)
        }
    }

    /**
     * Draw one cell of the world
     */
    private fun drawCell(i: Int, j: Int, cell: WorldCell) {
        if (cell.state == WorldCellState.ALIVE) {
            gc.fill = Color.BLACK
        } else {
            gc.fill = Color.WHITE
        }
        gc.fillRect(ratio * i, ratio * j, ratio, ratio)
    }

    /**
     * Update the statistics board of the game
     */
    private fun updateStats(stats: SimulationStatistics) {
        this.statsView.update(stats)
    }

    /**
     * Update the whole view according to the current game state
     */
    private fun updateGameView() {
        simulationManager?.let { realGame ->
            Platform.runLater {
                draw(realGame.world)
                updateStats(realGame.stats)
            }
        }
    }

    /**
     * Update the view when the game notifies a changes
     */
    override fun update(o: Observable, arg: Any?) {
        updateGameView()
    }
}
