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

import qdge.data.Graph;
import qdge.data.Vertex;

/**
 * Shifts the graph a specified amount.
 * @author nvcleemp
 */
public class Shift extends AbstractUniversalGraphTransformation {
    
    private final float x;
    private final float y;

    public Shift(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void transformVertex(Vertex v) {
        v.setXY(v.getX() + x, v.getY() + y);
    }

    @Override
    public GraphTransformation inverseTransformation(Graph g) {
        return new Shift(-x, -y);
    }

    @Override
    public GraphTransformation repeatTransformation(Graph g) {
        return new Shift(x, y);
    }
    
}
