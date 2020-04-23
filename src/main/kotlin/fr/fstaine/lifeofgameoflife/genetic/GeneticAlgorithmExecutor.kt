package fr.fstaine.lifeofgameoflife.genetic

import fr.fstaine.lifeofgameoflife.game.Simulation
import fr.fstaine.lifeofgameoflife.game.NormalSimulation
import fr.fstaine.lifeofgameoflife.game.SimulationParameter
import fr.fstaine.lifeofgameoflife.game.component.World
import fr.fstaine.lifeofgameoflife.game.stats.SimulationStatistics
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Run multiple games in parallel in dedicated Runnable
 */
class GeneticAlgorithmExecutor(params: SimulationParameter) : Simulation, Observable() {
    private val executorService: ExecutorService = ThreadPoolExecutor(8, 64, 50L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())

    private val simulation: NormalSimulation = NormalSimulation(params)

    override val world: World get() = simulation.world

    override val stats: SimulationStatistics get() = simulation.stats

    override fun update() {
        simulation.update()
    }

    override fun clean() {
        simulation.clean()
    }

    override fun invert(i: Int, j: Int) {
        simulation.invert(i, j)
    }

    override fun addObserver(o: Observer) {

    }
}
