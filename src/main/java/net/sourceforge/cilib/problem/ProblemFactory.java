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

/**
 *
 * <p>
 * A factory used to produce a {@link Problem} must satisfy this interface.
 * {@link net.sourceforge.cilib.xml.XMLProblemFactory} implements this
 * interface so that problems can be created for the Simulator.
 * </p>
 * <p>
 * To avoid the use of XML you can create your own implementation of <code>ProblemFactory</code> to create
 * appropriately configured problems.
 * </p>
 *
 * @author  Edwin Peer
 */
public interface ProblemFactory {

    /**
     * Returns a newly contructed problem.
     *
     * @return A new {@link Problem}.
     */
    public Problem newProblem();
}