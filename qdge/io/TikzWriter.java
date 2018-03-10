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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import qdge.data.Graph;
import qdge.data.Vertex;

/**
 * Class which handles I/O for the TikZ format.
 * 
 * @author nvcleemp
 */
public class TikzWriter implements FileGraphWriter, StringGraphWriter {

    /**
     * Encodes a given graph in TikZ format.
     * 
     * @param graph
     * @return 
     */
    @Override
    public String writeToString(Graph graph) {
        StringBuilder sb = new StringBuilder("\\begin{tikzpicture}[every node/.style={circle, draw, inner sep=5pt}]\n");
        
        int counter = 1;
        Map<Vertex, Integer> labels = new HashMap<>();
        for (Vertex v : graph.vertices().toArray(Vertex[]::new)) {
            sb.append(String.format("   \\node (%d) at (%.3f,%.3f) {};\n", counter, v.getX()/10, v.getY()/10));
            labels.put(v, counter);
            counter++;
        }
        
        sb.append("\n");
        
        graph.edges().forEach(e -> {
            sb.append(String.format("   \\draw (%d) to (%d);\n",
                    labels.get(e.getV()),
                    labels.get(e.getW())));
        });
        
        sb.append("\\end{tikzpicture}\n");
        
        return sb.toString();
    }

    @Override
    public void writeToFile(File file, Graph graph) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(writeToString(graph));
        }
    }

    @Override
    public String getFormatName() {
        return "TikZ";
    }
    
}
