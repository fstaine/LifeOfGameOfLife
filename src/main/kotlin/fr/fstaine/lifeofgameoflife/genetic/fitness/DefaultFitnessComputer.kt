package fr.fstaine.lifeofgameoflife.genetic.fitness

import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics

class DefaultFitnessComputer : FitnessComputer {
    override fun compute(s: SimulationStatistics): Fitness {
        val fitness = 1.0 * s.step * s.maxLivingCells / s.initialCells.count()
        return if (fitness.isNaN()) {
            0.0
        } else {
            fitness
        }
    }
}
