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
package net.sourceforge.cilib.util;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * The Cosine Distance Measure (or vector dot product) is not a distance measure, but
 * rather a similarity metric. It is defined in:<br/>
 * <strong>Learning structure and concepts in data through data clustering</strong>
 * by Gregory James Hamerly, 2003
 * </p>
 * <p>
 * More positive values indicate similarity. The vector dot product is the sum of the
 * product of each attribute from two vectors being compared. Here we use a normalized
 * version of the metric so that the dot product is always a value between -1 and 1. The
 * vector dot product does not measure the difference in magnitude between two vectors;
 * only the difference in the angle between the vectors. If the (normalized) result = 1,
 * the two vectors are collinear and pointed in the same direction, if the (normalized)
 * result = -1, the two vectors are collinear and pointed in opposite directions, and if
 * the (normalized) result = 0, then the two vectors are orthogonal. We can convert this
 * similarity measure to a "distance" by subtracting it from one, which will always give
 * a value between 0 and 2. <strong>This is the approach we follow.</strong>
 * </p>
 */
public class CosineDistanceMeasure implements DistanceMeasure {

    /**
     * Calculate the "distance" (dot product) between (of) two vectors.
     * @param <T> The {@linkplain Vector} type.
     * @param x the one vector.
     * @param y the other vector.
     * @return the "distance" (or angle or dot product) (as a double) between the two vectors.
     * @throws IllegalArgumentException when the two vectors' dimension differ.
     * @TODO: Can this not be replaced with x.dot(y)?
     */
    public <T extends Type, U extends StructuredType<T>> double distance(U x, U y) {
        if(x.size() != y.size())
            throw new IllegalArgumentException("Cannot calculate Cosine Distance for vectors of different dimensions");

        Iterator<T> xIterator = x.iterator();
        Iterator<T> yIterator = y.iterator();

        double distance = 0.0, norm_x = 0.0, norm_y = 0.0;
        double x_i = 0.0, y_i = 0.0;
        for(int i = 0; i < x.size(); ++i) {
            Numeric xElement = (Numeric) xIterator.next();
            Numeric yElement = (Numeric) yIterator.next();
            x_i = xElement.getReal();
            y_i = yElement.getReal();
            distance += x_i * y_i;
            norm_x += x_i * x_i;
            norm_y += y_i * y_i;
        }
        norm_x = Math.sqrt(norm_x);
        norm_y = Math.sqrt(norm_y);
        if(norm_x <= 0.0 || norm_y <= 0.0)
            throw new ArithmeticException("Division by zero");

        // TODO: return x.dot(y) ???

        //convert to distance by subtracting from 1
        return 1.0 - (distance / (norm_x * norm_y));
    }

    /**
     * Calculate the "distance" (dot product) between (of) two vectors represented by Java Collection objects.
     * @param <T> The {@linkplain Vector} type.
     * @param x the one Java Collection object.
     * @param y the other Java Collection object.
     * @return the distance (or angle or dot product) (as a double) between the two vectors.
     * @throws IllegalArgumentException when the two vectors' dimension differ.
     */
    public <T extends Collection<? extends Number>> double distance(T x, T y) {
        if (x.size() != y.size())
            throw new IllegalArgumentException("Cannot calculate Cosine Distance for vectors  of different dimensions");

        Iterator<? extends Number> i = x.iterator();
        Iterator<? extends Number> j = y.iterator();
        double distance = 0.0, norm_x = 0.0, norm_y = 0.0;
        double x_i = 0.0, y_i = 0.0;
        while (i.hasNext() && j.hasNext()) {
            x_i = i.next().doubleValue();
            y_i = j.next().doubleValue();
            distance += x_i * y_i;
            norm_x += x_i * x_i;
            norm_y += y_i * y_i;
        }
        norm_x = Math.sqrt(norm_x);
        norm_y = Math.sqrt(norm_y);
        if(norm_x <= 0.0 || norm_y <= 0.0)
            throw new ArithmeticException("Division by zero");
        //convert to distance by subtracting from 1
        return 1.0 - (distance / (norm_x * norm_y));
    }
}
