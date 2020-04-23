package fr.fstaine.lifeofgameoflife.game.stats

import fr.fstaine.lifeofgameoflife.game.component.Position
import fr.fstaine.lifeofgameoflife.game.component.World
import java.lang.Integer.max

data class GameStatistics (
    /**
     *  The size of the world
     */
    val size: Int,

    /**
     * The initial set of living individuals
     */
    val initialCells: Set<Position>
) {
    /**
     * The current step index (number of updates to reach this point)
     */
    var step: Int = 0

    /**
     * The current number of individuals
     */
    var livingCellsCount: Int = initialCells.size

    /**
     * The maximum number of individuals in the world
     */
    var maxLivingCells: Int = livingCellsCount

    /**
     * All the position which has been alive at least once
     */
    var touchedCells: MutableSet<Position> = initialCells.toMutableSet()

    /**
     * Indicate if the world has changed on the last update (if not, it won't ever move...)
     */
    var isBlocked: Boolean = false

    /**
     * Indicate if any cell remains in the world at the end
     */
    var isEmpty: Boolean = false

    var bordedReached: Boolean = initialCells.filter { (x, y) ->
        x == 0 || x == size - 1 || y == 0 || y == size - 1
    }.isNotEmpty()

    /**
     * The total number of cells which was born
     */
    var bornCellCount: Int = 0

    /**
     * The total number of cells which died
     */
    var deadCellsCount: Int = 0

    /**
     * The full history of the game
     */
    var fullHistory: MutableList<Set<Position>> = mutableListOf(initialCells)

    /**
     * Indicate if a loop is reached
     */
    var isLooping: Boolean = false

    /* Public methods */

    fun update(from: World, to: World) {
        val bornedCells = to.livingCellsPositionIterable.toSet() - from.livingCellsPositionIterable.toSet()
        val deadCells = from.livingCellsPositionIterable.toSet() - to.livingCellsPositionIterable.toSet()
        val borderCells = to.livingCellsPositionIterable.filter { (x, y) ->
            x == 0 || x == size - 1 || y == 0 || y == size - 1
        }
        val currentLivingCells = to.livingCellsPositionIterable.toSet()

        this.step += 1

        this.isEmpty = to.livingCellsIterable.count() > 0
        this.bordedReached = this.bordedReached || borderCells.isNotEmpty()
        this.isBlocked = from.livingCellsPositionIterable == to.livingCellsPositionIterable
        this.isLooping = this.isLooping || this.fullHistory.contains(currentLivingCells)

        this.livingCellsCount = to.livingCellsIterable.count()
        this.maxLivingCells = max(this.maxLivingCells, livingCellsCount)
        this.touchedCells.addAll(bornedCells)
        this.bornCellCount += bornedCells.count()
        this.deadCellsCount += deadCells.count()
        this.fullHistory.add(currentLivingCells)
    }
}
