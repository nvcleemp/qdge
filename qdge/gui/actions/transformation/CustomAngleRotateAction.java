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

package qdge.gui.actions.transformation;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import qdge.data.Graph;
import qdge.gui.undo.HistoryModel;
import qdge.gui.undo.TransformationHistoryItem;
import qdge.transformations.GraphTransformation;
import qdge.transformations.Rotation;

/**
 *
 * @author nvcleemp
 */
public class CustomAngleRotateAction extends AbstractAction {
    
    private final Graph graph;
    private final Rotation transformer;
    private final HistoryModel history;

    public CustomAngleRotateAction(String name, Graph graph, HistoryModel history) {
        super(name);
        this.transformer = new Rotation(0);
        this.graph = graph;
        this.history = history;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String angleS = JOptionPane.showInputDialog(null, "Give the angle in degrees:", "Rotation", JOptionPane.PLAIN_MESSAGE);
        if(angleS!=null){
            try {
                double angle = Double.parseDouble(angleS);
                transformer.setAngle(angle).transformGraph(graph);
                history.push(new TransformationHistoryItem(graph, "rotate graph", new Rotation(angle), new Rotation(-angle)));
            } catch (NumberFormatException nfe) {
                Logger.getLogger(CustomAngleRotateAction.class.getName()).log(Level.SEVERE, null, nfe);
            }
        }
    }
    
}
