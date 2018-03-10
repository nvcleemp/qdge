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

import qdge.data.Graph;

/**
 * Class which handles I/O for the LaTeX format.
 * 
 * @author nvcleemp
 */
public class LatexWriter implements FileGraphWriter, StringGraphWriter {
    
    private final TikzWriter tikzWriter = new TikzWriter();

    /**
     * Encodes a given graph in LaTeX format.
     * 
     * @param graph
     * @return 
     */
    @Override
    public String writeToString(Graph graph) {
        StringBuilder sb = new StringBuilder("\\documentclass[tikz]{standalone}\n");
        sb.append("\\begin{document}\n");
        sb.append(tikzWriter.writeToString(graph));
        sb.append("\\end{document}\n");
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
        return "LaTeX";
    }
    
}
