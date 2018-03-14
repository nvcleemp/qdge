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

package qdge.gui.undo;

import qdge.data.Graph;
import qdge.transformations.GraphTransformation;

/**
 *
 * @author nvcleemp
 */
public class TransformationHistoryItem extends AbstractHistoryItem {
    
    private final Graph graph;
    private final GraphTransformation action;
    private final GraphTransformation inverseAction;

    public TransformationHistoryItem(Graph graph, String description, GraphTransformation action, GraphTransformation inverseAction) {
        super(description);
        this.graph = graph;
        this.action = action;
        this.inverseAction = inverseAction;
    }

    @Override
    public void undo() {
        inverseAction.transformGraph(graph);
    }

    @Override
    public void redo() {
        action.transformGraph(graph);
    }

    
}
