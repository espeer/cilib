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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A velocity update strategy that utilizes the constriction coefficient as
 * developed by M. Clerc.
 * <p>References:
 * <ul>
 * <li>
 * <pre>
{@literal @}@INPROCEEDINGS{870279,
title={Comparing inertia weights and constriction factors in particle swarm optimization},
author={Eberhart, R.C. and Shi, Y.},
booktitle={Evolutionary Computation, 2000. Proceedings of the 2000 Congress on},
year={2000},
month={},
volume={1},
number={},
pages={84-88 vol.1},
abstract={The performance of particle swarm optimization using an inertia weight is compared
with performance using a constriction factor. Five benchmark functions are used for the
comparison. It is concluded that the best approach is to use the constriction factor while
limiting the maximum velocity Vmax to the dynamic range of the variable Xmax on each
dimension. This approach provides performance on the benchmark functions superior to any
other published results known by the authors},
keywords={evolutionary computationbenchmark functions, constriction factors, inertia weights, particle swarm optimization},
doi={10.1109/CEC.2000.870279},
ISSN={}, }
</pre>
 * </li>
 * <li>
 * <pre>
{@literal @}ARTICLE{985692,
title={The particle swarm - explosion, stability, and convergence in a multidimensional complex space},
author={Clerc, M. and Kennedy, J.},
journal={Evolutionary Computation, IEEE Transactions on},
year={2002},
month={Feb},
volume={6},
number={1},
pages={58-73},
abstract={The particle swarm is an algorithm for finding optimal regions of complex search spaces
through the interaction of individuals in a population of particles. This paper analyzes a particle's
trajectory as it moves in discrete time (the algebraic view), then progresses to the view of it in
continuous time (the analytical view). A five-dimensional depiction is developed, which describes
the system completely. These analyses lead to a generalized model of the algorithm, containing a set
of coefficients to control the system's convergence tendencies. Some results of the particle swarm optimizer,
implementing modifications derived from the analysis, suggest methods for altering the original algorithm
in ways that eliminate problems and increase the ability of the particle swarm to find optima of some well-studied
test functions },
keywords={convergence of numerical methods, genetic algorithms, numerical stability, search problemsconvergence,
evolutionary computation, multidimensional complex space, optimization, particle swarm, particle trajectory,
search spaces, stability},
doi={10.1109/4235.985692},
ISSN={1089-778X}, }
</pre>
 * </li>
 * </ul>
 *
 * <p>
 * Note, this strategy does not use the inertia control parameter.
 * Certain constraints are imposed on the other control parameters in order to
 * calculate the constriction coefficient, namely:
 * $c1 + c2 \leq 4$ , and
 * $\kappa \in [0, 1]$
 *
 * @author andrich
 */
public class ConstrictionVelocityUpdate implements VelocityUpdateStrategy {

    private static final long serialVersionUID = -4470110903487138758L;
    private ControlParameter socialAcceleration;
    private ControlParameter cognitiveAcceleration;
    private ControlParameter vMax;
    private ControlParameter kappa;
    private ControlParameter constrictionCoefficient;

    /**
     * Default constructor. The values given to the control parameters attempt to
     * adhere to the constraints of calculating the constriction constant, but do not
     * necessarily represent good values.
     */
    public ConstrictionVelocityUpdate() {
        socialAcceleration = new RandomizingControlParameter();
        cognitiveAcceleration = new RandomizingControlParameter();
        vMax = new ConstantControlParameter();
        kappa = new ConstantControlParameter();
        constrictionCoefficient = null;

        socialAcceleration.setParameter(2.05);
        cognitiveAcceleration.setParameter(2.05);
        kappa.setParameter(1.0);
        vMax.setParameter(Double.MAX_VALUE);
    }

