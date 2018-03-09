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
        panel.setFocus(findNearestVertex(x, y));
    }

    @Override
    public void clicked(float x, float y, int button, int clickcount, ModifierKey key) {}

    @Override
    public void dragStarted(float x, float y, int button, ModifierKey key) {}

    @Override
    public void dragged(float x, float y) {}

    @Override
    public void dragEnded(float x, float y) {}

    Vertex findNearestVertex(float x, float y) {
        float minDist = GraphPanel.VERTEX_RADIUS * GraphPanel.VERTEX_RADIUS;
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

    static float getDistance2(float x1, float y1, float x2, float y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
    
}