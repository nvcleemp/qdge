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

package qdge.gui.editormode;

import qdge.data.Graph;
import qdge.gui.GraphPanel;

/**
 *
 * @author nvcleemp
 */
public interface EditorMode {
    void moved(float x, float y);
    void clicked(float x, float y, int button, int clickcount, ModifierKey key);
    void dragStarted(float x, float y, int button, ModifierKey key);
    void dragged(float x, float y);
    void dragEnded(float x, float y);
    
    public static EditorMode layoutMode(Graph g, GraphPanel panel){
        return new LayoutMode(g, panel);
    }
    
    public static EditorMode createMode(Graph g, GraphPanel panel){
        return new CreateMode(g, panel);
    }
}
