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

package qdge.gui;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

import qdge.gui.editormode.EditorMode;
import qdge.gui.editormode.ModifierKey;

/**
 *
 * @author nvcleemp
 */
public class GraphPanelMouseListener extends MouseInputAdapter {
    
    private final GraphPanel panel;
    private EditorMode editorMode;
    
    public GraphPanelMouseListener(EditorMode editorMode, GraphPanel graphPanel) {
        this.editorMode = editorMode;
        this.panel = graphPanel;
    }

    public void setEditorMode(EditorMode editorMode) {
        this.editorMode = editorMode;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        float x = getCoordinate(e.getX() - panel.getWidth()/2);
        float y = getCoordinate(e.getY() - panel.getHeight()/2);
        editorMode.moved(x, y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        float x = getCoordinate(e.getX() - panel.getWidth()/2);
        float y = getCoordinate(e.getY() - panel.getHeight()/2);
        editorMode.clicked(x, y, e.getButton(), e.getClickCount(), getModifierKey(e.getModifiersEx()));
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        final float x = getCoordinate(e.getX() - panel.getWidth()/2);
        final float y = getCoordinate(e.getY() - panel.getHeight()/2);
        editorMode.dragStarted(x, y, e.getButton(), getModifierKey(e.getModifiersEx()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        final float x = getCoordinate(e.getX() - panel.getWidth()/2);
        final float y = getCoordinate(e.getY() - panel.getHeight()/2);
        editorMode.dragEnded(x, y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        final float x = getCoordinate(e.getX() - panel.getWidth()/2);
        final float y = getCoordinate(e.getY() - panel.getHeight()/2);
        editorMode.dragged(x, y);
        editorMode.moved(x, y);
    }
    
    private float getCoordinate(float coord){
        return coord/5.0f*panel.getZoom();
    }
    
    private ModifierKey getModifierKey(int modValue){
        if((modValue & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK)
            return ModifierKey.SHIFT;
        if((modValue & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK)
            return ModifierKey.CTRL;
        return null;
        
    }
}
