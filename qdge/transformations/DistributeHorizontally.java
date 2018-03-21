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

package qdge.transformations;

import java.util.List;
import java.util.stream.Collectors;
import qdge.data.Graph;
import qdge.data.GraphSelectionModel;
import qdge.data.Vertex;

/**
 * Distributes the selected vertices horizontally between the leftmost and the
 * rightmost selected vertex.
 * 
 * @author nvcleemp
 */
public class DistributeHorizontally extends AbstractVertexBasedGraphTransformation {
    
    public DistributeHorizontally(GraphSelectionModel selectionModel) {
        super(selectionModel::isSelected);
    }

    @Override
    public GraphTransformation repeatTransformation(Graph g) {
        final FixedRepositioningGraphTransformation repeat = new FixedRepositioningGraphTransformation();
        List<Vertex> vs = g.vertices().filter(filter).sorted((v1, v2) -> (int)Math.signum(v1.getX() - v2.getX())).collect(Collectors.toList());
        if(vs.size() > 2){
            float min = vs.get(0).getX();
            float max = vs.get(vs.size()-1).getX();
            int size = vs.size();
            int pos = 0;
            for (Vertex v : vs) {
                repeat.setPosition(v, min + pos*(max-min)/(size-1), v.getY());
                pos++;
            }
        }
        return repeat;
    }

    @Override
    public GraphTransformation inverseTransformation(Graph g) {
        return new FixedRepositioningGraphTransformation()
                .recordPositions(
                        g.vertices()
                                .filter(filter)
                                .collect(Collectors.toList()));
    }

    @Override
    public void transformGraph(Graph g) {
        List<Vertex> vs = g.vertices().filter(filter).sorted((v1, v2) -> (int)Math.signum(v1.getX() - v2.getX())).collect(Collectors.toList());
        if(vs.size() <= 2) return;
        float min = vs.get(0).getX();
        float max = vs.get(vs.size()-1).getX();
        int size = vs.size();
        int pos = 0;
        for (Vertex v : vs) {
            v.setXY(min + pos*(max-min)/(size-1), v.getY());
            pos++;
        }
    }
    
}
