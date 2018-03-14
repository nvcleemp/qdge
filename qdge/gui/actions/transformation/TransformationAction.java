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

package qdge.gui.actions.transformation;

import qdge.data.Graph;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import qdge.gui.undo.HistoryModel;
import qdge.gui.undo.TransformationHistoryItem;

import qdge.transformations.GraphTransformation;

/**
 * Action which applies a given transformation to the graph.
 * 
 * @author nvcleemp
 */
public class TransformationAction extends AbstractAction {
    
    private final Graph graph;
    private final GraphTransformation transformation;
    private final String name;
    private final HistoryModel history;

    public TransformationAction(String name, GraphTransformation transformation, Graph graph, HistoryModel history) {
        super(name);
        this.transformation = transformation;
        this.graph = graph;
        this.name = name;
        this.history = history;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphTransformation inverse = transformation.inverseTransformation(graph);
        transformation.transformGraph(graph);
        history.push(new TransformationHistoryItem(graph, name, transformation, inverse));
    }
    
}
