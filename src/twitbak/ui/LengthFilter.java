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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

/**
 * Filters only on input length. Used for input validation on username and 
 * password fields.
 * 
 * @author David @starchy Grant
 */
public class LengthFilter extends DocumentFilter {
	
	// Maximum field size
    private int length;
    
    /**
     * 
     * @param length - maximum field size
     */
    public LengthFilter(int length) {
        this.length = length;
    }
    
    /**
     * Limits field size to this.length when inserting text
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String text,
            AttributeSet attrs) throws
            BadLocationException {
        if (this.length > 0 && (fb.getDocument().getLength() + text.length() > (this.length))) {
            return;
        }
        super.insertString(fb, offset, text, attrs);
    }
    
    /**
     * Limits field size to this.length when replacing text
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws
            BadLocationException {
        if (this.length > 0 && (fb.getDocument().getLength() + text.length() - length > (this.length))) {
            return;
        }
        super.replace(fb, offset, length,
                text, attrs);
    }
}

