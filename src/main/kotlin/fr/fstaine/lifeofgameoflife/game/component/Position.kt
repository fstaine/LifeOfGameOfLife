package fr.fstaine.lifeofgameoflife.game.component

data class Position(val x: Int, val y: Int) {

    override fun toString(): String {
        return "($x, $y)"
    }

    fun toPair(): Pair<Int, Int> {
        return Pair(x, y)
    }
}
