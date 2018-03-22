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
 * Shifts all the selected vertices in a fixed direction.
 * 
 * @author nvcleemp
 */
public class SelectionShift extends AbstractUniversalSelectionMovementGraphTransformation {
    
    private final float x;
    private final float y;
    
    public SelectionShift(GraphSelectionModel selectionModel, float x, float y) {
        super(selectionModel);
        this.x = x;
        this.y = y;
    }

    @Override
    public void transformVertex(Vertex v) {
        v.setXY(v.getX() + x, v.getY() + y);
    }

    @Override
    public GraphTransformation repeatTransformation(Graph g) {
        final FixedRepositioningGraphTransformation repeat = new FixedRepositioningGraphTransformation();
        getVertices(g).stream().forEach(v -> {
            repeat.setPosition(v, v.getX() + x, v.getY() + y);
        });
        return repeat;
    }
    
}
