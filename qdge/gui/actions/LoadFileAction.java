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

import qdge.data.Graph;
import qdge.io.FileGraphReader;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import qdge.gui.undo.HistoryModel;

/**
 * Action which reads a graph in a specific format from a file.
 * 
 * @author nvcleemp
 */
public class LoadFileAction extends AbstractAction {

    private final Graph graph;
    
    private final FileGraphReader reader;
    
    private final HistoryModel history;
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    public LoadFileAction(Graph graph, FileGraphReader reader, HistoryModel history) {
        super("Load " + reader.getFormatName() + " from file...");
        this.reader = reader;
        this.graph = graph;
        this.history = history;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            graph.clear();
            history.clear();
            File file = fileChooser.getSelectedFile();
            try {
                reader.readFromFile(file, graph);
            } catch (IOException ex) {
                Logger.getLogger(LoadFileAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
