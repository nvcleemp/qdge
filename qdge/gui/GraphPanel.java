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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import qdge.data.Edge;
import qdge.data.GraphListener;
import qdge.data.Graph;
import qdge.data.Vertex;

/**
 * Panel to display a graph.
 * 
 * @author nvcleemp
 */
public class GraphPanel extends JPanel{
        
    public static final int VERTEX_RADIUS = 5;
    
    private final GraphListener listener = this::repaint;
    
    private Graph graph;
    
    private Vertex focusVertex = null;
    
    private Edge focusEdge = null;
    
    private Vertex halfEdgeStart = null;
    
    private float halfEdgeX = 0f;
    
    private float halfEdgeY = 0f;
    
    private boolean paintGrid = false;
    
    private int zoom = 5;

    public GraphPanel() {
        this(new Graph());
    }
    
    public GraphPanel(Graph graph) {
        this.graph = graph;
        graph.addListener(listener);
        listeners.add(e -> repaint());
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        paintComponentImpl(g, getWidth(), getHeight());
    }
    
    private void paintComponentImpl(Graphics g, int width, int height) {
        final Graphics g2 = g.create();
        g2.translate(width/2, height/2);
        if(paintGrid){
            paintGrid(g2, width, height);
        }
        ((Graphics2D)g2).scale(5.0/zoom, 5.0/zoom);
        if(halfEdgeStart!=null){
            paintEdge(g2, 
                    Math.round(halfEdgeStart.getX()), Math.round(halfEdgeStart.getY()),
                    Math.round(halfEdgeX), Math.round(halfEdgeY), 1, Color.BLACK);
        }
        graph.edges().forEach(e -> paintEdge(g2, e));
        graph.vertices().forEach(v -> paintVertex(g2, v));
    }

    private void paintGrid(Graphics g, int width, int height){
        final Graphics g2 = g.create();
        g2.setColor(Color.GRAY);
        int stepY = (height-40)/8;
        int stepX = (width-40)/8;
        for (int i = 0; i < 9; i++) {
            g2.drawLine((i-4)*stepX, 4*stepY, (i-4)*stepX, -4*stepY);
            g2.drawLine(4*stepX, (i-4)*stepY, -4*stepX, (i-4)*stepY);
        }
    }
    
    private void paintVertex(Graphics g, Vertex v){
        int x = Math.round(v.getX());
        int y = Math.round(v.getY());
        Color vertexColor = v.equals(focusVertex) ? Color.ORANGE : Color.WHITE;
        paintVertex(g, x, y, vertexColor, Color.BLACK);
    }
    
    private void paintVertex(Graphics g, int x, int y, Color color, Color edgeColor){
        Graphics2D g2 = (Graphics2D)(g.create());
        g2.setColor(color);
        g2.fillOval(x-VERTEX_RADIUS, y-VERTEX_RADIUS, 2*VERTEX_RADIUS, 2*VERTEX_RADIUS);
        g2.setColor(edgeColor);
        g2.drawOval(x-VERTEX_RADIUS, y-VERTEX_RADIUS, 2*VERTEX_RADIUS, 2*VERTEX_RADIUS);
    }
    
    private void paintEdge(Graphics g, Edge e){
        int x1 = Math.round(e.getV().getX());
        int y1 = Math.round(e.getV().getY());
        int x2 = Math.round(e.getW().getX());
        int y2 = Math.round(e.getW().getY());
        paintEdge(g, x1, y1, x2, y2, e.equals(focusEdge) ? 3 : 1, Color.BLACK);
    }
    
    private void paintEdge(Graphics g, int x1, int y1, int x2, int y2, float width, Color color){
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setStroke(new BasicStroke(width));
        g2.setColor(color);
        g2.drawLine(x1, y1, x2, y2);
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        } else {
            return new Dimension(500,500);
        }
    }

    public void setPaintGrid(boolean paintGrid) {
        this.paintGrid = paintGrid;
        fireStateChanged();
    }

    public boolean isPaintGrid() {
        return paintGrid;
    }
    
    public void incrementZoom(){
        zoom++;
        fireStateChanged();
    }
    
    public void decrementZoom(){
        if(zoom>1){
            zoom--;
            fireStateChanged();
        }
    }

    public int getZoom() {
        return zoom;
    }

    public void setHalfEdgeStart(Vertex halfEdgeStart) {
        this.halfEdgeStart = halfEdgeStart;
        if(halfEdgeStart!=null){
            this.halfEdgeX = halfEdgeStart.getX();
            this.halfEdgeY = halfEdgeStart.getY();
        }
    }

    public void setHalfEdgeEnd(float x, float y) {
        this.halfEdgeX = x;
        this.halfEdgeY = y;
        repaint();
    }

    public void setFocusVertex(Vertex focusVertex) {
        if(this.focusVertex != focusVertex){
            this.focusVertex = focusVertex;
            if(focusVertex!=null){
                this.focusEdge = null;
            }
            repaint();
        }
    }

    public Vertex getFocusVertex() {
        return focusVertex;
    }
    
    public void setFocusEdge(Edge focusEdge) {
        if(this.focusEdge != focusEdge){
            this.focusEdge = focusEdge;
            repaint();
        }
    }

    public Edge getFocusEdge() {
        return focusEdge;
    }
    
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    public void addListener(ChangeListener listener){
        listeners.add(listener);
    }
    
    public void removeListener(ChangeListener listener){
        listeners.remove(listener);
    }
    
    private void fireStateChanged(){
        final ChangeEvent e = new ChangeEvent(this);
        listeners.parallelStream().forEach(l->l.stateChanged(e));
    }
}
