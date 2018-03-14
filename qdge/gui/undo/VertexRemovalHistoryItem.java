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

import java.util.List;

import qdge.data.Edge;
import qdge.data.Graph;
import qdge.data.Vertex;

/**
 *
 * @author nvcleemp
 */
public class VertexRemovalHistoryItem extends AbstractHistoryItem {
    
    private final Vertex v;
    private final Graph graph;
    private final List<Edge> incidentEdges;

    public VertexRemovalHistoryItem(Vertex v, Graph graph) {
        super("delete vertex");
        this.v = v;
        this.graph = graph;
        this.incidentEdges = graph.incidentEdges(v);
    }

    @Override
    public void undo() {
        graph.addVertex(v);
        for (Edge e : incidentEdges) {
            graph.addEdge(e);
        }
    }

    @Override
    public void redo() {
        graph.removeVertex(v);
    }
    
}
