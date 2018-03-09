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

package qdge.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import qdge.data.Graph;
import qdge.data.Vertex;

/**
 * Class which handles I/O for the writegraph2d format.
 * 
 * @author nvcleemp
 */
public class WriteGraph2DHandler implements FileGraphReader, FileGraphWriter, StringGraphWriter {
    
    @Override
    public void readFromFile(File f, Graph graph) throws IOException{
        graph.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            String s = br.readLine(); //skip first line: header
            s = br.readLine();
            while(s!=null && !s.trim().equals("0")){
                String[] parts = s.trim().split("\\s+");
                int v = Integer.parseInt(parts[0]);
                graph.addNewVertex(Float.parseFloat(parts[1])*100, Float.parseFloat(parts[2])*100);
                for (int i = 3; i < parts.length; i++) {
                    int n = Integer.parseInt(parts[i]);
                    if(v>n) graph.addNewEdge(v-1, n-1);
                }
                s = br.readLine();
            }
        }
    }
    
    @Override
    public void writeToFile(File file, Graph graph) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(">>writegraph2d<<\n");
        writer.write(writeToString(graph));
    }
    
    @Override
    public String writeToString(Graph graph) {
        StringBuilder sb = new StringBuilder();
        Vertex[] vertices = graph.vertices().toArray(Vertex[]::new);
        Map<Vertex, Integer> labels = new HashMap<>();
        for (int i = 0; i < vertices.length; i++) {
            labels.put(vertices[i], i+1);
        }
        for (int i = 0; i < vertices.length; i++) {
            final Vertex v = vertices[i];
            sb.append(i+1)
                    .append("\t").append(v.getX()/100)
                    .append("\t").append(v.getY()/100)
                    .append("\t");
            graph.edges()
                    .filter(e -> e.getV().equals(v) || e.getW().equals(v))
                    .forEach(e -> {
                        sb.append(labels.get(e.getV().equals(v) ? e.getW() : e.getV())).append(" ");
                    });
            sb.append("\n");
        }
        sb.append("0\n");
        return sb.toString();
    }

    @Override
    public String getFormatName() {
        return "writegraph2d";
    }
}
