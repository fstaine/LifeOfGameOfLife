package fr.fstaine.lifeofgameoflife.persistence.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.fstaine.lifeofgameoflife.game.Simulation
import fr.fstaine.lifeofgameoflife.game.NormalSimulation
import fr.fstaine.lifeofgameoflife.game.SimulationParameter
import fr.fstaine.lifeofgameoflife.game.component.Position

/**
 * The JSON representation of the game
 */
private data class JsonSavedGame(val size: Int, val initial: Set<Position>, val score: Double?) {

    val simulation: SimulationParameter get() = SimulationParameter(size, initial)

    constructor(simulation: Simulation) : this(simulation.stats.size, simulation.stats.initialCells, 0.0) {

    }
}

/**
 * parse a simulation from / to as JSON content
 */
class JsonSimulationSerializer {

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun serialize(simulation: Simulation) : String {
        val jsonSavedGame = JsonSavedGame(simulation)
        return gson.toJson(jsonSavedGame)
    }

    fun deserialize(string: String) : SimulationParameter? {
        return gson.fromJson(string, JsonSavedGame::class.java)?.simulation
    }
}
