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

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import qdge.data.Graph;
import qdge.data.Vertex;

/**
 * Abstract superclass for transformations which use a set of vertices as a basis
 * for the transformation.
 * 
 * @author nvcleemp
 */
public abstract class AbstractVertexBasedGraphTransformation implements GraphTransformation {
    
    protected final Predicate<Vertex> filter;

    public AbstractVertexBasedGraphTransformation(Predicate<Vertex> filter) {
        this.filter = filter;
    }
    
    public List<Vertex> getVertices(Graph g){
        return g.vertices().filter(filter).collect(Collectors.toList());
    }
}
