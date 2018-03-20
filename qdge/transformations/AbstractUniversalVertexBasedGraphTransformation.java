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

import java.util.function.Predicate;

import qdge.data.Graph;
import qdge.data.Vertex;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractUniversalVertexBasedGraphTransformation extends AbstractVertexBasedGraphTransformation {

    public AbstractUniversalVertexBasedGraphTransformation(Predicate<Vertex> filter) {
        super(filter);
    }

    @Override
    public void transformGraph(Graph g) {
        g.vertices().filter(filter).forEach(this::transformVertex);
    }

    public abstract void transformVertex(Vertex v);
    
}
