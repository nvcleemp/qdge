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

import qdge.gui.dialogs.InfoDialog;

/**
 * Action which shows an info dialog.
 * 
 * @author nvcleemp
 */
public class InfoAction extends AbstractAction {
    
    private final InfoDialog dialog;

    public InfoAction(String s) {
        this("Info", s);
    }

    public InfoAction(String name, String s) {
        super(name);
        dialog = new InfoDialog(s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }
    
}
