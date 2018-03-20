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

import java.util.stream.Collectors;
import qdge.data.Graph;
import qdge.data.GraphSelectionModel;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractUniversalSelectionMovementGraphTransformation extends AbstractUniversalSelectionGraphTransformation {

    public AbstractUniversalSelectionMovementGraphTransformation(GraphSelectionModel selectionModel) {
        super(selectionModel);
    }

    @Override
    public GraphTransformation inverseTransformation(Graph g) {
        return new FixedRepositioningGraphTransformation()
                .recordPositions(
                        g.vertices()
                                .filter(filter)
                                .collect(Collectors.toList()));
    }
    
}
