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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Main GUI class for TwitBak
 * 
 * @author David @starchy Grant 
 *
 */
public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6395452504541189308L;
	private static final String WARNING = "WARNING: Any existing file will be overwritten.";
	private static final String SELECT = "Select";
	private static final String PASTE = "Paste";
	private static final String COPY = "Copy";
	private static final String CUT = "Cut";
	private static final String BACKUP_BUTTON = "\nBackup Now"; // FIXME - i18n
	private static final String BROWSE_BUTTON = "Browse";
	private static final String BACKUP_FILE_LABEL = "Backup file:";
	private static final String PASSWORD_LABEL = "Password:";
	private static final String USERNAME_LABEL = "Username: ";
	private static final String TITLE = "Twitbak";
	private static final String BROWSE = "browse";
	private static final String BACKUP = "backup";
	
	private JTextField unameField, fileField;
	private JPasswordField passField;
	private String uname, pass, filename;
	private final Document unameDoc, passDoc, fileDoc;
	private static JLabel statusLabel = new JLabel();

	public MainFrame() {
		
		super(TITLE);
		setSize(470, 200);	//FIXME
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		JLabel unameLabel = new JLabel(USERNAME_LABEL);
		gc.anchor = GridBagConstraints.WEST;
		gc.gridy = 0;
		gc.gridx = 0;
		panel.add(unameLabel, gc);

		final JTextField unameField = new JTextField(20);
		
		// Swing's threadsafe way of reading text input (argh)
		unameDoc = unameField.getDocument();
		
		// Maximum username length: 15
		((AbstractDocument) unameDoc).setDocumentFilter(new LengthFilter(15));
		
		// If user hits ENTER on any text field, backup
		unameField.setActionCommand(BACKUP);		
		unameField.addActionListener(this);
		unameField.getDocument().addDocumentListener(new DocumentListener() {
			
			public void changedUpdate(DocumentEvent e) {
				try {
					uname = unameDoc.getText(0,unameField.getDocument().getLength());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}

			public void insertUpdate(DocumentEvent e) {
					try {
						uname = unameDoc.getText(0,unameField.getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}				
			}

			public void removeUpdate(DocumentEvent e) {
				try {
					uname = unameDoc.getText(0,unameField.getDocument().getLength());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}				
			}
			
		});
		gc.gridy = 0;
		gc.gridx = 1;
		panel.add(unameField, gc);

		JLabel passLabel = new JLabel(PASSWORD_LABEL);
		gc.gridy = 1;
		gc.gridx = 0;
		panel.add(passLabel, gc);

		passField = new JPasswordField(20);
		passDoc = passField.getDocument();
		
		// Maximum password length: 20
		((AbstractDocument) passDoc).setDocumentFilter(new LengthFilter(20));

		// If user hits ENTER on any text field, backup
		passField.setActionCommand(BACKUP);
		passField.addActionListener(this);
		passField.getDocument().addDocumentListener(new DocumentListener() {
					
				public void changedUpdate(DocumentEvent e) {
					try {
						pass = passDoc.getText(0,passField.getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
	
				public void insertUpdate(DocumentEvent e) {
					try {
						pass = passDoc.getText(0,passField.getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}				
				}
	
				public void removeUpdate(DocumentEvent e) {
					try {
						pass = passDoc.getText(0,passField.getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}				
				}
				
			});
		gc.gridy = 1;
		gc.gridx = 1;
		panel.add(passField, gc);

		JLabel fileLabel = new JLabel(BACKUP_FILE_LABEL);
		gc.gridy = 2;
		gc.gridx = 0;
		panel.add(fileLabel, gc);

		final JTextField fileField = new JTextField(24);
		fileField.setActionCommand(BACKUP);
		fileField.addActionListener(this);
		fileDoc = fileField.getDocument();
		fileDoc.addDocumentListener(new DocumentListener() {
					
				public void changedUpdate(DocumentEvent e) {
					try {
						filename = fileDoc.getText(0,fileField.getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
	
				public void insertUpdate(DocumentEvent e) {
					try {
						filename = fileDoc.getText(0,fileField.getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}				
				}
	
				public void removeUpdate(DocumentEvent e) {
					try {
						filename = fileDoc.getText(0,fileField.getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}				
				}
				
			});
		gc.gridy = 2;
		gc.gridx = 1;
		panel.add(fileField, gc);

		JButton fileButton = new JButton(BROWSE_BUTTON);
		fileButton.setActionCommand(BROWSE);
		fileButton.addActionListener(this);
		gc.gridy = 2;
		gc.gridx = 2;
		panel.add(fileButton, gc);
		
		JLabel warningLabel = new JLabel(WARNING);
		warningLabel.setForeground(new Color(255, 0, 0));
		gc.gridy = 3;
		gc.gridx = 1;
		panel.add(warningLabel, gc);

		JButton backupButton = new JButton(BACKUP_BUTTON); // FIXME - layout
		backupButton.setActionCommand(BACKUP);
		backupButton.addActionListener(this);
		gc.gridy = 4;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		gc.ipady = 10;
		gc.insets = new Insets(10,0,0,0);
		panel.add(backupButton, gc);
		
		HashMap<Object, Action> jTextFieldPopupActionMap = createActionTable(unameField);
		JPopupMenu jTextFieldPopupMenu = new JPopupMenu();

		unameField.setComponentPopupMenu(jTextFieldPopupMenu);
		passField.setComponentPopupMenu(jTextFieldPopupMenu);
		fileField.setComponentPopupMenu(jTextFieldPopupMenu);

//		Action action = (Action) jTextFieldPopupActionMap
//				.get(DefaultEditorKit.cutAction);
//		action.putValue(Action.NAME, CUT); // FIXME - add keyboard shortcuts
//		unameField.getComponentPopupMenu().add(action);
//
//		action = (Action) jTextFieldPopupActionMap
//				.get(DefaultEditorKit.copyAction);
//		action.putValue(Action.NAME, COPY);
//		unameField.getComponentPopupMenu().add(action);
//
//		action = (Action) jTextFieldPopupActionMap
//				.get(DefaultEditorKit.pasteAction);
//		action.putValue(Action.NAME, PASTE);
//		unameField.getComponentPopupMenu().add(action);

		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

		add(panel,BorderLayout.WEST);
		
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(statusLabel,BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(backupButton);
	}

	private HashMap<Object, Action> createActionTable(
			JTextComponent textComponent) {
		HashMap<Object, Action> actions = new HashMap<Object, Action>();
		Action[] actionsArray = textComponent.getActions();
		for (int i = 0; i < actionsArray.length; i++) {
			Action a = actionsArray[i];
			actions.put(a.getValue(Action.NAME), a);
		}
		return actions;
	}
	
	public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        
        if (BACKUP.equals(cmd)) { 
        	System.out.println("Attempting to backup...");
        	
        	SwingUtilities.invokeLater(new Runnable() {
    			public void run() {
    				
    				// Driver - backup without opening status window
//    				BackupWorker worker = new BackupWorker(uname, pass, filename);
//    				worker.execute();
//    				
//    				try {
//    					worker.get();
//    				} catch (InterruptedException e) {
//    					e.printStackTrace();
//    				} catch (ExecutionException e) {
//    					e.printStackTrace();
//    				}
    				
    				//Open status window and run the actual backup
    				final StatusFrame statusFrame = new StatusFrame();
    				statusFrame.setVisible(true);
    				statusFrame.beginBackup(uname,pass,filename);
    			}
    		});
        	
        }
        
        // Open a file browser to choose output location
        //TODO - warn user that existing file will be overwritten
        if (BROWSE.equals(cmd)) {
        	final JFileChooser fileChooser = new JFileChooser();
        	if (fileChooser.showDialog(this,SELECT) == JFileChooser.APPROVE_OPTION) {
        		try {
					fileDoc.remove(0,fileDoc.getLength());
					fileDoc.insertString(0, fileChooser.getSelectedFile().getPath(), null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
        	}
        }
        
    }
	
}