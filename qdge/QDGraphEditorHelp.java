/*
 * Copyright (C) 2018 Nico Van Cleemput <nico.vancleemput@gmail.com>
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

/**
 * Class which groups several help messages.
 * @author nvcleemp
 */
class QDGraphEditorHelp {
    static final String EDITOR_MODES = "<html><body>"
            + "<h1>Editor modes</h1>"
            + "<h2>Layout mode</h2>"
            + "The structure of the graph cannot be changed."
            + "<ul>"
            + "<li>Move vertices: drag them</li>"
            + "</ul>"
            + "<h2>Create mode</h2>"
            + "The structure of the graph can be changed by adding vertices or "
            + "edges."
            + "<ul>"
            + "<li>Move vertices: drag them</li>"
            + "<li>Add vertex: double-click</li>"
            + "<li>Add edge: drag from a vertex while holding down SHIFT</li>"
            + "</ul>"
            + "<h2>Edit mode</h2>"
            + "The structure of the graph can be changed by adding and/or "
            + "removing vertices or edges."
            + "<ul>"
            + "<li>Move vertices: drag them</li>"
            + "<li>Add vertex: double-click</li>"
            + "<li>Add edge: drag from a vertex while holding down SHIFT</li>"
            + "<li>Delete vertex or edge: click them while holding down CTRL</li>"
            + "</ul>"
            + "</body></html>";
}