    /**
     * Copy constructor.
     * @param copy the ConstrictionVelocityUpdate to copy.
     */
    public ConstrictionVelocityUpdate(ConstrictionVelocityUpdate copy) {
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.vMax = copy.vMax.getClone();
        this.kappa = copy.kappa.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ConstrictionVelocityUpdate getClone() {
        return new ConstrictionVelocityUpdate(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void updateVelocity(Particle particle) {
        // lazy construction (necessary to do this after user has set c1 and c2, and to only do it once per particle).
        if (constrictionCoefficient == null) {
            calculateConstrictionCoefficient();
        }
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector bestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();


        for (int i = 0; i < particle.getDimension(); ++i) {
            double value = constrictionCoefficient.getParameter() * (velocity.getReal(i) +
                    (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter() +
                    (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter());
            velocity.setReal(i, value);

            clamp(velocity, i);
        }
    }

    /**
     * Clamp to maximum velocity.
     * @param velocity The {@link Vector} to be clamped.
     * @param i The dimension index to be clamped
     */
    protected void clamp(Vector velocity, int i) {
        // if vMax is not set (or set as max), it is unnecessary to clamp
        if (Double.compare(vMax.getParameter(), Double.MAX_VALUE) == 0) {
            return;
        }
        if (velocity.getReal(i) < -vMax.getParameter()) {
            velocity.setReal(i, -vMax.getParameter());
        } else if (velocity.getReal(i) > vMax.getParameter()) {
            velocity.setReal(i, vMax.getParameter());
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void updateControlParameters(Particle particle) {
        this.kappa.updateParameter();
        this.cognitiveAcceleration.updateParameter();
        this.socialAcceleration.updateParameter();
        this.vMax.updateParameter();
    }

    /**
     * Calculate the constriction coefficient as well as the
     * maximum acceleration.
     */
    private void calculateConstrictionCoefficient() {
        double c1 = ((RandomizingControlParameter) cognitiveAcceleration).getControlParameter().getParameter();
        double c2 = ((RandomizingControlParameter) socialAcceleration).getControlParameter().getParameter();
        
        double phi = c1 + c2;
        if (phi < 4.0) {
            throw new UnsupportedOperationException("Parameter constraint violation: " +
                    "The sum of the Cognitive (" + c1 + ") and Social (" + c2 + ") acceleration parameters " +
                    "has to be greater than or equal to 4.");
        }
        double chi;
        chi = (2 * kappa.getParameter()) /
                Math.abs(2 - phi - Math.sqrt(phi * (phi - 4.0)));
        
        constrictionCoefficient = new ConstantControlParameter();
        constrictionCoefficient.setParameter(chi);
    }

    /**
     * Get the coginitive acceleration parameter.
     * @return the cognitive acceleration {@link ControlParameter control parameter }.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the coginitive acceleration parameter.
     * @param cognitiveAcceleration the new cognitive acceleration {@link ControlParameter control parameter }.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveAcceleration) {
        this.cognitiveAcceleration = cognitiveAcceleration;
    }

    /**
     * Get the Kappa control parameter.
     * @return the kappa {@link ControlParameter control parameter }.
     */
    public ControlParameter getKappa() {
        return kappa;
    }

    /**
     * Set the Kappa control parameter.
     * @param kappa the new kappa {@link ControlParameter control parameter }.
     */
    public void setKappa(ControlParameter kappa) {
        this.kappa = kappa;
    }

    /**
     * Get the social acceleration parameter.
     * @return the social acceleration {@link ControlParameter control parameter }.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the social acceleration parameter.
     * @param socialAcceleration the new social accerelation {@link ControlParameter control parameter }.
     */
    public void setSocialAcceleration(ControlParameter socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }

    /**
     * Get the maximum velocity parameter.
     * @return the maximum velocity {@link ControlParameter control parameter }.
     */
    public ControlParameter getvMax() {
        return vMax;
    }

    /**
     * Set the maximum velocity parameter.
     * @param vMax the new maximum velocity {@link ControlParameter control parameter }.
     */
    public void setvMax(ControlParameter vMax) {
        this.vMax = vMax;
    }

    /**
     * Gets the constriction coefficient.
     * @return the constriction coefficient  {@link ControlParameter control parameter }.
     */
    public ControlParameter getConstrictionCoefficient() {
        return constrictionCoefficient;
    }

    /**
     * Sets the constriction coefficient.
     * @param constrictionCoefficient the new constriction coefficient  {@link ControlParameter control parameter }.
     */
    public void setConstrictionCoefficient(ControlParameter constrictionCoefficient) {
        this.constrictionCoefficient = constrictionCoefficient;
    }
}
