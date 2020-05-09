package fr.fstaine.lifeofgameoflife.gui

import fr.fstaine.lifeofgameoflife.game.NormalSimulation
import fr.fstaine.lifeofgameoflife.game.SimulationParameter
import fr.fstaine.lifeofgameoflife.game.component.World
import fr.fstaine.lifeofgameoflife.game.component.WorldCell
import fr.fstaine.lifeofgameoflife.game.component.WorldCellState
import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics
import fr.fstaine.lifeofgameoflife.gui.components.GameStatisticsView
import fr.fstaine.lifeofgameoflife.gui.components.OptionView
import fr.fstaine.lifeofgameoflife.persistence.ManualSimulationFileStorage
import fr.fstaine.lifeofgameoflife.persistence.SimulationStorage
import javafx.application.Platform
import javafx.beans.binding.StringBinding
import javafx.beans.property.BooleanProperty
import javafx.beans.property.BooleanPropertyBase
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*
import java.util.*

class RunningGameView(simulationParms: SimulationParameter? = null): View("Game Of Life"), Observer {

    private var size = 50
    private var windowSize = 1000
    private var delay = 80

    private var ratio = 0.0

    private lateinit var can: Canvas
    private lateinit var gc: GraphicsContext

    private val playPauseBtn = Button("Play (enter)")
    private val restartBtn = Button("Restart")
    private val saveBtn = Button("Save")
    private val loadBtn = Button("Load")
    private val statsView = GameStatisticsView()
    private val optionsView = OptionView()

    private val fileStorage: SimulationStorage = ManualSimulationFileStorage()
    private var simulationManager: NormalSimulation? = null
    var timer: Timer = Timer()

    private var started: BooleanProperty = SimpleBooleanProperty(false)

    override val root = hbox {
        vbox {
            playPauseBtn.attachTo(this)
            restartBtn.attachTo(this)
            saveBtn.attachTo(this)
            loadBtn.attachTo(this)
            statsView.attachTo(this)
            optionsView.attachTo(this)
        }
        can = canvas(windowSize.toDouble(), windowSize.toDouble())

        gc = can.graphicsContext2D
        gc.stroke = Color.GREY
        playPauseBtn.font = Font(17.0)
        restartBtn.font = Font(17.0)
        saveBtn.font = Font(17.0)
        loadBtn.font = Font(17.0)
        onKeyPressed = EventHandler { event ->
            when(event.code) {
                KeyCode.ENTER -> onPlayPause()
                KeyCode.C -> simulationManager?.clean()
                KeyCode.R -> simulationManager?.restart()
                KeyCode.RIGHT -> simulationManager?.update()
            }
        }
        can.onMouseClicked = EventHandler { event ->
            if (!started.get()) {
                val x = (event.x.toInt() / ratio).toInt()
                val y = (event.y.toInt() / ratio).toInt()
                simulationManager?.invert(x, y)
            }
        }
        primaryStage.onCloseRequest = EventHandler {
            stopGame()
        }

        restartBtn.action {
            simulationManager?.restart()
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
            onPlayPause()
        }

        linkViewProperties()
    }

    init {
        initGame(simulationParms ?: SimulationParameter(size))
    }

    /**
     * Link the different view' elements
     */
    private fun linkViewProperties() {
        saveBtn.disableProperty().bind(started)
        loadBtn.disableProperty().bind(started)
        restartBtn.disableProperty().bind(started)

        playPauseBtn.textProperty().bind(object: StringBinding() {
            init {
                super.bind(started)
            }

            override fun computeValue(): String {
                return if (started.get()) "Pause" else "Play (enter)"
            }
        })
    }

    /**
     * Action to perform to play or pause the game
     */
    private fun onPlayPause() {
        if (started.get()) {
            stopGame()
        } else {
            startGame()
        }
    }

    private fun initGame(params: SimulationParameter) {
        size = params.size
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
                simulationManager?.let { sim ->
                    sim.update()
                    if (sim.isBlocked) {
                        stopGame()
                    }
                }
            }
        }, 0, optionsView.delayProperty.longValue())
        started.value = true
    }

    private fun stopGame() {
        if (started.get()) {
            timer.cancel()
            started.value = false
        }
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
