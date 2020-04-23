package fr.fstaine.lifeofgameoflife.genetic.fitness

import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics

data class SimulationResults(
    /**
     * The statistics at the end of the simulation
     */
    val stats: SimulationStatistics,

    /**
     * The fitness of the individual at the last step
     */
    val fitness: Fitness,

    /**
     * Indicate if the simulation had time to complete
     */
    val isCompleted: Boolean
)
