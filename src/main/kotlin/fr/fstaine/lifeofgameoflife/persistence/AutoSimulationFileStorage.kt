package fr.fstaine.lifeofgameoflife.persistence

import fr.fstaine.lifeofgameoflife.game.Simulation
import fr.fstaine.lifeofgameoflife.game.SimulationParameter
import fr.fstaine.lifeofgameoflife.persistence.serializer.JsonSimulationSerializer
import java.io.File

/**
 * Store a grid to be able to play it again later
 * Store the grid automatically in the given root directory
 */
class AutoSimulationFileStorage(val rootPath: String) : SimulationStorage {

    private val serializer = JsonSimulationSerializer()

    private val extension: String = ".gol.json"

    private val root: File = File(rootPath)

    /**
     * The collection of files that can be retrieved
     */
    private val storedFiles: Collection<File> get() {
        val files: Array<File> = root.listFiles() ?: emptyArray()
        return files
            .filter { it.endsWith(extension) }
    }

    // Public methods

    val savedSessions: Collection<String> get() {
        return storedFiles.map { it.name }
    }

    override fun store(simulation: Simulation) {
        val newFileName = ""
        val newFile = File(root, newFileName)
        val json = serializer.serialize(simulation)
        newFile.writeText(json)
    }

    override fun load(name: String): SimulationParameter? {
        val file = storedFiles.first { it.name == "${name}${extension}" }
        val content = file.readText()
        return serializer.deserialize(content)
    }
}
