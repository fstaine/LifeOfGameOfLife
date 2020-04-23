package fr.fstaine.lifeofgameoflife.persistence

import fr.fstaine.lifeofgameoflife.game.Simulation
import fr.fstaine.lifeofgameoflife.game.SimulationParameter
import fr.fstaine.lifeofgameoflife.persistence.serializer.JsonSimulationSerializer
import javafx.stage.FileChooser
import tornadofx.FileChooserMode
import tornadofx.chooseFile
import java.io.File

class ManualSimulationFileStorage : SimulationStorage {

    private val fileChooser = FileChooser()

    private val serializer = JsonSimulationSerializer()

    private val extension: String = "golSim"
    private val extensionFilter = arrayOf(FileChooser.ExtensionFilter("Game Of Life simulation", "*.$extension"))

    // Public methods

    override fun store(simulation: Simulation) {

        val fileSelection = chooseFile("Store a simulation", filters = extensionFilter, mode = FileChooserMode.Save)
        fileSelection.firstOrNull()?.let { file ->
            val fileWithExtension = if (file.extension == extension) file else File("${file.absolutePath}.${extension}")
            val json = serializer.serialize(simulation)
            fileWithExtension.writeText(json)
        }
    }

    override fun load(name: String): SimulationParameter? {
        val fileSelection = chooseFile("Load a simulation", filters = extensionFilter)
        return fileSelection.firstOrNull()?.let { file ->
            val content = file.readText()
            return serializer.deserialize(content)
        }
    }
}
