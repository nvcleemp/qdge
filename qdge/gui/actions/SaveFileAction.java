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
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import qdge.io.FileGraphWriter;

/**
 *
 * @author nvcleemp
 */
public class SaveFileAction extends AbstractAction {

    private final Graph graph;
    
    private final FileGraphWriter writer;
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    public SaveFileAction(Graph graph, FileGraphWriter writer) {
        super("Save as " + writer.getFormatName() + " file...");
        this.graph = graph;
        this.writer = writer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            try {
                writer.writeToFile(fileChooser.getSelectedFile(), graph);
            } catch (IOException ex) {
                Logger.getLogger(SaveFileAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
