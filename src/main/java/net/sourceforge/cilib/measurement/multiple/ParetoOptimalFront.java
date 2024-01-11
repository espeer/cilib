/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.measurement.multiple;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * <p>
 * Measures the set of non-dominated objective vectors withing an archive.
 * Requires the set of non-dominated decision vectors to be evaluated.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class ParetoOptimalFront implements Measurement<TypeList> {

    private static final long serialVersionUID = 6695894359780745776L;

    @Override
    public ParetoOptimalFront getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "T";
    }

    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList allFitnessValues = new TypeList();
        Collection<OptimisationSolution> solutions = Archive.get();
        for (OptimisationSolution solution : solutions) {
            MOFitness fitnesses = (MOFitness) solution.getFitness();
            TypeList fitnessValues = new TypeList();
            for (int i = 0; i < fitnesses.getDimension(); ++i) {
                Fitness fitness = fitnesses.getFitness(i);
                fitnessValues.add(fitness);
            }
            allFitnessValues.add(fitnessValues);
        }
        return allFitnessValues;
    }
}
