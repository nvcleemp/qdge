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
 * Centers the bounding box of the graph.
 * 
 * @author nvcleemp
 */
public class CenterBoundingBox extends AbstractUniversalGraphTransformation {
    
    private float x;
    private float y;

    @Override
    public void transformGraph(Graph g) {
        float maxX = (float)g.vertices().mapToDouble(v -> v.getX()).max().getAsDouble();
        float minX = (float)g.vertices().mapToDouble(v -> v.getX()).min().getAsDouble();
        float maxY = (float)g.vertices().mapToDouble(v -> v.getY()).max().getAsDouble();
        float minY = (float)g.vertices().mapToDouble(v -> v.getY()).min().getAsDouble();
        x = -(maxX + minX)/2;
        y = -(maxY + minY)/2;
        super.transformGraph(g);
    }

    @Override
    public void transformVertex(Vertex v) {
        v.setXY(v.getX() + x, v.getY() + y);
    }
    
}
