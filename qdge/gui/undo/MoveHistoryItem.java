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

package qdge.gui.undo;

import qdge.data.Vertex;

/**
 *
 * @author nvcleemp
 */
public class MoveHistoryItem extends AbstractHistoryItem {

    private final Vertex v;
    private final float oldX;
    private final float oldY;
    private final float newX;
    private final float newY;

    public MoveHistoryItem(Vertex v, float oldX, float oldY, float newX, float newY) {
        super("move vertex");
        this.v = v;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }
    
    @Override
    public void undo() {
        v.setXY(oldX, oldY);
    }

    @Override
    public void redo() {
        v.setXY(newX, newY);
    }

}
