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

import java.util.stream.Collectors;

import qdge.data.Edge;
import qdge.data.Graph;
import qdge.data.Vertex;
import qdge.gui.GraphPanel;

/**
 * Abstract implementation of {@link EditorMode} which has empty implementation
 * of each method.
 * 
 * @author nvcleemp
 */
abstract class AbstractEditorMode implements EditorMode {
    
    protected final Graph graph;
    protected final GraphPanel panel;

    public AbstractEditorMode(Graph graph, GraphPanel panel) {
        this.graph = graph;
        this.panel = panel;
    }

    @Override
    public void moved(float x, float y) {
        if(!isNearVertex(panel.getFocusVertex(), x, y)){
            panel.setFocusVertex(findNearestVertex(x, y));
        }
    }

    @Override
    public void clicked(float x, float y, int button, int clickcount, ModifierKey key) {}

    @Override
    public void dragStarted(float x, float y, int button, ModifierKey key) {}

    @Override
    public void dragged(float x, float y) {}

    @Override
    public void dragEnded(float x, float y) {}

    private static final float CLOSENESS_TOLERANCE = GraphPanel.VERTEX_RADIUS * GraphPanel.VERTEX_RADIUS;
    
    boolean isNearVertex(Vertex v, float x, float y){
        if(v==null){
            return false;
        }
        return getDistance2(x, y, v.getX(), v.getY()) < CLOSENESS_TOLERANCE;
    }
    
    Vertex findNearestVertex(float x, float y) {
        float minDist = CLOSENESS_TOLERANCE;
        Vertex nearest = null;
        for (Vertex v : graph.vertices().collect(Collectors.toList())) {
            float d = getDistance2(x, y, v.getX(), v.getY());
            if (d < minDist) {
                minDist = d;
                nearest = v;
            }
        }
        return nearest;
    }

    boolean isNearEdge(Edge e, float x, float y){
        if(e==null){
            return false;
        }
        return getDistance2(e, x, y) < CLOSENESS_TOLERANCE;
    }
    
    Edge findNearestEdge(float x, float y) {
        float minDist = CLOSENESS_TOLERANCE;
        Edge nearest = null;
        for (Edge e : graph.edges().collect(Collectors.toList())) {
            float d = getDistance2(e, x, y);
            if (d < minDist) {
                minDist = d;
                nearest = e;
            }
        }
        return nearest;
    }
    
    static float getDistance2(Edge e, float x, float y){
        final float x1 = e.getV().getX();
        final float y1 = e.getV().getY();
        final float x2 = e.getW().getX();
        final float y2 = e.getW().getY();
        float edgeLength = getDistance2(x1, y1, x2, y2);
        if (edgeLength == 0) {
            return getDistance2(x1, y1, x, y);
        }
        float t = ((x - x1)*(x2 - x1) + (y - y1)*(y2 - y1))/edgeLength;
        t = Math.max(0, Math.min(1, t));
        return getDistance2(x, y, x1 + t*(x2 - x1), y1 + t*(y2 - y1));
    }

    static float getDistance2(float x1, float y1, float x2, float y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
    
}
