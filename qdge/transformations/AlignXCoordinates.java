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
import qdge.data.GraphSelectionModel;
import qdge.data.Vertex;

/**
 * Sets the X coordinate of all selected vertices to the same value.
 * 
 * @author nvcleemp
 */
public class AlignXCoordinates extends AbstractUniversalSelectionMovementGraphTransformation implements HasReferenceVertex {
    
    private Vertex reference;

    public AlignXCoordinates(GraphSelectionModel selectionModel) {
        super(selectionModel);
    }

    @Override
    public void transformVertex(Vertex v) {
        if(reference==null)
            throw new IllegalStateException("Reference is not set");
        v.setXY(reference.getX(), v.getY());
    }

    @Override
    public GraphTransformation repeatTransformation(Graph g) {
        if(reference==null)
            throw new IllegalStateException("Reference is not set");
        final FixedRepositioningGraphTransformation repeat = new FixedRepositioningGraphTransformation();
        getVertices(g).stream().forEach(v -> {
            repeat.setPosition(v, reference.getX(), v.getY());
        });
        return repeat;
    }

    @Override
    public void setReferenceVertex(Vertex reference) {
        this.reference = reference;
    }
    
}
