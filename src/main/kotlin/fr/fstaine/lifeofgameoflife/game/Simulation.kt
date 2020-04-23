package fr.fstaine.lifeofgameoflife.game

import fr.fstaine.lifeofgameoflife.game.component.World
import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics
import java.util.*

interface Simulation {
    /**
     * The statistics of the current game
     */
    val stats: SimulationStatistics

    /**
     * Return the current world
     */
    val world: World

    val isBlocked: Boolean get() = this.stats.isBlocked || this.stats.isLooping

    /**
     * Update the world to it's next state
     */
    fun update()

    fun clean()

    fun invert(i: Int, j: Int)

    fun addObserver(o: Observer)
}
