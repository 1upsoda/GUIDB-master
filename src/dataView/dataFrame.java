package dataView;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SpringLayout;





import database.controller.DBAppController;
import database.controller.DBController;

public class dataFrame extends JFrame
{
	private dataPanel basePanel;
	private DBAppController baseController;
	/**
	 * puts the panel in the frame for the GUI
	 * @param dbAppController
	 */
		public dataFrame(DBAppController baseController)

		{

			basePanel = new dataPanel(baseController, "scores");
			this.baseController = baseController;

			setupFrame();
			setupListeners();

		}
	
	private void setupListeners()
	{
		addWindowListener(new WindowListener()
		{

			@Override
			public void windowActivated(WindowEvent arg0)
			{
				
				
			}

			@Override
			public void windowClosed(WindowEvent arg0)
			{
				
			}

			
			/**
			 * instantly saves the info when you close the window
			 */
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				baseController.saveTimingInformation();
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0)
			{
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0)
			{
				
			}

			@Override
			public void windowIconified(WindowEvent arg0)
			{
				
			}

			@Override
			public void windowOpened(WindowEvent arg0)
			{
				
			}
			
		});
	}
	public dataFrame(DBController dataController)
	{
		
	}
	/**
	 * builds the frame of the window that holds the gui panel
	 */
		private void setupFrame()

		{

			setContentPane(basePanel);

			setLocation(5, 6);

			setSize(500, 500);

			setResizable(true);

			setVisible(true);
			
			

		}
	}
