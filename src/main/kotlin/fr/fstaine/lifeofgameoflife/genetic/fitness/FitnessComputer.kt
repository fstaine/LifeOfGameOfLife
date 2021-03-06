package fr.fstaine.lifeofgameoflife.genetic.fitness

import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics

typealias Fitness = Double

/**
 * Compute the fitness of an individual based on it's statistics after executing
 */
interface FitnessComputer {
    fun compute(s: SimulationStatistics): Fitness
}
