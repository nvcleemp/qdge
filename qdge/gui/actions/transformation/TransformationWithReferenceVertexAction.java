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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import qdge.data.Graph;
import qdge.data.GraphSelectionModel;
import qdge.data.Vertex;
import qdge.gui.dialogs.ReferenceVertexSelectionDialog;
import qdge.gui.undo.HistoryModel;
import qdge.gui.undo.TransformationHistoryItem;
import qdge.transformations.GraphTransformation;
import qdge.transformations.HasReferenceVertex;

/**
 * Action which applies a given transformation to the graph.
 * 
 * @author nvcleemp
 */
public class TransformationWithReferenceVertexAction extends AbstractAction {
    
    private final Graph graph;
    private final HasReferenceVertex transformation;
    private final String name;
    private final HistoryModel history;
    private final GraphSelectionModel selectionModel;
    private final ReferenceVertexSelectionDialog referenceDialog;

    public TransformationWithReferenceVertexAction(String name, HasReferenceVertex transformation, Graph graph, HistoryModel history, GraphSelectionModel selectionModel, ReferenceVertexSelectionDialog.ReferenceVertexSelectionItem... referenceTypes) {
        super(name);
        this.transformation = transformation;
        this.graph = graph;
        this.name = name;
        this.history = history;
        this.selectionModel = selectionModel;
        this.referenceDialog = new ReferenceVertexSelectionDialog(name, referenceTypes);
        
        setEnabled(!selectionModel.isVertexSelectionEmpty());
        selectionModel.addVertexSelectionListener((m, i) -> setEnabled(i>0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        referenceDialog.setVisible(true);
        if(referenceDialog.getSelector()!=null){
            Vertex v = referenceDialog.getSelector().apply(graph, selectionModel);
            transformation.setReferenceVertex(v);
            
            GraphTransformation inverse = transformation.inverseTransformation(graph);
            GraphTransformation repeat = transformation.repeatTransformation(graph);

            transformation.transformGraph(graph);
            history.push(new TransformationHistoryItem(graph, name, repeat, inverse));
        }
    }
    
}
