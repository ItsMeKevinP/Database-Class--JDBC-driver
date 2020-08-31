import java.awt.Toolkit;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



public class JDBCDataBaseConnect {

	// url is the databse URL
	public static final String URL = "FILL IN";
	// Use your username and Password
	public static final String USER = "sa";
	public static final String PASS = "***";

	// returns a connection object with a succesful connection or throws an
	// exception if it fails
	public static Connection getConnection() {
		try {
			// registering the driver for SQL server
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException ex) {
			throw new RuntimeException("Error Connecting to the database", ex);
		}
	}

	public static void main(String[] args) {
		Connection connection = null;

		
		int length,max=0;
		
		try {
			// connection to database
			connection = JDBCDataBaseConnect.getConnection();

			// statement to excute the query to get result set
			Statement s = connection.createStatement();

			// Executes SQL query
			ResultSet r = s.executeQuery( 
					  
					
					"\r\n" + 
					"				SELECT DATEDIFF(MILLISECOND, StartingDateTime, EndingDateTime) AS TotalExecutionTimeInMilliseconds\r\n" + 
					"				FROM Process.WorkflowSteps\r\n" + 
					"				WHERE WorkFlowStepDescription LIKE 'Load% Star% Schema'");
			// prints out column names for result set
			
			DefaultTableModel model1 = new DefaultTableModel();
			model1.addColumn("");
			String[] colData = new String[r.getMetaData().getColumnCount()+1];
			for (int i = 0; i < r.getMetaData().getColumnCount(); i++) {
				colData[i] = r.getMetaData().getColumnName(i+1);//populate columns for table
				model1.addColumn(r.getMetaData().getColumnName(i+1));
			}

		
				
				// process the result set
			int rownumber =0;
			while (r.next()) {
				// prints out rows
				++rownumber;
				length=0;
				for (int i = 0; i < r.getMetaData().getColumnCount(); i++) {
					
					System.out.print(r.getString(i + 1) + "\t");
					colData[0]=rownumber+"";
					colData[i+1]= r.getString(i + 1);
					if(r.getString(i+1)==null)
						length+=4;
					else length+= r.getString(i+1).length();
				}
				System.out.println();
				if(length >=max)
					max=length;
				
				model1.addRow(colData);
			}
			//container for the table
			JFrame frame = new JFrame();
			JTable tables = new JTable(model1);	// make table from the modeled data
			tables.getColumnModel().getColumn(0).setPreferredWidth(Integer.toString(rownumber).length());//rownumber so it doesnt take too much space
			tables.setBounds(5, 5, 200, 300);
			JScrollPane scrollpane = new JScrollPane(tables);//scroll pane to enable viewing of all data
			
			frame.add(scrollpane);//add the scrollpane to frame. The scrollpane contains the table(previous line of code does this)
			frame.setSize(16*max,Toolkit.getDefaultToolkit().getScreenSize().height-45);//set bounds of frame container
			frame.setVisible(true);//show
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// closes the connection if it still open
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
