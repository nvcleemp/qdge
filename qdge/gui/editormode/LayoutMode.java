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

package qdge.gui.editormode;

import qdge.data.Edge;
import qdge.data.Graph;
import qdge.data.GraphSelectionModel;
import qdge.data.Vertex;
import qdge.gui.GraphPanel;
import qdge.gui.undo.HistoryModel;
import qdge.gui.undo.MoveHistoryItem;

/**
 * Editor mode which does not edit the underlying graph
 * @author nvcleemp
 */
class LayoutMode extends AbstractEditorMode {
    
    private Vertex currentVertex = null;
    private final HistoryModel history;
    private final GraphSelectionModel selectionModel;
    private float startX;
    private float startY;

    public LayoutMode(Graph graph, GraphPanel panel, HistoryModel history, GraphSelectionModel selectionModel) {
        super(graph, panel);
        this.history = history;
        this.selectionModel = selectionModel;
    }

    @Override
    public void clicked(float x, float y, int button, int clickcount, ModifierKey key) {
        if(button > 1){
            Vertex v = findNearestVertex(x, y);
            Edge e = findNearestEdge(x, y);
            if(v!=null){
                selectionModel.toggleSelected(v);
            } else if(e!=null){
                selectionModel.toggleSelected(e);
            }
        }
    }

    @Override
    public void dragStarted(float x, float y, int button, ModifierKey key) {
        currentVertex = findNearestVertex(x, y);
        if(currentVertex!=null){
            startX = currentVertex.getX();
            startY = currentVertex.getY();
        }
    }

    @Override
    public void dragged(float x, float y) {
        if(currentVertex!=null){
            currentVertex.setXY(x, y);
        }
    }

    @Override
    public void dragEnded(float x, float y) {
        if(currentVertex!=null){
            history.push(new MoveHistoryItem(
                    currentVertex, 
                    startX, startY,
                    currentVertex.getX(), currentVertex.getY()));
        }
        currentVertex = null;
    }    
}
