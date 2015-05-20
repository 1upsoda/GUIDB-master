package database.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.controller.DBAppController;

public class DBPanel extends JPanel
{
	/**
	 * passes the info
	 */
	private DBAppController baseController;
	private SpringLayout baseLayout;
	private JButton queryButton;
	private JScrollPane displayPane;
	private JTextArea displayArea;
	private JTable resultsTable;
	private JPasswordField samplePassword;
	private TableCellWrapRenderer cellRenderer;

	public DBPanel(DBAppController baseController)
	{
		this.baseController = baseController;

		baseLayout = new SpringLayout();
		queryButton = new JButton("Click here to test the query");
		baseLayout.putConstraint(SpringLayout.NORTH, queryButton, 0, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.WEST, queryButton, 99, SpringLayout.WEST, this);

		displayArea = new JTextArea(10, 30);
		displayPane = new JScrollPane(displayArea);
		samplePassword = new JPasswordField(null, 20);
		cellRenderer = new TableCellWrapRenderer();
		baseLayout.putConstraint(SpringLayout.WEST, samplePassword, 10, SpringLayout.WEST, this);

		//setupDisplayPane();
		setupTable();
		setupPanel();
		setupLayout();
		setupListeners();

	}

	private void setupListeners()
	{
		queryButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
				String[] temp = baseController.getDataController().getDatabaseColumnNames("books");
				for (String current : temp)
				{
					displayArea.setText(displayArea.getText() + " Column : " + current + "\n");
				}

			}

		});

	}

	private void setupDisplayPane()
	{
		displayArea.setWrapStyleWord(true);
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
		displayArea.setBackground(Color.PINK);
	}

	private void setupTable()
	{
		DefaultTableModel basicData = new DefaultTableModel(baseController.getDataController().testResults(), baseController.getDataController().getMetaDataTitles());
		resultsTable = new JTable(basicData);
		displayPane = new JScrollPane(resultsTable);
		
		
		for(int spot=0; spot<resultsTable.getColumnCount(); spot++)
		{
			resultsTable.getColumnModel().getColumn(spot).setCellRenderer(cellRenderer);
		}
	}

	private void setupLayout()
	{
		baseLayout.putConstraint(SpringLayout.NORTH, displayPane, 80, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.WEST, displayPane, 80, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.SOUTH, samplePassword, -6, SpringLayout.NORTH, displayPane);
	}

	private void setupPanel()
	{
		this.setBackground(Color.YELLOW);
		this.setSize(1000, 1000);
		this.setLayout(baseLayout);
		this.add(displayPane);
		this.add(queryButton);
		this.add(samplePassword);
		samplePassword.setEchoChar('¥');
		samplePassword.setFont(new Font("Serif", Font.BOLD, 32));
		samplePassword.setForeground(Color.MAGENTA);

	}
}
