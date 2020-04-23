package fr.fstaine.lifeofgameoflife.genetic

import fr.fstaine.lifeofgameoflife.game.NormalSimulation
import fr.fstaine.lifeofgameoflife.game.Simulation
import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics
import fr.fstaine.lifeofgameoflife.genetic.fitness.DefaultFitnessComputer
import fr.fstaine.lifeofgameoflife.genetic.fitness.Fitness
import fr.fstaine.lifeofgameoflife.genetic.fitness.FitnessComputer
import fr.fstaine.lifeofgameoflife.genetic.fitness.SimulationResults
import fr.fstaine.lifeofgameoflife.genetic.initial.DefaultInitialPopulationGenerator
import fr.fstaine.lifeofgameoflife.genetic.initial.InitialPopulationGenerator
import fr.fstaine.lifeofgameoflife.utils.Logger
import java.util.concurrent.*

interface GeneticRunner {

    val population: Array<Individual>

    fun initPopulation()

    /**
     * Run the current population in the environment
     * Run all the Simulations and compute their fitness
     */
    fun runPopulation()

    /**
     * Generate the next population from the current one
     */
    fun generateDescendants()

    fun getResult(individual: Individual) : SimulationResults?
}

/**
 * Run multiple games in parallel in dedicated Runnable
 */
class GeneticAlgorithmExecutor(val params: GenomeParameter) : GeneticRunner {

    // Private attributes

    private val popGenerator: InitialPopulationGenerator = DefaultInitialPopulationGenerator(params)

    private val fitnessComputer: FitnessComputer = DefaultFitnessComputer()

    private val executorService: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    // ThreadPoolExecutor(8, 64, 50L, TimeUnit.MILLISECONDS, LinkedBlockingQueue())

    // Public attributes

    override var population: Array<Individual> = emptyArray()
    var populationResults: ConcurrentMap<Individual, SimulationResults> = ConcurrentHashMap()

    override fun initPopulation() {
        val initPop = popGenerator.generatePopulation(20) // TODO: Pass it in params
        Logger.v("Initial population:")
        initPop.forEach {
            Logger.v(it)
        }
        population = initPop
    }

    override fun runPopulation() {
        populationResults.clear()
        population.forEach { individual ->
            executorService.submit(Runnable {
                val result = runIndividual(individual)
                populationResults[individual] = result
            })
        }
        executorService.awaitTermination(1, TimeUnit.SECONDS)
    }

    override fun generateDescendants() {
        Logger.e("Should generate next population (TODO)")
    }

    override fun getResult(individual: Individual) : SimulationResults? {
        return populationResults[individual]
    }

    // Private methods

    /**
     * Run an individual until it's blocked
     */
    private fun runIndividual(individual: Individual) : SimulationResults {
        val simulation = NormalSimulation(individual.params)
        while (!Thread.currentThread().isInterrupted && !simulation.isBlocked) {
            simulation.update()
        }
        if (Thread.currentThread().isInterrupted) {
            Logger.w("Task interrupted !")
        }
        return computeSimulationResults(simulation)
    }

    private fun computeSimulationResults(simulation: Simulation) : SimulationResults {
        val stats = simulation.stats
        val fitness = fitnessComputer.compute(stats)
        val blocked = simulation.isBlocked
        return SimulationResults(stats, fitness, blocked)
    }
}
