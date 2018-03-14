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

package qdge.gui.undo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author nvcleemp
 */
public class HistoryModel {
    
    private final Stack<HistoryItem> history = new Stack<>();
    private final Stack<HistoryItem> future = new Stack<>();
    private final List<HistoryListener> listeners = new ArrayList<>();
    
    public void push(HistoryItem item){
        if(!future.empty()){
            future.clear();
        }
        history.push(item);
        fireHistoryChanged();
    }
    
    public void undo(){
        if(history.isEmpty())
            return;
        
        HistoryItem item = history.pop();
        item.undo();
        future.push(item);
        fireHistoryChanged();
    }
    
    public void redo(){
        if(future.isEmpty())
            return;
        
        HistoryItem item = future.pop();
        item.redo();
        history.push(item);
        fireHistoryChanged();
    }
    
    public void clear(){
        history.clear();
        future.clear();
        fireHistoryChanged();
    }
    
    public void addListener(HistoryListener l){
        listeners.add(l);
    }
    
    public void removeListener(HistoryListener l){
        listeners.remove(l);
    }
    
    private void fireHistoryChanged(){
        final HistoryItem topHistory = history.isEmpty() ? null : history.peek();
        final HistoryItem topFuture = future.isEmpty() ? null : future.peek();
        listeners.parallelStream().forEach(l -> l.historyChanged(topHistory, topFuture));
    }
}
