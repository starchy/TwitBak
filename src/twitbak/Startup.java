package twitbak;

import java.io.IOException;

import javax.swing.SwingUtilities;

import twitbak.ui.MainFrame;
import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONException;

//TODO - open file on completed backup
//TODO - write what we got on exception (eg 502)
//TODO - status window still getting initially setup, etc
//TODO - SSL
//TODO - swap out JSON file with database (SQLite?) - location should be OS dependent
//TODO - incremental backup
//TODO - OAuth
//TODO - de-uglify UI
//TODO - prettify UI
//TODO - search function
//TODO - export backup to file (csv, etc)
//TODO - organize DMs by sender
//TODO - backup geolocation
//TODO - retweets

/**
 * Entry point class for TwitBak.  Creates a new MainFrame on an
 * event dispatch thread, which creates the UI and launches the program 
 * logic in FullBackup on a worker thread.
 * 
 * @author David @starchy Grant
 *
 */
public class Startup {
	
	static MainFrame mf;
	
	public static void main (String[] args) throws TwitterException, InterruptedException, IOException, JSONException {
		
		//GUI creation must be handled on Swing's event dispatch thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame mf = new MainFrame();
				mf.setVisible(true);
			}
		});
		
		//Test driver - backup without GUI
//		String uname=args[0];
//		String pass=args[1];
//		String filename=args[2];
//		FullBackup fullBak = new FullBackup(uname,pass,filename);
	}
}
