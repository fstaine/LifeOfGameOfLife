package fr.fstaine.lifeofgameoflife.game.component

data class WorldCell(var state: WorldCellState) {
    /**
     * Create a copy of the cell which is alive
     */
    fun livingCopy(): WorldCell {
        return WorldCell(WorldCellState.ALIVE)
    }

    /**
     * Create a copy of the cell which is dead
     */
    fun deadCopy(): WorldCell {
        return WorldCell(WorldCellState.DEAD)
    }
}
