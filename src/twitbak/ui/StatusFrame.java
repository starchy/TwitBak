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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXBusyLabel;

import twitbak.FullBackup;
import twitter4j.TwitterException;

/**
 * Popup JFrame including status text and a cancel button.
 * Kicks off the worker thread that runs the actual backup logic.
 * @author David @starchy Grant
 */
public class StatusFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String TITLE = "TwitBak Progress";
	private static final String CANCEL_BUTTON = "Cancel";
	private static final String INITIAL_STATUS = "Backing up...";
	private static JButton cancelButton;
	private static final JXBusyLabel busyLabel = new JXBusyLabel();
	static final StatusBar statusBar = new StatusBar();
	static int statusChangeFlag = 0;
	
	StatusFrame() {
		super(TITLE);
		setSize(370,80);
		
		setLayout(new BorderLayout());
		
		add(statusBar,BorderLayout.NORTH);
		statusBar.setMessage(INITIAL_STATUS);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		cancelButton = new JButton(CANCEL_BUTTON);
		panel.add(cancelButton,BorderLayout.WEST);
		
		panel.add(busyLabel,BorderLayout.EAST);
//		busyLabel.setBusy(true);

		add(panel,BorderLayout.SOUTH);
		
	}

	@Override
	
	public void actionPerformed(ActionEvent event) {
	}
	
	public void beginBackup(final String uname, final String pass, final String filename) {
		
		busyLabel.setBusy(true);
		final BakWorker worker = new BakWorker(uname,pass,filename);
		worker.execute();
		
		//FIXME - not actually resizing
		if (!worker.isDone()) {
			try {
				if (statusChangeFlag == 1) {
					statusChangeFlag = 0;
					pack();
				}
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			pack();
		}
		
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Canceling backup...");
				worker.cancel(true);
				dispose();
			}});
	}
	
	/**
	 * Worker thread for the long-running backup task.
	 * @author david
	 *
	 */
	public static class BakWorker extends SwingWorker<String,String> {

		private static String uname = null;
		private static String pass = null;
		private static String filename = null;
		static String status = null;
		
		BakWorker (String uname, String pass, String filename) {
			this.uname = uname;
			this.pass = pass;
			this.filename = filename;
		}
		
		@Override
		protected String doInBackground() throws Exception {
		try {
				publish("Working...");
				FullBackup fullBak = new FullBackup(uname,pass,filename);
			} 
		
		catch (TwitterException e) {
				if (e != null) {
					String exception = e.toString();
					
					if (exception.contains("statusCode=-1")) {
						exception = "Unable to contact Twitter. Check your network connection.";
					}
					else if (exception.contains("statusCode=401")) {
						exception = "Authorization failed (bad username or password).";
					}
					else if (exception.contains("statusCode=502") || exception.contains("statusCode=503")) {
						exception = "Fail Whale! Twitter is being lame; please try again shortly.";
					}
					
					setStatus(exception);
//					statusBar.setText(exception);
					busyLabel.setBusy(false);
					e.printStackTrace();
					return (exception);
				}
			} 
		
		catch (IOException e) {
				if (e != null) {
					statusBar.setText(e.toString());
					busyLabel.setBusy(false);
					e.printStackTrace();
					return (e.toString());
				}
			} 
		
		catch (InterruptedException e) {
				if (e != null) {
					statusBar.setText(e.toString());
					busyLabel.setBusy(false);
					e.printStackTrace();
					return (e.toString());
				}
			} 
			statusBar.setText("Done!");
			busyLabel.setBusy(false);
			return "Done!";
		}
		
		// We only care about displaying the latest status
		protected void process (List<String> statuses) {
            setStatus(statuses.get(statuses.size() -1));
		}
		
		String getStatus () { return statusBar.getText(); }
		
		// Kinda lame having this public static, but it works for now
		public static void setStatus (String str) {	statusBar.setText(str); statusChangeFlag = 1;}
		
	}
	


}
