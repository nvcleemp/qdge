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

package qdge;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import qdge.data.Graph;
import qdge.data.GraphSelectionModel;
import qdge.data.Vertex;
import qdge.gui.GraphPanel;
import qdge.gui.GraphPanelMouseListener;
import qdge.gui.actions.ExportStringAction;
import qdge.gui.actions.InfoAction;
import qdge.gui.actions.LoadFileAction;
import qdge.gui.actions.LoadStringAction;
import qdge.gui.actions.SaveFileAction;
import qdge.gui.actions.ZoomAction;
import qdge.gui.actions.transformation.CustomAngleRotateAction;
import qdge.gui.actions.transformation.TransformationWithReferenceVertexAction;
import qdge.gui.actions.transformation.TransformationAction;
import qdge.gui.actions.transformation.VertexSelectionTransformationAction;
import qdge.gui.dialogs.ReferenceVertexSelectionDialog;
import qdge.gui.editormode.EditorMode;
import qdge.gui.undo.HistoryModel;
import qdge.io.Graph6Handler;
import qdge.io.LatexWriter;
import qdge.io.TikzWriter;
import qdge.io.WriteGraph2DHandler;
import qdge.transformations.AlignXCoordinates;
import qdge.transformations.AlignYCoordinates;
import qdge.transformations.CenterBoundingBox;
import qdge.transformations.CenterGravitationalCenter;
import qdge.transformations.DistributeHorizontally;
import qdge.transformations.DistributeVertically;
import qdge.transformations.FlipVertically;
import qdge.transformations.Rotation;
import qdge.transformations.Scale;
import qdge.transformations.Shift;
import qdge.transformations.FlipHorizontally;
import qdge.transformations.SelectionShift;
import qdge.util.SimpleSingleSelectionModel;

/**
 * A quick & dirty graph editor.
 * @author nvcleemp
 */
