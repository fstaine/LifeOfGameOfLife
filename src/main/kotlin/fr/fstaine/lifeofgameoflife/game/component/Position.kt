package fr.fstaine.lifeofgameoflife.game.component

data class Position(val x: Int, val y: Int) {

    override fun toString(): String {
        return "($x, $y)"
    }

    fun toPair(): Pair<Int, Int> {
        return Pair(x, y)
    }

    fun translate(by: Int) : Position {
        return translate(by, by)
    }

    fun translate(tX: Int, tY: Int) : Position {
        return Position(this.x + tX, this.y + tY)
    }
}
