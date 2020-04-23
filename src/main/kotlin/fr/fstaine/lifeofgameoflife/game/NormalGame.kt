package fr.fstaine.lifeofgameoflife.game

import fr.fstaine.lifeofgameoflife.game.component.World
import fr.fstaine.lifeofgameoflife.game.component.WorldCell
import fr.fstaine.lifeofgameoflife.game.component.WorldCellState
import fr.fstaine.lifeofgameoflife.game.stats.GameStatistics
import java.util.*
import java.util.stream.Stream

class NormalGame(val size: Int): Game, Observable() {

    /**
     * Indicate which board is the current one
     */
    private var first: Boolean = true

    /**
     * Two mutable boards which alternatively represent the current or the previous state of the world
     */
    private var board1: World = World.empty(size)
    private var board2: World = World.empty(size)

    private var _stats: GameStatistics = GameStatistics(size, emptySet())

    /**
     * The board which represent the current representation of the world
     */
    private val currentBoard: World get() {
        return if (first) {
            board1
        } else {
            board2
        }
    }

    /**
     * The board which represent the previous representation of the world
     */
    private val otherBoard: World get() {
        return if (first) {
            board2
        } else {
            board1
        }
    }

    /*********************/
    /* Public attributes */
    /*********************/

    /**
     * The statistics of the current game
     */
    override val stats: GameStatistics get() = _stats

    /**
     * Return the current world
     */
    override val world: World
        get() {
        return this.currentBoard
    }

    /******************/
    /* Public methods */
    /******************/

    /**
     * Update the world to it's next state
     */
    override fun update() {
        val next = this.otherBoard
        val current = this.currentBoard
        for (i in 0 until size) {
            for (j in 0 until size) {
                next[i, j] = update(current, i, j)
            }
        }
        this.first = !this.first
        updateStats()
        setChanged()
        notifyObservers()
    }

    /**
     * Clean all the cells (Make them all dead)
     */
    override fun clean() {
        for (pos in world.positionIterable) {
            val (i, j) = pos
            this.manuallySetCell(i, j,
                WorldCell(WorldCellState.DEAD)
            )
        }
        initStats()
        setChanged()
        notifyObservers()
    }

    /**
     * Invert the status of a cell
     */
    override fun invert(i: Int, j: Int) {
        val cell = currentBoard[i, j]
        if (cell == null) {
            return
        } else {
            if (cell.state == WorldCellState.ALIVE) {
                this.manuallySetCell(i, j, cell.deadCopy())
            } else {
                this.manuallySetCell(i, j, cell.livingCopy())
            }
        }
        initStats()
        setChanged()
        notifyObservers()
    }

    /*******************/
    /* Private methods */
    /*******************/

    /**
     * Update the cell at the given position (i, j) in the given world
     */
    private fun update(world: World, i: Int, j: Int): WorldCell {
        val neighbors: Stream<WorldCell?> = Stream.of(
            world[i-1, j-1],
            world[i-1, j  ],
            world[i-1, j+1],
            world[i  , j-1],
            world[i  , j+1],
            world[i+1, j-1],
            world[i+1, j  ],
            world[i+1, j+1]
        )
        val livingNeighborsCount = neighbors
            .filter { c -> c?.state == WorldCellState.ALIVE }
            .count()
        return if (livingNeighborsCount == 3L || world[i, j]!!.state == WorldCellState.ALIVE && livingNeighborsCount == 2L) {
            world[i, j]!!.livingCopy()
        } else {
            world[i, j]!!.deadCopy()
        }
    }

    /**
     * Set the given state to the cell at position (i, j)
     */
    private fun manuallySetCell(i: Int, j: Int, cell: WorldCell) {
        currentBoard[i, j] = cell
    }

    /**
     * Init the statistics with the current state of the game
     */
    private fun initStats() {
        _stats = GameStatistics(size, world.livingCellsPositionIterable.toSet())
    }

    /**
     * Update the statistics of the game after it has been updated.
     * Note: The updateStats must be called after the board has been inverted
     */
    private fun updateStats() {
        _stats.update(otherBoard, currentBoard)
    }
}
