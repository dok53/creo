package myCreo;
/**
 * @author derekok
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.mysql.jdbc.PreparedStatement;

public class JobStatusRep{

	String[] columnNames = {"Job ID","Forename","Surname","Address", "County", "Job Status", "Rep Assigned", "Job Outcome", "Date"};
	private JFrame frame;
	private JTable aTable;
	final DefaultTableModel model = new DefaultTableModel(0,columnNames.length);

	
	private int monthChosen;
	private String yearChosen;
	private String rep;
	private String jobType;
	Connection connect;
	 
	 
	 
	public JobStatusRep(int monthChosen, String yearChosen, String rep, String jobType){
		this.monthChosen = monthChosen;
		this.yearChosen = yearChosen;
		this.rep = rep;
		this.jobType = jobType;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		if (jobType.equalsIgnoreCase("all")){
			fillTableNoType(monthChosen, yearChosen, rep);
		}
		else{
			fillTable(monthChosen, yearChosen,jobType, rep);
		}
		connect.close();
		
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		aTable = new JTable(model);
		aTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		aTable.setFillsViewportHeight(true);
		aTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < columnNames.length; i ++)
		{
			aTable.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
			aTable.getColumnModel().getColumn(i).setPreferredWidth(screen.width / columnNames.length);
		}
		
		JScrollPane scrollPane = new JScrollPane(aTable);
		
		frame = new JFrame("Job status for the month of " + getMonth(monthChosen));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		frame.pack();
		frame.setVisible(true);
		frame.setVisible(true);
		frame.setBounds(0, 0,screen.width,screen.height - 30);
		frame.add(scrollPane);
		
		
		
		//Menu bar
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		bar.add(file);
		frame.setJMenuBar(bar);
		JMenuItem print = new JMenuItem("Print");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(print);
		file.add(exit);
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					print();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Cannot print customer chart","",1);
					e1.printStackTrace();
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Cannot exit customer chart","",1);
					e1.printStackTrace();
				}
			}
		});
	}

	public void fillTable(int month, String year,String type, String rep)
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("select * from `jobs`WHERE "
					+ "MONTH(`Primary Record`) = ('"+month+"') AND `Status` = ('"+type+"') AND YEAR (`Primary Record`) = ('"+year+"') AND `rep`  = ('"+rep+"')");
			ResultSet result = selectStatement.executeQuery();
			int i = 0;
			while (result.next())
			{
				model.insertRow(i,new Object[]{result.getString(1),(result.getString(2)),(result.getString(3)),(result.getString(4)),result.getString(8)
						,result.getString(13),result.getString(15), result.getString(16).toString(), result.getString(18).toString()});
				i ++;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill table in jobStatus rep","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillTableNoType(int month, String year, String rep)
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("select * from `jobs`WHERE "
					+ "MONTH(`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') AND `rep`  = ('"+rep+"')");
			ResultSet result = selectStatement.executeQuery();
			int i = 0;
			while (result.next())
			{
				model.insertRow(i,new Object[]{result.getString(1),(result.getString(2)),(result.getString(3)),(result.getString(4)),result.getString(8)
						,result.getString(13),result.getString(15), result.getString(16).toString(), result.getString(18).toString()});
				i ++;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot get sales for month","Missing info",2);
			e.printStackTrace();
		}
	}
	
	public void print()
	{
		PrinterJob pj = PrinterJob.getPrinterJob();
		PageFormat pf = pj.pageDialog(pj.defaultPage());
		pj.setPrintable(new MyPrintable(), pf);
		if (pj.printDialog()) {
			try {
				pj.print();
			} catch (PrinterException pe) {
				pe.printStackTrace(System.err);
			}
		}
	}
	class MyPrintable implements Printable {
		public int print(Graphics g, PageFormat pf, int i) throws PrinterException {
			if (i > 0) {
				return NO_SUCH_PAGE;
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			// get the bounds of the component
			Dimension dim = frame.getSize();
			double cHeight = dim.getHeight();
			double cWidth = dim.getWidth();

			// get the bounds of the printable area
			double pHeight = pf.getImageableHeight();
			double pWidth = pf.getImageableWidth();

			double xRatio = pWidth / cWidth;
			double yRatio = pHeight / cHeight;

			Paper A4 = new Paper();
			A4.setSize(595, 842);

			g2d.scale(xRatio, yRatio);

			frame.printAll(g);
			return Printable.PAGE_EXISTS;
		}
	}
	/**
	 * @return the monthChosen
	 */
	public int getMonthChosen() {
		return monthChosen;
	}
	/**
	 * @param monthChosen the monthChosen to set
	 */
	public void setMonthChosen(int monthChosen) {
		this.monthChosen = monthChosen;
	}
	/**
	 * @return the yearChosen
	 */
	public String getYearChosen() {
		return yearChosen;
	}
	/**
	 * @param yearChosen the yearChosen to set
	 */
	public void setYearChosen(String yearChosen) {
		this.yearChosen = yearChosen;
	}
	public String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month-1];
	}
	public String getRep() {
		return rep;
	}
	public void setRep(String rep) {
		this.rep = rep;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
}