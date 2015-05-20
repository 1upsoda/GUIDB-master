package database.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import dataView.dataFrame;
import dataView.dataPanel;
import database.model.QueryInfo;
import database.view.DBFrame;
import database.view.DBPanel;

public class DBAppController
{
	/**
	 * the base frame of the GUI
	 */
	private dataFrame baseFrame;
	/**
	 * the base panel of the GUI
	 */
	private dataPanel myAppPanel;
	/**
	 * the database controlling the actuall info of the database
	 */
	private DBController dataController;
	
	private ArrayList<QueryInfo> queryList;
/**
 * currently starts save time instantly so that i can be sure it works, remove to use function better, later
 */
	public DBAppController()
	{

		setDataController(new DBController(this));
		//starts a new list
		queryList = new ArrayList<QueryInfo>();

		dataPanel myAppPanel = (dataPanel) baseFrame.getContentPane();
		
		myAppPanel = new dataPanel(this, "scores");
		
		baseFrame = new dataFrame(this);
		QueryInfo a = new QueryInfo("lol",2);
		QueryInfo b = new QueryInfo("llo",3);
		QueryInfo c = new QueryInfo("olo",780);
		queryList.add(a);
		queryList.add(b);
		queryList.add(c);
		saveTimingInformation();

	}
	
	public void start()
	{
//		DBPanel myAppPanel = (DBPanel) baseFrame.getContentPane();
//		myAppPanel.
////		DBAppController();
//		setDataController(new DBController(this));
//		
//		queryList = new ArrayList<QueryInfo>();
//
//		dataPanel myAppPanel = (dataPanel) baseFrame.getContentPane();
//		
//		myAppPanel = new dataPanel(this, "books");
//		
//		baseFrame = new dataFrame(this);
	}
	
	
	public dataFrame getBaseFrame() 
	{
		return baseFrame;
	}
	public DBController getDataController()
	{
		return dataController;
	}
	public void setBaseFrame(dataFrame baseFrame) 
	{
		this.baseFrame = baseFrame;
	}
	/**
	 * currently able to save text, but whenever it is called it will overwrite EVERYTHING IN THE previous file
	 */
	public void saveTimingInformation()
	{
		File saveFile = new File("save.save");
		if(saveFile.exists())
		{
			saveFile.delete();
		}
		saveFile = new File("save.save");
		
		PrintWriter outputWriter;
		
			try
			{
				outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(saveFile, false)));
				
				for(int spot = 0; spot<queryList.size(); spot++)
				{
					outputWriter.println(queryList.get(spot).getQuery());
					outputWriter.println(queryList.get(spot).getQueryTime());
				}
				outputWriter.close();
			}
			catch(FileNotFoundException noExistingFile)
			{
				JOptionPane.showMessageDialog(null, "There is no file there");
				JOptionPane.showMessageDialog(null, noExistingFile.getMessage());
			}
			catch(IOException inputOutputError)
			{
				JOptionPane.showMessageDialog(null, "There is no file there");
				JOptionPane.showMessageDialog(null, inputOutputError.getMessage());
			}
		

	}
	/**
	 * allows you to pull the info saved on the .save file
	 */
	public void loadTimingInformation()
	{
		try
		{
			File loadFile = new File("save.save");
			if(loadFile.exists())
			{
				queryList.clear();
				Scanner textScanner = new Scanner(loadFile);
				while(textScanner.hasNext())
				{
					String query = textScanner.nextLine();
					long tempLine = Long.parseLong(textScanner.nextLine());
					
					
					queryList.add(new QueryInfo(query, tempLine));
				}
				textScanner.close();
				JOptionPane.showMessageDialog(getBaseFrame(), queryList.size() + " QueryInfo objects were loaded into the application");
			}
			else
			{
				JOptionPane.showMessageDialog(getBaseFrame(), "File not present. No QueryInfo objects loaded");
			}
		}
		catch(IOException currentError)
		{
			dataController.displayErrors(currentError);
		}
	}
	public void setDataController(DBController dataController)
	{
		this.dataController = dataController;
	}
	public ArrayList<QueryInfo> getQueryList()
	{
		return queryList;
	}
	
}
