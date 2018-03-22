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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author nvcleemp
 */
public class GraphSelectionModel {
    
    private final List<Vertex> selectedVerticesOrder = new ArrayList<>();
    private final Set<Vertex> selectedVertices = new HashSet<>();
    private final List<Edge> selectedEdgesOrder = new ArrayList<>();
    private final Set<Edge> selectedEdges = new HashSet<>();
    
    private final List<VertexSelectionListener> vertexListeners =
            new ArrayList<>();
    private final List<EdgeSelectionListener> edgeListeners =
            new ArrayList<>();
    
    public boolean isSelected(Vertex v){
        return selectedVertices.contains(v);
    }
    
    public boolean isSelected(Edge e){
        return selectedEdges.contains(e);
    }
    
    public void toggleSelected(Vertex v){
        if(isSelected(v)){
            selectedVertices.remove(v);
            selectedVerticesOrder.remove(v);
        } else {
            selectedVertices.add(v);
            selectedVerticesOrder.add(v);
        }
        fireVertexSelectionChanged();
    }
    
    public void toggleSelected(Edge e){
        if(isSelected(e)){
            selectedEdges.remove(e);
            selectedEdgesOrder.remove(e);
        } else {
            selectedEdges.add(e);
            selectedEdgesOrder.add(e);
        }
        fireEdgeSelectionChanged();
    }
    
    public void setSelected(Vertex v, boolean selected){
        if(selected){
            if(!isSelected(v)){
                selectedVertices.add(v);
                selectedVerticesOrder.add(v);
                fireVertexSelectionChanged();
            }
        } else {
            if(selectedVertices.remove(v)){
                selectedVerticesOrder.remove(v);
                fireVertexSelectionChanged();
            }
        }
    }
    
    public void setSelected(Edge e, boolean selected){
        if(selected){
            if(!isSelected(e)){
                selectedEdges.add(e);
                selectedEdgesOrder.add(e);
                fireEdgeSelectionChanged();
            }
        } else {
            if(selectedEdges.remove(e)){
                selectedEdgesOrder.remove(e);
                fireEdgeSelectionChanged();
            }
        }
    }
    
    public void clearVertexSelection(){
        if(selectedVertices.isEmpty()) return;
        
        selectedVertices.clear();
        selectedVerticesOrder.clear();
        fireVertexSelectionChanged();
    }
    
    public void clearEdgeSelection(){
        if(selectedEdges.isEmpty()) return;
        
        selectedEdges.clear();
        selectedEdgesOrder.clear();
        fireEdgeSelectionChanged();
    }
    
    public void clear(){
        clearVertexSelection();
        clearEdgeSelection();
    }
    
    public boolean isVertexSelectionEmpty(){
        return selectedVertices.isEmpty();
    }
    
    public boolean isEdgeSelectionEmpty(){
        return selectedEdges.isEmpty();
    }
    
    public List<Vertex> getOrderedSelectedVertices(){
        return new ArrayList<>(selectedVerticesOrder);
    }
    
    public List<Edge> getOrderedSelectedEdges(){
        return new ArrayList<>(selectedEdgesOrder);
    }
    
    public Stream<Vertex> getOrderedSelectedVerticesStream(){
        return selectedVerticesOrder.stream();
    }
    
    public Stream<Edge> getOrderedSelectedEdgesStream(){
        return selectedEdgesOrder.stream();
    }
    
    public void selectAllVertices(Graph g){
        g.vertices().filter(v -> !selectedVertices.contains(v)).forEach(v -> {
            selectedVertices.add(v);
            selectedVerticesOrder.add(v);
        });
        fireVertexSelectionChanged();
    }
    
    public void selectAllEdges(Graph g){
        g.edges().filter(e -> !selectedEdges.contains(e)).forEach(e -> {
            selectedEdges.add(e);
            selectedEdgesOrder.add(e);
        });
        fireEdgeSelectionChanged();
    }
    
    public void invertVertexSelection(Graph g){
        Set<Vertex> oldSelection = new HashSet<>(selectedVertices);
        selectedVerticesOrder.clear();
        selectedVertices.clear();
        g.vertices().filter(v -> !oldSelection.contains(v)).forEach(v -> {
            selectedVertices.add(v);
            selectedVerticesOrder.add(v);
        });
        fireVertexSelectionChanged();
    }
    
    public void invertEdgeSelection(Graph g){
        Set<Edge> oldSelection = new HashSet<>(selectedEdges);
        selectedEdgesOrder.clear();
        selectedEdges.clear();
        g.edges().filter(e -> !oldSelection.contains(e)).forEach(e -> {
            selectedEdges.add(e);
            selectedEdgesOrder.add(e);
        });
        fireEdgeSelectionChanged();
    }
    
    public void addVertexSelectionListener(VertexSelectionListener l){
        vertexListeners.add(l);
    }
    
    public void removeVertexSelectionListener(VertexSelectionListener l){
        vertexListeners.remove(l);
    }
    
    public void addEdgeSelectionListener(EdgeSelectionListener l){
        edgeListeners.add(l);
    }
    
    public void removeEdgeSelectionListener(EdgeSelectionListener l){
        edgeListeners.remove(l);
    }
    
    public void addGraphSelectionListener(GraphSelectionListener l){
        vertexListeners.add(l);
        edgeListeners.add(l);
    }
    
    public void removeGraphSelectionListener(GraphSelectionListener l){
        vertexListeners.remove(l);
        edgeListeners.remove(l);
    }
    
    private void fireVertexSelectionChanged(){
        int size = selectedVertices.size();
        vertexListeners.parallelStream().forEach(l -> l.vertexSelectionChanged(this, size));
    }
    
    private void fireEdgeSelectionChanged(){
        int size = selectedEdges.size();
        edgeListeners.parallelStream().forEach(l -> l.edgeSelectionChanged(this, size));
    }
}
