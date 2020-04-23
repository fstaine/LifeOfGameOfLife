package fr.fstaine.lifeofgameoflife.genetic

import fr.fstaine.lifeofgameoflife.genetic.utils.ProbabilityPropagation
import fr.fstaine.lifeofgameoflife.genetic.utils.ProbabilityPropagationFunction

/**
 * The parameters from which to generate individuals.
 *
 * An individual is created from an initial point at the center of the square on which he can live.
 * Neighbors points are
 */
data class GenomeParameter(

    /**
     * The size of the board
     */
    val boardSize: Int,

    /**
     * The maximum size that an individual can take.
     * It's always a squared box
     */
    val individualMaxSize: Int,

    /**
     * The probability of a point to be alive.
     * Must be between 0 (always dead) and 1 (always alive)
     */
    val liveProbability: Double,

    /**
     * How is computed the probability to live for each cell
     */
    val liveProbabilityPropagation: ProbabilityPropagation
) {

}
