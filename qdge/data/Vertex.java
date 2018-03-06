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

package qdge.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertex together with its coordinates.
 * @author nvcleemp
 */
public class Vertex {
    
    private float x;
    private float y;
    
    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        fireVertexMoved();
    }
    
    private final List<VertexListener> listeners = new ArrayList<>();
    
    public void addListener(VertexListener listener){
        listeners.add(listener);
    }
    
    public void removeListener(VertexListener listener){
        listeners.remove(listener);
    }
    
    private void fireVertexMoved(){
        listeners.parallelStream().forEach(l->l.vertexMoved());
    }
}
