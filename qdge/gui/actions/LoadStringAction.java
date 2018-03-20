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

package qdge.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import qdge.data.Graph;
import qdge.data.GraphSelectionModel;
import qdge.gui.undo.HistoryModel;
import qdge.io.StringGraphReader;

/**
 * Action which reads a graph in a specific format from a string.
 * @author nvcleemp
 */
public class LoadStringAction extends AbstractAction {

    private final Graph graph;
    
    private final StringGraphReader reader;
    
    private final HistoryModel history;
    
    private final GraphSelectionModel selectionModel;
    
    public LoadStringAction(Graph graph, StringGraphReader reader, HistoryModel history, GraphSelectionModel selectionModel) {
        super("Load " + reader.getFormatName() + " from text...");
        this.reader = reader;
        this.graph = graph;
        this.history = history;
        this.selectionModel = selectionModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = JOptionPane.showInputDialog(null, "Give the graph", "Graph", JOptionPane.PLAIN_MESSAGE);
        if(s!=null){
            history.clear();
            selectionModel.clear();
            reader.readFromString(s, graph);
        }
    }
    
}
