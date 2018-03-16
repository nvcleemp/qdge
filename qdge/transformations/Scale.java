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
 * Scales the graph by some factor.
 * @author nvcleemp
 */
public class Scale extends AbstractUniversalGraphTransformation {
    
    private float scale;

    public Scale(float scale) {
        this.scale = scale;
    }

    public Scale setScale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public void transformVertex(Vertex v) {
        v.setXY(v.getX()*scale, v.getY()*scale);
    }

    @Override
    public GraphTransformation inverseTransformation(Graph g) {
        return new Scale(1/scale);
    }

    @Override
    public GraphTransformation repeatTransformation(Graph g) {
        return new Scale(scale);
    }
    
}
