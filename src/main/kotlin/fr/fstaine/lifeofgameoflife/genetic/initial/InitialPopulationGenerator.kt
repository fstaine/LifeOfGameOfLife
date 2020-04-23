package fr.fstaine.lifeofgameoflife.genetic.initial

import fr.fstaine.lifeofgameoflife.genetic.GenomeParameter

interface InitialPopulationGenerator {
    fun generatePopulation(fromParams: GenomeParameter)
}
