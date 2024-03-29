/*
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
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.dynamic.detectionstrategies.EnvironmentChangeDetectionStrategy;
import net.sourceforge.cilib.pso.dynamic.detectionstrategies.RandomSentryDetectionStrategy;
import net.sourceforge.cilib.pso.dynamic.responsestrategies.EnvironmentChangeResponseStrategy;
import net.sourceforge.cilib.pso.dynamic.responsestrategies.PartialReinitialisationResponseStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;

/**
 * Dynamic iteration strategy for PSO in dynamic environments.
 * In each iteration, it checks for an environmental change, and reinitialises
 * a percentage of the swarm once such a change is detected in order to preserve
 * diversity.
 *
 * The algorithm is adopted from<br/>
 * @book{focsi,
 *         author=         {Andries P. Engelbrecht},
 *         title=          {{Fundamentals of Computational Swarm Intelligence}},
 *        publisher=      {John Wiley \& Sons, Ltd},
 *         year=           {2005}
 * }
 *
 * @author Anna Rakitianskaia
 */
public class DynamicIterationStrategy implements IterationStrategy<PSO> {
    private static final long serialVersionUID = -4441422301948289718L;

    private IterationStrategy<PSO> iterationStrategy;
    //TODO: private DetectionStrategy<PSO> detection
    //TODO: private ReactionStrategy<PSO> reaction
    private EnvironmentChangeDetectionStrategy<PSO> detectionStrategy;
    private EnvironmentChangeResponseStrategy<PSO> responseStrategy;

    /**
     * Create a new instance of {@linkplain DynamicIterationStrategy}.
     * <p>
     * The following defaults are set in the constructor:
     * randomiser is instantiated as a MersenneTwister,
     * theta is set to 0.001,
     * reinitialisationRatio is set to 0.5 (reinitialise one half of the swarm)
     */
    public DynamicIterationStrategy() {
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new RandomSentryDetectionStrategy<PSO>();
        this.responseStrategy = new PartialReinitialisationResponseStrategy<PSO>();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public DynamicIterationStrategy(DynamicIterationStrategy copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.responseStrategy = copy.responseStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
//    @Override
    public DynamicIterationStrategy getClone() {
        return new DynamicIterationStrategy(this);
    }

    /**
     * @see net.sourceforge.cilib.PSO.IterationStrategy#performIteration()
     *
     * Structure of Dynamic iteration strategy with re-initialisation:
     *
     * <ol>
     *   <li>Check for environment change</li>
     *   <li>If the environment has changed:</li>
     *   <ol>
     *     <li>Respond to change</li>
     *   <ol>
     *   <li>Perform normal itartion</li>
     * </ol>
     */
    public void performIteration(PSO algorithm) {
        boolean hasChanged = detectionStrategy.detect(algorithm);

        if (hasChanged)
            responseStrategy.respond(algorithm);

        iterationStrategy.performIteration(algorithm);
    }

    /**
     * Get the current {@linkplain IterationStrategy}.
     * @return The current {@linkplain IterationStrategy}.
     */
    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the {@linkplain IterationStrategy} to be used.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * Get the currently defined {@linkplain EnvironmentChangeDetectionStrategy}.
     * @return The current {@linkplain EnvironmentChangeDetectionStrategy}.
     */
    public EnvironmentChangeDetectionStrategy<PSO> getDetectionStrategy() {
        return detectionStrategy;
    }

    /**
     * Set the {@linkplain EnvironmentChangeDetectionStrategy} to be used.
     * @param detectionStrategy The {@linkplain EnvironmentChangeDetectionStrategy} to set.
     */
    public void setDetectionStrategy(EnvironmentChangeDetectionStrategy<PSO> detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    /**
     * Get the currently defined {@linkplain EnvironmentChangeResponseStrategy},
     * @return The current {@linkplain EnvironmentChangeResponseStrategy}.
     */
    public EnvironmentChangeResponseStrategy<PSO> getResponseStrategy() {
        return responseStrategy;
    }

    /**
     * Set the current {@linkplain EnvironmentChangeResponseStrategy} to use.
     * @param responseStrategy The {@linkplain EnvironmentChangeResponseStrategy} to set.
     */
    public void setResponseStrategy(EnvironmentChangeResponseStrategy<PSO> responseStrategy) {
        this.responseStrategy = responseStrategy;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }
}
