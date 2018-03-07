/*
 * Copyright (C) 2018 Nico Van Cleemput <nico.vancleemput@gmail.com>
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

import qdge.gui.GraphPanel;

/**
 * Action which increases or decreases the zoom of a GraphPanel.
 * 
 * @author Nico Van Cleemput <nico.vancleemput@gmail.com>
 */
public class ZoomAction extends AbstractAction {
    
    private final Runnable action;

    public ZoomAction(boolean zoomIn, GraphPanel panel) {
        super("Zoom " + (zoomIn ? "in" : "out"));
        if(zoomIn)
            panel.addListener(e -> setEnabled(panel.getZoom()!=1));
        action = zoomIn ? panel::decrementZoom : panel::incrementZoom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        action.run();
    }
    
}
