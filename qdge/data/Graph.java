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
import java.util.stream.Stream;

/**
 * A graph with coordinates associated with the vertices.
 * @author nvcleemp
 */
public class Graph {
    
    private final List<GraphListener> listeners = new ArrayList<>();
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    private final List<Edge> edges = new ArrayList<>();
    
    private final VertexListener vListener = this::fireDrawingChanged;
    
    public void addListener(GraphListener listener){
        listeners.add(listener);
    }
    
    public void removeListener(GraphListener listener){
        listeners.remove(listener);
    }
    
    private void fireDrawingChanged(){
        listeners.parallelStream().forEach(l->l.graphChanged());
    }
    
    public Vertex addNewVertex(float x, float y){
        Vertex v = new Vertex(x, y);
        vertices.add(v);
        v.addListener(vListener);
        fireDrawingChanged();
        return v;
    }
    
    public void addNewEdge(int v, int w){
        addNewEdge(vertices.get(v), vertices.get(w));
    }
    
    public void addNewEdge(Vertex v, Vertex w){
        edges.add(new Edge(v, w));
        fireDrawingChanged();
    }
    
    public void addNewEdgeIfDoesNotExist(Vertex v, Vertex w){
        if(edges.parallelStream()
                .anyMatch(e -> 
                        (e.getV().equals(v) && e.getW().equals(w)) ||
                                (e.getV().equals(w) && e.getW().equals(v)))){
            return;
        }
        edges.add(new Edge(v, w));
        fireDrawingChanged();
    }
    
    public Stream<Vertex> vertices(){
        return vertices.stream();
    }
    
    public Stream<Edge> edges(){
        return edges.stream();
    }
    
    public int getOrder(){
        return vertices.size();
    }
    
    public void clear(){
        vertices().forEach(v -> {v.removeListener(vListener);});
        vertices.clear();
        edges.clear();
        fireDrawingChanged();
    }
}
