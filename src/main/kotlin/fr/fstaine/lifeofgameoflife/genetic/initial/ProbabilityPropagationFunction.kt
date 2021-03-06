package fr.fstaine.lifeofgameoflife.genetic.initial

import fr.fstaine.lifeofgameoflife.game.component.Position
import kotlin.math.abs

enum class ProbabilityPropagation(val func: ProbabilityPropagationFunction) {
    CONSTANT(ConstantProbabilityPropagationFunction()),
    VERTICALLY_CONSTANT(VerticallyCenteredConstantProbabilityPropagationFunction())
}

interface ProbabilityPropagationFunction {
    fun probability(size: Int, initialValue: Double, at: Position) : Double
}

/**
 * The same probability on each position
 */
class ConstantProbabilityPropagationFunction :
    ProbabilityPropagationFunction {
    override fun probability(size: Int, initialValue: Double, at: Position): Double {
        return initialValue
    }
}

/**
 * Probability are the same on each column, decreasing when approaching the sides
 */
class VerticallyCenteredConstantProbabilityPropagationFunction:
    ProbabilityPropagationFunction {
    override fun probability(size: Int, initialValue: Double, at: Position): Double {
        return 2 * initialValue / abs(size/2 - at.x)
    }
}
