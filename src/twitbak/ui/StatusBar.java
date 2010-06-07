/*
 * Copyright 2010 David "Starchy" Grant
 *
 * This file is part of TwitBak.
 * 
 * TwitBak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package twitbak.ui;

import java.awt.Dimension;

import javax.swing.JLabel;

public class StatusBar extends JLabel {
    
    /** Creates a new instance of StatusBar 
     * Thanks to Java Tips: 
     * http://www.java-tips.org/java-se-tips/javax.swing/creating-a-status-bar.html */
	
    public StatusBar() {
        super();
        super.setPreferredSize(new Dimension(100, 16));
        setMessage(" ");
    }
    
    public void setMessage(String message) {
        setText(" "+message);        
    }        
}