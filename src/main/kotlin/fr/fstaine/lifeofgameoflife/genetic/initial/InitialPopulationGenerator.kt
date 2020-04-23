package fr.fstaine.lifeofgameoflife.genetic.initial

import fr.fstaine.lifeofgameoflife.game.SimulationParameter
import fr.fstaine.lifeofgameoflife.game.component.Position
import fr.fstaine.lifeofgameoflife.game.component.World
import fr.fstaine.lifeofgameoflife.genetic.GenomeParameter
import fr.fstaine.lifeofgameoflife.genetic.Individual
import tornadofx.parallelTransition
import kotlin.random.Random

interface InitialPopulationGenerator {
    fun generatePopulation(count: Int) : Array<Individual>
}

class DefaultInitialPopulationGenerator(val params: GenomeParameter) : InitialPopulationGenerator {

    private var randGen = Random.Default

    private fun generateIndividual() : Individual {
        val size = params.boardSize
        val world = generateWorld(size)
        val individualParams = SimulationParameter(size, world)
        return Individual(individualParams)
    }

    private fun generateWorld(size: Int, center: Boolean = true) : Set<Position> {
        val world = World.empty(params.individualMaxSize)
        val repartition = params.liveProbabilityPropagation.func
        val probabilityMap: Iterable<Pair<Position, Double>> = world.positionIterable
            .map { pos -> pos to repartition.probability(size, params.liveProbability, pos) }
        val alive = probabilityMap.filter { isAlive(it) }
        val alivePositions = alive.map { it.first }
        val repositionedPosition = if (center) alivePositions.map { it.translate((size - params.individualMaxSize)/2) } else alivePositions
        return repositionedPosition.toSet()
    }

    /**
     * Chose if the given cell is alive or not
     */
    private fun isAlive(probability: Double) : Boolean {
        return randGen.nextDouble() < probability
    }

    private fun isAlive(probCell: Pair<Position, Double>) : Boolean {
        return isAlive(probCell.second)
    }

    // InitialPopulationGenerator methods

    override fun generatePopulation(count: Int): Array<Individual> {
        return (0.until(count))
            .map { _ -> generateIndividual() }
            .toTypedArray()
    }
}
