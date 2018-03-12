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

package qdge.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 * @param <E>
 */
public class SimpleSingleSelectionModel<E> {
    
    private E selection = null;
    
    public boolean isSelected(E e){
        return e != null && e.equals(selection);
    }

    public E getSelection() {
        return selection;
    }
    
    public void select(E e){
        if(isSelected(e)) return;
        E old = selection;
        selection = e;
        fireSelectionChanged(old, e);
    }
    
    private final List<SimpleSingleSelectionListener<E>> listeners = new ArrayList<>();
    
    public void addListener(SimpleSingleSelectionListener<E> l){
        listeners.add(l);
    }
    
    public void removeListener(SimpleSingleSelectionListener<E> l){
        listeners.remove(l);
    }
    
    private void fireSelectionChanged(E oldSelection, E newSelection){
        for (SimpleSingleSelectionListener<E> l : listeners) {
            l.selectionChanged(oldSelection, newSelection);
        }
    }
}