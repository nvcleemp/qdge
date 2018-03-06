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
 * Abstract superclass for transformations which apply the same operation to
 * each vertex.
 * 
 * @author nvcleemp
 */
public abstract class AbstractUniversalGraphTransformation {
    
    public void transformGraph(Graph g){
        g.vertices().forEach(this::transformVertex);
    }
    
    public abstract void transformVertex(Vertex v);
}
