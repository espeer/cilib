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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.type.types.Type;

/**
 * This class represents a solution to an {@link net.sourceforge.cilib.problem.OptimisationProblem}.
 * It is responsible for keeping track of the optimisation problem and position of the solution within the search
 * space.
 *
 * @author  Edwin Peer
 */
public class OptimisationSolution implements Solution, Comparable<OptimisationSolution> {
    private static final long serialVersionUID = 2119444179382452329L;

    private final Type position;
    private final Fitness fitness;

    /**
     * Constructs a new instance of {@code OptimisationSolution}.
     *
     * @param position The position of the solution within the search space of the problem.
     * @param fitness The fitness of the optimisation solution.
     */
    public OptimisationSolution(Type position, Fitness fitness) {
        this.position = position.getClone();
        this.fitness = fitness.getClone();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        OptimisationSolution otherSolution = (OptimisationSolution)other;
        return this.position.equals(otherSolution.position) &&
            this.fitness.equals(otherSolution.fitness);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.position == null ? 0 : this.position.hashCode());
        hash = 31 * hash + (this.position == null ? 0 : this.fitness.hashCode());
        return hash;
    }

    /**
     * Returns the position of this solution within the search space of the problem.
     *
     * @return The position of this solution in search space.
     */
    public Type getPosition() {
        return this.position;
    }

    /**
     * Returns the fitness of this solution according to {@link net.sourceforge.cilib.problem.OptimisationProblem#getFitness(Type, boolean)}.
     * Calling this function does not contribute to the number of fitness evaulations maintained by
     * {@link net.sourceforge.cilib.problem.OptimisationProblem}.
     *
     * @return The fitness of this solution.
     */
    public Fitness getFitness() {
        return this.fitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(OptimisationSolution other) {
        return this.fitness.compareTo(other.fitness);
    }
}
