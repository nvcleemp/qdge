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

package qdge.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.function.BiFunction;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import qdge.data.Graph;
import qdge.data.GraphSelectionModel;
import qdge.data.Vertex;

/**
 *
 * @author nvcleemp
 */
public class ReferenceVertexSelectionDialog extends JDialog {
    
    public static final ReferenceVertexSelectionItem FIRST_SELECTED = 
            new ReferenceVertexSelectionItem("First selected", (g, sm) -> 
                    sm.getOrderedSelectedVerticesStream()
                            .sequential()
                            .findFirst()
                            .orElse(null));
    
    public static final ReferenceVertexSelectionItem LAST_SELECTED = 
            new ReferenceVertexSelectionItem("Last selected", (g, sm) -> 
                    sm.getOrderedSelectedVerticesStream()
                            .sequential()
                            .reduce((__,n)->n)
                            .orElse(null));
    
    private ReferenceVertexSelectionItem selection = null;

    public ReferenceVertexSelectionDialog(String title, ReferenceVertexSelectionItem... items) {
        super((Frame)null, title, true);
        
        JPanel itemsPanel = new JPanel(new GridLayout(items.length, 1));
        ButtonGroup group = new ButtonGroup();
        
        for (ReferenceVertexSelectionItem item : items) {
            final ReferenceVertexSelectionItem fItem = item;
            JRadioButton radioButton = new JRadioButton(item.getName());
            group.add(radioButton);
            radioButton.addActionListener(e -> {
                selection = fItem;
            });
            itemsPanel.add(radioButton);
        }
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonsPanel.add(new JButton(new AbstractAction("OK") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        }));
        buttonsPanel.add(new JButton(new AbstractAction("Cancel") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                selection = null;
                setVisible(false);
            }
        }));
        
        setLayout(new BorderLayout());
        add(itemsPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);
        
        pack();
    }
    
    public BiFunction<Graph, GraphSelectionModel, Vertex> getSelector(){
        return selection==null ? null : selection.getSelector();
    }
    
    public static class ReferenceVertexSelectionItem {
        
        private final String name;
        
        private final BiFunction<Graph, GraphSelectionModel, Vertex> selector;

        public ReferenceVertexSelectionItem(String name, BiFunction<Graph, GraphSelectionModel, Vertex> selector) {
            this.name = name;
            this.selector = selector;
        }

        public String getName() {
            return name;
        }

        public BiFunction<Graph, GraphSelectionModel, Vertex> getSelector() {
            return selector;
        }
    }
    
}
