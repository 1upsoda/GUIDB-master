package dataView;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;




import database.controller.DBAppController;
import database.controller.DBController;

public class dataPanel extends JPanel
{

	private DBAppController baseController;
	private JButton queryButton, saveButton, loadButton;
	private SpringLayout baseLayout;
	private String table;
	private ArrayList<JTextField> fieldList;

	public dataPanel(DBAppController baseController, String table)
	{
		this.baseController = baseController;
		this.table = table;
		baseLayout = new SpringLayout();
		queryButton = new JButton("Show Query");
		saveButton = new JButton("Save");
		baseLayout.putConstraint(SpringLayout.WEST, saveButton, 95, SpringLayout.WEST, this);
		loadButton = new JButton("Load");
		baseLayout.putConstraint(SpringLayout.NORTH, saveButton, 0, SpringLayout.NORTH, loadButton);
		baseLayout.putConstraint(SpringLayout.NORTH, loadButton, 0, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.EAST, loadButton, -78, SpringLayout.EAST, this);
		fieldList = new ArrayList<JTextField>();
		
		setupPanel(table);
		setupListeners();
		setupLayout();
	}

	private void setupLayout()
	{
		baseLayout.putConstraint(SpringLayout.WEST, queryButton, 139, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.SOUTH, queryButton, -10, SpringLayout.SOUTH, this);
	}

	
	private String getFields()
	{
		String fields = "(";
		
		for(int spot = 0; spot < fieldList.size();spot++)
		{
			fields += "`"+fieldList.get(spot).getName()+"`";
			if(spot==fieldList.size()-1)
			{
				fields+=")";
			}
			else
			{
				fields+=", ";
			}
		}
		
		return fields;
	}
	private String getValues()
	{
		String values = "(";
		
		for(int spot = 0; spot < fieldList.size();spot++)
		{
			values += "'"+fieldList.get(spot).getText()+"'";
			if(spot==fieldList.size()-1)
			{
				values+=");";
			}
			else
			{
				values+=", ";
			}
		}
		
		return values;
	}
	private void setupListeners()
	{
//		ArrayList<JTextField> myTextFields = new ArrayList<JTextField>();
//		for(Component current : this.getComponents())
//		{
//			if(current instanceof JTextField)
//			{
//				myTextFields.add((JTextField)current);
//			}
//		}
//		
		
		queryButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
				String query = "INSERT INTO " + "`" + table + "` " + getFields() + " VALUES " + getValues();
				baseController.getDataController().submitUpdateQuery(query);
				baseController.loadTimingInformation();
			}
			
		});
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
				String chat = "";
				String selectedTable = "books";
				String [] columns = baseController.getDataController().getDatabaseColumnNames(selectedTable);
				for(int count = 0; count<columns.length; count++)
				{
					if(!columns[count].equalsIgnoreCase("id"))
					{
					chat += columns[count];
					}
				}
				saveText(chat, false);
			}
		});
		
		loadButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
				String savedChat = readTextFromFile();
				if(savedChat.length()<1)
				{
					JOptionPane.showMessageDialog(null, "no text in file");
				}
				else
				{
					JOptionPane.showMessageDialog(null, savedChat);
				}
				
			}
		});
	}
	public String readTextFromFile()
	{
		String fileText = "";
		String filePath = "C:/Users/tpar4829/Documents/";
		String fileName = filePath + "saved text.txt";
		File inputFile = new File(fileName);
		
		try
		{
			Scanner fileScanner = new Scanner(inputFile);
			
			while(fileScanner.hasNext())
			{
				fileText += fileScanner.next() + "\n";
			}
			
			fileScanner.close();
		}
		catch(FileNotFoundException fileException)
		{
			JOptionPane.showMessageDialog(null, "The file is not here");
		}
		
		return fileText;
	}
	
	public void saveText(String conversation, boolean appendToEnd)
	{
		String fileName = "/Users/tpar4829/Documents/saved text.txt";
		
		PrintWriter outputWriter;
		
		if(appendToEnd)
		{
			try
			{
				outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName, appendToEnd)));
				outputWriter.append(conversation);
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
		else
		{
			try
			{
				outputWriter = new PrintWriter(fileName);
				outputWriter.println(conversation);
				outputWriter.close();
			}
			catch(FileNotFoundException noFileIsThere)
			{
				JOptionPane.showMessageDialog(null, "Thereis no file there");
			}
		}
	}
	private void setupPanel(String selectedTable)
	{
		
		this.setLayout(baseLayout);
		this.add(queryButton);
//		this.add(saveButton);
//		this.add(loadButton);
		
		this.setSize(400, 600);
		this.setBackground(Color.ORANGE);
		
		String [] columns = baseController.getDataController().getDatabaseColumnNames(selectedTable);
		int spacing = 50;
		for(int count = 0; count<columns.length; count++)
		{
			if(!columns[count].equalsIgnoreCase("id"))
			{
				JLabel columnLabel = new JLabel(columns[count]);
				JTextField columnField = new JTextField(20);
				columnField.setName(columns[count]);
				fieldList.add(columnField);
				
				this.add(columnLabel);
				this.add(columnField);
				baseLayout.putConstraint(SpringLayout.WEST, columnLabel, 20, SpringLayout.WEST, this);
				baseLayout.putConstraint(SpringLayout.WEST, columnField, 60, SpringLayout.WEST, columnLabel);
				
				baseLayout.putConstraint(SpringLayout.NORTH, columnLabel, spacing, SpringLayout.NORTH, this);
				baseLayout.putConstraint(SpringLayout.NORTH, columnField, spacing, SpringLayout.NORTH, this);
				
				spacing += 50;
			}
			
		}
	}

}
