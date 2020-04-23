package fr.fstaine.lifeofgameoflife.genetic

import fr.fstaine.lifeofgameoflife.game.Game
import fr.fstaine.lifeofgameoflife.game.NormalGame
import fr.fstaine.lifeofgameoflife.game.component.World
import fr.fstaine.lifeofgameoflife.game.stats.GameStatistics
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Run multiple games in parallel in dedicated Runnable
 */
class GeneticAlgorithmExecutor(size: Int) : Game, Observable() {
    private val executorService: ExecutorService = ThreadPoolExecutor(8, 64, 50L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())

    private val game: NormalGame =
        NormalGame(size)

    override val world: World get() = game.world

    override val stats: GameStatistics get() = game.stats

    override fun update() {
        game.update()
    }

    override fun clean() {
        game.clean()
    }

    override fun invert(i: Int, j: Int) {
        game.invert(i, j)
    }

    override fun addObserver(o: Observer) {

    }
}