public class QDGraphEditor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("QD Graph Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        final HistoryModel history = new HistoryModel();
        final GraphSelectionModel selectionModel = new GraphSelectionModel();
        
        final Graph graph = new Graph();
        final GraphPanel panel = new GraphPanel(graph, selectionModel);
        final SimpleSingleSelectionModel<EditorMode> editorModeModel =
                new SimpleSingleSelectionModel<>();
        final EditorMode createMode = EditorMode.createMode(graph, panel, history, selectionModel);
        final EditorMode layoutMode = EditorMode.layoutMode(graph, panel, history, selectionModel);
        final EditorMode editMode = EditorMode.editMode(graph, panel, history, selectionModel);
        final GraphPanelMouseListener l = new GraphPanelMouseListener(createMode, panel);
        editorModeModel.select(createMode);
        graph.addInfoListener(() -> {
            if(graph.isStructureEditable()){
                editorModeModel.select(createMode);
            } else {
                editorModeModel.select(layoutMode);
            }
        });
        editorModeModel.addListener((o, n) -> l.setEditorMode(n));
        panel.addMouseListener(l);
        panel.addMouseMotionListener(l);

        
        frame.add(panel, BorderLayout.CENTER);
        
        JMenu mFile = new JMenu("File");
        mFile.add(new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.clear();
                history.clear();
                selectionModel.clear();
            }
        });
        JMenu mSave = new JMenu("Save");
        mSave.add(new SaveFileAction(graph, new WriteGraph2DHandler()));
        mSave.add(new SaveFileAction(graph, new TikzWriter()));
        mSave.add(new SaveFileAction(graph, new LatexWriter()));
        mFile.add(mSave);
        JMenu mExport = new JMenu("Export");
        mExport.add(new ExportStringAction(graph, new Graph6Handler()));
        mExport.add(new ExportStringAction(graph, new WriteGraph2DHandler()));
        mExport.add(new ExportStringAction(graph, new TikzWriter()));
        mExport.add(new ExportStringAction(graph, new LatexWriter()));
        mFile.add(mExport);
        JMenu mLoad = new JMenu("Load");
        JMenu mLoadFile = new JMenu("Load from file");
        mLoadFile.add(new LoadFileAction(graph, new WriteGraph2DHandler(), history, selectionModel));
        mLoad.add(mLoadFile);
        JMenu mLoadString = new JMenu("Load from string");
        mLoadString.add(new LoadStringAction(graph, new Graph6Handler(), history, selectionModel));
        mLoad.add(mLoadString);
        mFile.add(mLoad);
        
        JMenu mEdit = new JMenu("Edit");
        final JMenu mMode = new JMenu("Editor mode");
        final EditorMode[] editorModes = {layoutMode, createMode, editMode};
        final String[] modeNames = {"Layout mode", "Create mode", "Edit mode"};
        for (int i = 0; i < editorModes.length; i++) {
            final EditorMode m = editorModes[i];
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(
                    modeNames[i], editorModeModel.isSelected(m));
            item.addChangeListener(e -> {
                if(item.isSelected())
                    editorModeModel.select(m);
            });
            item.setAccelerator(KeyStroke.getKeyStroke((char)('1' + i), InputEvent.CTRL_DOWN_MASK));
            editorModeModel.addListener((o,n) -> item.setSelected(m.equals(n)));
            mMode.add(item);
        }
        mMode.addSeparator();
        mMode.add(new InfoAction(QDGraphEditorHelp.EDITOR_MODES));
        mEdit.add(mMode);
        mEdit.addSeparator();
        final JMenuItem undoItem = new JMenuItem(new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                history.undo();
            }
        });
        undoItem.setEnabled(false);
        final JMenuItem redoItem = new JMenuItem(new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                history.redo();
            }
        });
        redoItem.setEnabled(false);
        history.addListener((u,r) -> {
            undoItem.setEnabled(u!=null);
            undoItem.setText(u==null ? "Undo" : "Undo: " + u.getDescription());
            redoItem.setEnabled(r!=null);
            redoItem.setText(r==null ? "Redo" : "Redo: " + r.getDescription());
        });
        mEdit.add(undoItem).setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        mEdit.add(redoItem).setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
        
        JMenu mSelection = new JMenu("Selection");
        mSelection.add(new AbstractAction("Clear selection") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionModel.clear();
            }
        }).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        mSelection.add(new AbstractAction("Select all vertices") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionModel.selectAllVertices(graph);
            }
        }).setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        mSelection.add(new AbstractAction("Select all edges") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionModel.selectAllEdges(graph);
            }
        }).setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_DOWN_MASK));
        mSelection.add(new AbstractAction("Invert vertex selection") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionModel.invertVertexSelection(graph);
            }
        }).setAccelerator(KeyStroke.getKeyStroke('!'));
        mSelection.add(new AbstractAction("Invert edge selection") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionModel.invertEdgeSelection(graph);
            }
        }).setAccelerator(KeyStroke.getKeyStroke('|'));

        
        JMenu mCenter = new JMenu("Center");
        mCenter.add(new TransformationAction("Center bounding box", new CenterBoundingBox(), graph, history)).setAccelerator(KeyStroke.getKeyStroke('B'));
        mCenter.add(new TransformationAction("Center gravitational center", new CenterGravitationalCenter(), graph, history)).setAccelerator(KeyStroke.getKeyStroke('G'));
        
        JMenu mTransform = new JMenu("Transform");
        mTransform.add(new CustomAngleRotateAction("Rotate...", graph, history));
        mTransform.add(new TransformationAction("Rotate right", new Rotation(-90), graph, history)).setAccelerator(KeyStroke.getKeyStroke('r'));
        mTransform.add(new TransformationAction("Rotate left", new Rotation(90), graph, history)).setAccelerator(KeyStroke.getKeyStroke('l'));
        mTransform.add(new TransformationAction("Flip horizontally", new FlipHorizontally(), graph, history)).setAccelerator(KeyStroke.getKeyStroke('H'));
        mTransform.add(new TransformationAction("Flip vertically", new FlipVertically(), graph, history)).setAccelerator(KeyStroke.getKeyStroke('V'));
        mTransform.addSeparator();
        mTransform.add(mCenter);
        mTransform.add(new TransformationAction("Shift up", new Shift(0, -1), graph, history)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        mTransform.add(new TransformationAction("Shift down", new Shift(0, 1), graph, history)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        mTransform.add(new TransformationAction("Shift right", new Shift(1, 0), graph, history)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        mTransform.add(new TransformationAction("Shift left", new Shift(-1, 0), graph, history)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        mTransform.add(new VertexSelectionTransformationAction("Shift selection up", new SelectionShift(selectionModel, 0, -1), graph, history, selectionModel)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK));
        mTransform.add(new VertexSelectionTransformationAction("Shift selection down", new SelectionShift(selectionModel, 0, 1), graph, history, selectionModel)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK));
        mTransform.add(new VertexSelectionTransformationAction("Shift selection left", new SelectionShift(selectionModel, -1, 0), graph, history, selectionModel)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK));
        mTransform.add(new VertexSelectionTransformationAction("Shift selection right", new SelectionShift(selectionModel, 1, 0), graph, history, selectionModel)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK));
        mTransform.addSeparator();
        mTransform.add(new TransformationAction("Scale up", new Scale(2), graph, history)).setAccelerator(KeyStroke.getKeyStroke('S'));
        mTransform.add(new TransformationAction("Scale down", new Scale(.5f), graph, history)).setAccelerator(KeyStroke.getKeyStroke('s'));
        mTransform.addSeparator();
        mTransform.add(new TransformationWithReferenceVertexAction("Align X coordinates", new AlignXCoordinates(selectionModel), graph, history, selectionModel, 
                ReferenceVertexSelectionDialog.FIRST_SELECTED,
                ReferenceVertexSelectionDialog.LAST_SELECTED,
                new ReferenceVertexSelectionDialog.ReferenceVertexSelectionItem(
                        "Leftmost vertex", 
                        (g,sm) -> sm.getOrderedSelectedVerticesStream()
                                .parallel()
                                .reduce((u,v) -> u.getX() < v.getX() ? u : v)
                                .orElse(null)
                ),
                new ReferenceVertexSelectionDialog.ReferenceVertexSelectionItem(
                        "Rightmost vertex", 
                        (g,sm) -> sm.getOrderedSelectedVerticesStream()
                                .parallel()
                                .reduce((u,v) -> u.getX() > v.getX() ? u : v)
                                .orElse(null)
                ),
                new ReferenceVertexSelectionDialog.ReferenceVertexSelectionItem(
                        "Average", 
                        (g,sm) -> new Vertex( 
                                (float) sm.getOrderedSelectedVerticesStream()
                                        .parallel()
                                        .mapToDouble(Vertex::getX)
                                        .average().getAsDouble(),
                        0)
                )
        )).setAccelerator(KeyStroke.getKeyStroke('x'));
        mTransform.add(new TransformationWithReferenceVertexAction("Align Y coordinates", new AlignYCoordinates(selectionModel), graph, history, selectionModel, 
                ReferenceVertexSelectionDialog.FIRST_SELECTED,
                ReferenceVertexSelectionDialog.LAST_SELECTED,
                new ReferenceVertexSelectionDialog.ReferenceVertexSelectionItem(
                        "Highest vertex", 
                        (g,sm) -> sm.getOrderedSelectedVerticesStream()
                                .parallel()
                                .reduce((u,v) -> u.getY() < v.getY() ? u : v)
                                .orElse(null)
                ),
                new ReferenceVertexSelectionDialog.ReferenceVertexSelectionItem(
                        "Lowest vertex", 
                        (g,sm) -> sm.getOrderedSelectedVerticesStream()
                                .parallel()
                                .reduce((u,v) -> u.getY() > v.getY() ? u : v)
                                .orElse(null)
                ),
                new ReferenceVertexSelectionDialog.ReferenceVertexSelectionItem(
                        "Average", 
                        (g,sm) -> new Vertex(0, 
                                (float) sm.getOrderedSelectedVerticesStream()
                                        .parallel()
                                        .mapToDouble(Vertex::getY)
                                        .average().getAsDouble())
                )
        )).setAccelerator(KeyStroke.getKeyStroke('y'));
        mTransform.add(new VertexSelectionTransformationAction("Distribute horizontally", new DistributeHorizontally(selectionModel), graph, history, selectionModel))
                .setAccelerator(KeyStroke.getKeyStroke('h'));
        mTransform.add(new VertexSelectionTransformationAction("Distribute vertically", new DistributeVertically(selectionModel), graph, history, selectionModel))
                .setAccelerator(KeyStroke.getKeyStroke('v'));
        
        JMenu mZoom = new JMenu("Zoom");
        mZoom.add(new ZoomAction(true, panel)).setAccelerator(KeyStroke.getKeyStroke('+'));
        mZoom.add(new ZoomAction(false, panel)).setAccelerator(KeyStroke.getKeyStroke('-'));
        
        JMenu mView = new JMenu("View");
        mView.add(new AbstractAction("Toggle grid") {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setPaintGrid(!panel.isPaintGrid());
            }
        }).setAccelerator(KeyStroke.getKeyStroke('g'));
        mView.add(mZoom);
        
        JMenu mHelp = new JMenu("Help");
        mHelp.add(new InfoAction("Help", QDGraphEditorHelp.HELP)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        
        JMenuBar mb = new JMenuBar();
        mb.add(mFile);
        mb.add(mEdit);
        mb.add(mSelection);
        mb.add(mTransform);
        mb.add(mView);
        mb.add(mHelp);
        
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), "downdown");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), "upup");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK), "rightright");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK), "leftleft");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK), "seldowndown");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK), "selupup");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK), "selrightright");
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK), "selleftleft");
        panel.getActionMap().put("downdown", new TransformationAction("Move down", new Shift(0, 10), graph, history));
        panel.getActionMap().put("upup", new TransformationAction("Move up", new Shift(0, -10), graph, history));
        panel.getActionMap().put("rightright", new TransformationAction("Move right", new Shift(10, 0), graph, history));
        panel.getActionMap().put("leftleft", new TransformationAction("Move left", new Shift(-10, 0), graph, history));
        panel.getActionMap().put("seldowndown", new VertexSelectionTransformationAction("Shift selection down", new SelectionShift(selectionModel, 0, 10), graph, history, selectionModel));
        panel.getActionMap().put("selupup", new VertexSelectionTransformationAction("Shift selection up", new SelectionShift(selectionModel, 0, -10), graph, history, selectionModel));
        panel.getActionMap().put("selrightright", new VertexSelectionTransformationAction("Shift selection right", new SelectionShift(selectionModel, 10, 0), graph, history, selectionModel));
        panel.getActionMap().put("selleftleft", new VertexSelectionTransformationAction("Shift selection right", new SelectionShift(selectionModel, -10, 0), graph, history, selectionModel));
        
        panel.addMouseWheelListener(e -> {
            if(e.getWheelRotation()<0){
                panel.decrementZoom();
            } else {
                panel.incrementZoom();
            }
        });
        
        frame.setJMenuBar(mb);
        
        frame.pack();
        frame.setVisible(true);
    }
    
}
