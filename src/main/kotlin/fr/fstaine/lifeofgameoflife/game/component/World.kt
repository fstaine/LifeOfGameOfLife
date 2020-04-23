package fr.fstaine.lifeofgameoflife.game.component

/**
 * A squared shaped world of size * size elements
 */
data class World(val size: Int, var cells: Array<Array<WorldCell>>) {
    companion object {
        /**
         * Create an empty world with only dead cells
         */
        fun empty(size: Int): World {
            return World(
                size,
                emptyCells(size)
            )
        }

        private fun emptyCells(size: Int): Array<Array<WorldCell>> {
            return Array(size) {
                Array(size) {
                    WorldCell(WorldCellState.DEAD)
                }
            }
        }
    }

    /**
     * Safe get:
     * Get a cell if it is in the world, null otherwise
     */
    operator fun get(i: Int, j: Int): WorldCell? {
        return if (i < 0 || j < 0 || i >= size || j >= size) {
            null
        } else {
            cells[i][j]
        }
    }

    /**
     * Update a cell in the world at index (i, j)
     */
    operator fun set(i: Int, j: Int, value: WorldCell) {
        return if (i < 0 || j < 0 || i >= size || j >= size) {
            throw ArrayIndexOutOfBoundsException()
        } else {
            cells[i][j] = value
        }
    }

    /* Iterable methods */

    /**
     * Iterate over the world by retreiving the position and the cell at this position
     */
    val allIterable: Iterable<Pair<Position, WorldCell>> get() = WorldBaseIterable(
        this
    )

    /**
     * Iterate over the world' positions
     */
    val positionIterable: Iterable<Position> get() = allIterable.map { (pos, _) -> pos }

    /**
     * Iterate over the world' cells
     */
    val cellIterable: Iterable<WorldCell> get() = allIterable.map { (_, cell) -> cell }

    /**
     * Iterate over the world' living cells
     */
    val livingCellsIterable: Iterable<WorldCell> get() = cellIterable.filter { cell -> cell.state == WorldCellState.ALIVE }

    /**
     * Iterate over the world' living cells's position
     */
    val livingCellsPositionIterable: Iterable<Position> get() = allIterable
        .filter { (_, cell) -> cell.state == WorldCellState.ALIVE }
        .map { (pos, _) -> pos }
}

private class WorldBaseIterator(val world: World): Iterator<Pair<Position, WorldCell>> {

    /**
     * The size of a
     */
    val size: Int get() = world.size

    /**
     * The index at which we reached the end of the world
     */
    val maxIdx: Int = size * size

    /**
     * The current index of the iterator
     */
    var idx: Int = 0

    override fun hasNext(): Boolean {
        return idx < maxIdx
    }

    override fun next(): Pair<Position, WorldCell> {
        val i = idx / world.size
        val j = idx % world.size
        val cell = world[i, j]
        idx += 1
        return Pair(Position(i, j), cell!!)
    }
}

private class WorldBaseIterable(val world: World): Iterable<Pair<Position, WorldCell>> {
    override fun iterator(): Iterator<Pair<Position, WorldCell>> {
        return WorldBaseIterator(world)
    }
}
