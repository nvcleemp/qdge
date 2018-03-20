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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import qdge.data.Graph;
import qdge.data.Vertex;

/**
 * An implementation of GraphTransformation which always moves certain vertices
 * to fixed coordinates.
 * 
 * @author nvcleemp
 */
public class FixedRepositioningGraphTransformation implements GraphTransformation {

    private final Map<Vertex, float[]> coordinates = new HashMap<>();
    
    @Override
    public void transformGraph(Graph g) {
        g.vertices()
                .filter(coordinates::containsKey)
                .forEach(v -> v.setXY(coordinates.get(v)[0], coordinates.get(v)[1]));
    }
    
    public FixedRepositioningGraphTransformation recordPositions(Collection<Vertex> vertices){
        vertices.stream()
                .forEach(v -> coordinates.put(v, new float[]{v.getX(), v.getY()}));
        return this;
    }

    public void setPosition(Vertex v, float... xy){
        coordinates.put(v, xy);
    }

    @Override
    public GraphTransformation inverseTransformation(Graph g) {
        throw new UnsupportedOperationException("Inverse is not supported for this class.");
    }

    @Override
    public GraphTransformation repeatTransformation(Graph g) {
        return this;
    }
    
}
