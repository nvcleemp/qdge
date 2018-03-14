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

import qdge.data.Edge;
import qdge.data.Graph;

/**
 *
 * @author nvcleemp
 */
public class EdgeRemovalHistoryItem extends AbstractHistoryItem {
    
    private final Edge e;
    private final Graph graph;

    public EdgeRemovalHistoryItem(Edge e, Graph graph) {
        super("delete edge");
        this.e = e;
        this.graph = graph;
    }

    @Override
    public void undo() {
        graph.addEdge(e);
    }

    @Override
    public void redo() {
        graph.removeEdge(e);
    }
    
}
