/*
 * Copyright (C) 2018 Nico Van Cleemput
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package qdge.transformations;

import qdge.data.Vertex;

/**
 * Rotates the graph about the origin through a set angle.
 * @author nvcleemp
 */
public class Rotation extends AbstractUniversalGraphTransformation {
    
    private double angle;

    public Rotation() {
        this(0.0);
    }

    public Rotation(double angle) {
        this.angle = angle;
    }

    public Rotation setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    @Override
    public void transformVertex(Vertex v) {
        //the matrix is transposed from what you would normally expect, because
        //the Y-axis is pointing down.
        v.setXY(
                (int)(Math.round(Math.cos(Math.PI*angle/180)*v.getX() + Math.sin(Math.PI*angle/180)*v.getY())),
                (int)(Math.round(-Math.sin(Math.PI*angle/180)*v.getX() + Math.cos(Math.PI*angle/180)*v.getY()))
        );
    }
    
}
