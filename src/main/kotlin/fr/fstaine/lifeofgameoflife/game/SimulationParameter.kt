package fr.fstaine.lifeofgameoflife.game

import fr.fstaine.lifeofgameoflife.game.component.Position

data class SimulationParameter(val size: Int, val initial: Set<Position> = emptySet()) {

}
