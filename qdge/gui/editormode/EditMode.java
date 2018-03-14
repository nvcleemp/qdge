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

import java.util.EnumMap;
import java.util.Map;

import qdge.data.Edge;
import qdge.data.Graph;
import qdge.data.Vertex;
import qdge.gui.GraphPanel;
import qdge.gui.undo.EdgeCreationHistoryItem;
import qdge.gui.undo.EdgeRemovalHistoryItem;
import qdge.gui.undo.EdgeVertexCreationHistoryItem;
import qdge.gui.undo.HistoryModel;
import qdge.gui.undo.MoveHistoryItem;
import qdge.gui.undo.VertexRemovalHistoryItem;

/**
 * Editor mode which edits the underlying graph: it supports easy addition and
 * removal of vertices and edges. Vertices can be added by double-clicking.
 * Edges can be added by dragging while holding the SHIFT key down. If an edge
 * is started from a vertex, but does not end in a vertex, then a new vertex is
 * automatically created at the end of the new edge. Click vertices and edges
 * while holding down the CTRL key to delete them.
 * 
 * @author nvcleemp
 */
class EditMode extends AbstractEditorMode {
    
    private DragHandler currentHandler = null;
    private final DragHandler defaultHandler = new DragHandler() {

        private Vertex currentVertex;
        private float startX;
        private float startY;
        
        @Override
        public void start(float x, float y) {
            currentVertex = findNearestVertex(x, y);
            if(currentVertex!=null){
                startX = currentVertex.getX();
                startY = currentVertex.getY();
            }
        }

        @Override
        public void drag(float x, float y) {
            if(currentVertex!=null){
                currentVertex.setXY(x, y);
            }
        }

        @Override
        public void end(float x, float y) {
            if(currentVertex!=null){
                history.push(new MoveHistoryItem(
                        currentVertex, 
                        startX, startY,
                        currentVertex.getX(), currentVertex.getY()));
            }
            currentVertex = null;
        }
    };
    private Map<ModifierKey, DragHandler> handlers = new EnumMap<>(ModifierKey.class);
    {
        handlers.put(ModifierKey.SHIFT, new DragHandler() {
            
            private Vertex start;

            @Override
            public void start(float x, float y) {
                start = findNearestVertex(x, y);
                panel.setHalfEdgeStart(start);
            }

            @Override
            public void drag(float x, float y) {
                panel.setHalfEdgeEnd(x, y);
            }

            @Override
            public void end(float x, float y) {
                panel.setHalfEdgeStart(null);
                if(start!=null){
                    Vertex end = findNearestVertex(x, y);
                    if(end == null){
                        end = graph.addNewVertex(x, y);
                        Edge e = graph.addNewEdge(start, end);
                        history.push(new EdgeVertexCreationHistoryItem(end, e, graph));
                    } else if (!end.equals(start)){
                        Edge e = graph.addNewEdge(start, end);
                        history.push(new EdgeCreationHistoryItem(e, graph));
                    }
                }
            }
        });
    }
    
    private final HistoryModel history;

    public EditMode(Graph graph, GraphPanel panel, HistoryModel history) {
        super(graph, panel);
        this.history = history;
    }

    @Override
    public void clicked(float x, float y, int button, int clickCount, ModifierKey key) {
        if(ModifierKey.CTRL.equals(key)){
            Vertex v = panel.getFocusVertex();
            Edge e = panel.getFocusEdge();
            if(v!=null){
                history.push(new VertexRemovalHistoryItem(v, graph));
                graph.removeVertex(v);
            } else if(e!=null){
                history.push(new EdgeRemovalHistoryItem(e, graph));
                graph.removeEdge(e);
            }
        } else if(clickCount > 1){
            graph.addNewVertex(x, y);
        }
    }

    @Override
    public void dragStarted(float x, float y, int button, ModifierKey key) {
        currentHandler = handlers.getOrDefault(key, defaultHandler);
        if(currentHandler!=null) currentHandler.start(x, y);
    }

    @Override
    public void dragged(float x, float y) {
        if(currentHandler!=null){
            currentHandler.drag(x, y);
        }
    }

    @Override
    public void dragEnded(float x, float y) {
        if(currentHandler!=null){
            currentHandler.end(x, y);
            currentHandler = null;
        }
    }
    
    private static interface DragHandler {
        void start(float x, float y);
        void drag(float x, float y);
        void end(float x, float y);
    }
}
