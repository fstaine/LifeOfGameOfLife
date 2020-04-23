package fr.fstaine.lifeofgameoflife.persistence

import fr.fstaine.lifeofgameoflife.game.Simulation
import fr.fstaine.lifeofgameoflife.game.SimulationParameter

interface SimulationStorage {
    /**
     * Store the result of a simulation
     */
    fun store(simulation: Simulation)

    fun load(name: String) : SimulationParameter?
}
