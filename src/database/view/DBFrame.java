package database.view;

import javax.swing.JFrame;

import database.controller.DBAppController;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

// Referenced classes of package string.view:

//            ChatbotPanel

public class DBFrame extends JFrame

{

	private DBPanel basePanel;
/**
 * puts the panel in the frame for the GUI
 * @param baseController
 */
	public DBFrame(DBAppController baseController)

	{

		basePanel = new DBPanel(baseController);
		SpringLayout springLayout = (SpringLayout) basePanel.getLayout();

		setupFrame();

	}
/**
 * builds the frame of the window that holds the gui panel
 */
	private void setupFrame()

	{

		setContentPane(basePanel);

		setLocation(5, 6);

		setSize(1000, 1000);

		setResizable(true);

		setVisible(true);

	}
}
