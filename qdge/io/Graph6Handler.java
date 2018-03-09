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

import java.util.ArrayList;
import java.util.List;
import qdge.data.Graph;

/**
 * Class which handles I/O for the graph6 format. Most code here is taken
 * from HoG.
 * 
 * @author nvcleemp
 */
public class Graph6Handler implements StringGraphReader, StringGraphWriter {

    /**
     * Convert a graph6 string to a graph. Code taken from HoG.
     * 
     * @param s
     * @param graph 
     */
    @Override
    public void readFromString(String s, Graph graph) {
        byte[] code = s.getBytes();

        // check validity of the code
        for (int i = 0; i < code.length; i++) {
            if (code[i] < 63 || code[i] > 126) {
                throw new RuntimeException("Non-printable character in graph6 body");
            }
        }

        // decode
        int adjacencyStart;
        int numberOfVertices;
        if (code[0] == 126) {
            assert code.length > 4;
            if (code[1] == 126) {
                throw new RuntimeException("Too many vertices for graph6 file (> 258047)");
            }
            numberOfVertices = (code[1] - 63) * 4096 + (code[2] - 63) * 64 + (code[3] - 63);
            adjacencyStart = 4;
        } else {
            numberOfVertices = code[0] - 63;
            adjacencyStart = 1;
        }
        
        graph.clear();
        for(int i = 0; i < numberOfVertices; i++) graph.addNewVertex(0, 0);

        int index0 = 0;
        int index1 = 1;
        for (int i = adjacencyStart; i < code.length; i++) {
            byte number = (byte) (code[i] - 63);
            for (byte k = 1 << 5; k >= 1; k >>= 1) {
                if ((number & k) > 0) {
                    graph.addNewEdge(index0, index1);
                    // TODO: could abort earlier (but doesn't matter since decoding isn't the bottleneck)
                }
                index0++;
                if (index0 == index1) {
                    index1++;
                    index0 = 0;
                }
            }
        }
    }

    /**
     * Encodes a given graph in graph6 format. Code taken from HoG.
     * 
     * @param graph
     * @return 
     */
    @Override
    public String writeToString(Graph graph) {
        int numberOfVertices = graph.getOrder();
        List<Byte> code = new ArrayList<>();

        // encode number of vertices
        if (numberOfVertices <= 62) {
            code.add((byte) (numberOfVertices + 63));
        } else if (numberOfVertices <= 258047) {
            code.add((byte) 126);
            code.add((byte) (numberOfVertices / 4096 + 63));
            code.add((byte) ((numberOfVertices % 4096) / 64 + 63));
            code.add((byte) (numberOfVertices % 64 + 63));
        } else {
            throw new RuntimeException("Too many vertices: " + numberOfVertices + " (> 258047)");
        }

        // encode adjacencies
        int k = 32;
        byte temp = 0;
        for (int j = 1; j < numberOfVertices; j++) {
            for (int i = 0; i < j; i++) {
                if (graph.areAdjacent(i, j)) {
                    temp |= k;
                }
                k = k >> 1;
                if (k == 0) {
                    temp += 63;
                    code.add(temp);
                    k = 32;
                    temp = 0;
                }
            }
        }
        if (k != 32) {
            temp += 63;
            code.add(temp);
        }

        // convert list of bytes to array
        byte codeArray[] = new byte[code.size()];
        for (int i = 0; i < code.size(); i++) {
            codeArray[i] = code.get(i);
        }
        return new String(codeArray);
    }

    @Override
    public String getFormatName() {
        return "graph6";
    }
    
}
