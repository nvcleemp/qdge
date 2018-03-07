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

import qdge.data.Graph;
import qdge.gui.dialogs.StringExportDialog;
import qdge.io.StringGraphWriter;

/**
 * Action which exports a graph as string using a StringGraphWriter.
 * @author nvcleemp
 */
public class ExportStringAction extends AbstractAction {

    private final Graph graph;
    private final StringGraphWriter writer;
    
    public ExportStringAction(Graph graph, StringGraphWriter writer) {
        super("Export as " + writer.getFormatName() + " string...");
        this.graph = graph;
        this.writer = writer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new StringExportDialog(writer.writeToString(graph)).setVisible(true);
    }
    
}
