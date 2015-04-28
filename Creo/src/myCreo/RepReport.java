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

public class RepReport{

	String[] columnNames = {"Month","Leads","Recieved","Sales", "Rate%"};
	private JFrame frame;
	private JTable aTable;
	final DefaultTableModel model = new DefaultTableModel(0,columnNames.length);

	
	private String yearChosen;
	private String rep;
	Connection connect;
	double[] monthAmount = new double[12];
	double[] monthSoldAmount = new double[12];
	int[] precentageForMonth = new int[12];
	double[] recieved = new double[12];
	 
	public RepReport(String yearChosen, String rep){
		this.yearChosen = yearChosen;
		this.rep = rep;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);;
		jobsForMonth(yearChosen,rep);
		jobsSoldForMonth(yearChosen, rep);
		jobsRecieved(yearChosen,rep);
		precentage();
		fillTable();
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
		
		frame = new JFrame("Conversion rates for " + rep);
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
	public void fillTable()
	{
		for (int i = 0; i < 12; i ++){
			model.insertRow(i,new Object[]{getMonth(i+1),(int)monthAmount[i],(int)recieved[i],(int)monthSoldAmount[i],precentageForMonth[i]});
		}
		model.addRow(new Object[]{"Total", (int)addDoubleTotals(monthAmount), (int)addDoubleTotals(recieved), (int)addDoubleTotals(monthSoldAmount), addIntTotals(precentageForMonth)});
	}
	
	public double addDoubleTotals(double [] array)
	{
		double sum = 0;
		for (double i : array)
			sum += i;
		return sum;
	}
	public int addIntTotals(int [] array)
	{
		int sum = 0;
		for (int i : array)
			sum += i;
		return sum;
	}
	public void jobsRecieved(String year, String rep)
	{
		for (int j = 1; j < 13; j ++){
			try {
				PreparedStatement selectStatement;
				selectStatement = (PreparedStatement) connect.prepareStatement("select count(*)from jobs WHERE `Rep` = ('"+rep+"') AND YEAR (`Primary Record`) = ('"+year+"')"
						+ " AND MONTH(`Primary Record`) = ('"+j+"')");
				ResultSet result = selectStatement.executeQuery();
				while (result.next())
				{
					recieved[j-1] = Integer.parseInt(result.getString(1));
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"Cannot fill jobs recieved","Missing info",2);
				e.printStackTrace();
			}
		}
	}
	public void jobsForMonth(String year, String rep)
	{
		for (int j = 1; j < 13; j ++){
			try {
				PreparedStatement selectStatement;
				selectStatement = (PreparedStatement) connect.prepareStatement("select count(*)from jobs WHERE `Passed to` = ('"+rep+"') AND YEAR (`Primary Record`) = ('"+year+"')"
						+ " AND MONTH(`Primary Record`) = ('"+j+"')");
				ResultSet result = selectStatement.executeQuery();
				while (result.next())
				{
					monthAmount[j-1] = Integer.parseInt(result.getString(1));
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"Cannot jobs for month","Missing info",2);
				e.printStackTrace();
			}
		}
	}
	public void jobsSoldForMonth(String year, String rep)
	{
		for (int j = 1; j < 13; j ++){
			try {
				PreparedStatement selectStatement;
				selectStatement = (PreparedStatement) connect.prepareStatement("select count(*)from jobs WHERE `Rep` = ('"+rep+"') AND YEAR (`Primary Record`) = ('"+year+"')"
						+ " AND MONTH(`Primary Record`) = ('"+j+"') AND `Reason for closing` = 'sold'");
				ResultSet result = selectStatement.executeQuery();
				while (result.next())
				{
					monthSoldAmount[j-1] = Integer.parseInt(result.getString(1));
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"Cannot fill jobs sold for month","Missing info",2);
				e.printStackTrace();
			}
		}
	}
	public void precentage()
	{
		for (int i = 0; i < 12; i ++)
		{
			if (monthAmount[i] != 0){
				double pre = (monthSoldAmount[i]/(monthAmount[i]) * 100);
				int num = Integer.parseInt(String.valueOf(pre).split("\\.")[0]);
				precentageForMonth[i] = num;
			}
			else{
				precentageForMonth[i] = 0;
			}
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
	
}