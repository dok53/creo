package myCreo;
/**
 * @author derekok
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.mysql.jdbc.PreparedStatement;

public class RepsChart {

	private ArrayList<Integer> employeesJobNumbers = new ArrayList<Integer>();
	private ArrayList<Integer> employeeSoldJobNumbers = new ArrayList<Integer>();
	private ArrayList<Integer> daysJobOpen = new ArrayList<Integer>();
	private ArrayList<Integer> daysJobSold = new ArrayList<Integer>();

	private int monthChosen;
	private String yearChosen;
	private String employeeName;
	private JFrame frame;
	
	Connection connect;

	public RepsChart(int monthCosen, String yearChosen, String name){
		this.monthChosen = monthCosen;
		this.yearChosen = yearChosen;
		this.employeeName = name;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		fillEmployeeJobNumbers(employeeName, monthChosen, yearChosen, "open");
		fillEmployeeJobNumbers(employeeName, monthChosen, yearChosen, "sold");
		fillDaysJobOpen(employeeName, monthChosen, yearChosen, "open");
		fillDaysJobOpen(employeeName, monthChosen, yearChosen, "sold");
		connect.close();
		/*for (int i = 0; i < daysJobSold.size(); i++){
			System.out.println(daysJobSold.get(i)+ " NUMBER " + employeeSoldJobNumbers.get(i));
		}*/

		// Bar chart for open jobs
		final String series1 = "Open";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < employeesJobNumbers.size(); i ++){
			dataset.setValue(daysJobOpen.get(i), series1, employeesJobNumbers.get(i));
		}
		JFreeChart chart = ChartFactory.createBarChart("Job Status Chart", "Open jobs", "Days open", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		ChartUtilities.applyCurrentTheme(chart);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		BarRenderer renderer1 = new StackedBarRenderer(false);

		renderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer1.setBaseItemLabelsVisible(true);
		renderer1.setBaseItemLabelFont(new Font("Times New Roman", Font.BOLD, 14));
		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(
				0.0f, 0.0f, Color.GREEN, 
				0.0f, 0.0f, Color.lightGray
				);

		renderer1.setSeriesPaint(0, gp0);
		chart.getCategoryPlot().setRenderer(renderer1);

		// Bar chart for sold jobs
		final String soldSeries1 = "Sold";

		DefaultCategoryDataset soldDataset = new DefaultCategoryDataset();
		for (int i = 0; i < employeeSoldJobNumbers.size(); i ++){
			soldDataset.setValue(daysJobSold.get(i), soldSeries1, employeeSoldJobNumbers.get(i));
		}
		JFreeChart soldChart = ChartFactory.createBarChart("", "Sold jobs", "Days since first recieved", soldDataset,
				PlotOrientation.VERTICAL, false, true, false);
		ChartUtilities.applyCurrentTheme(soldChart);

		CategoryPlot soldPlot = (CategoryPlot) soldChart.getPlot();
		// set the range axis to display integers only...
		final NumberAxis soldRangeAxis = (NumberAxis) soldPlot.getRangeAxis();
		soldRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		BarRenderer soldRenderer1 = new StackedBarRenderer(false);

		soldRenderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		soldRenderer1.setBaseItemLabelsVisible(true);
		soldRenderer1.setBaseItemLabelFont(new Font("Times New Roman", Font.BOLD, 14));
		// set up gradient paints for series...
		final GradientPaint gp1 = new GradientPaint(
				0.0f, 0.0f, Color.RED, 
				0.0f, 0.0f, Color.lightGray
				);

		soldRenderer1.setSeriesPaint(0, gp1);
		soldChart.getCategoryPlot().setRenderer(soldRenderer1);
		// Frame setup for grid layout
		frame = new JFrame("Sales for "+employeeName+" for the month of " + getMonth(monthChosen));
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
					JOptionPane.showMessageDialog(null,"Cannot print reps chart","",1);
					e1.printStackTrace();
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Cannot exit reps chart","",1);
					e1.printStackTrace();
				}
			}
		});
		/////////////////////////////////////////
		frame.setLayout(new GridBagLayout());     
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL; 
		c.ipady = 350;
		c.weightx = 0.5;     
		c.gridx = 2;     
		c.gridy = 0;     
		frame.add(new ChartPanel (chart),c);

		//lineChart.removeLegend();
		c.fill = GridBagConstraints.HORIZONTAL;     
		c.ipady = 335;      //make this component tall     
		c.weightx = 0.0; 
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridwidth = 3;     
		c.gridx = 0;     
		c.gridy = 1;     
		frame.add(new ChartPanel (soldChart),c);

		frame.pack();
		frame.setVisible(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,screen.height - 30);
	}
	public void fillEmployeeJobNumbers(String employee, int month, String year, String status)
	{
		try {
			//String name = myCreo.decrypt(employee);
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("SELECT * FROM `jobs` WHERE `Rep` = ('"+employee+"')"
					+ "AND MONTH(`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"')AND `Reason for closing` = ('"+status+"')");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				if (status.equalsIgnoreCase("open")){
					employeesJobNumbers.add(Integer.parseInt(result.getString(1)));
				}
				if (status.equalsIgnoreCase("sold")){
					employeeSoldJobNumbers.add(Integer.parseInt(result.getString(1)));
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill employeeJobNumbers","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillDaysJobOpen(String employee, int month, String year, String status)
	{
		try {
			//String name = myCreo.decrypt(employee);
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("SELECT ABS(DATEDIFF( `Primary Record` , CURDATE( ))) FROM `jobs` WHERE `Rep` = ('"+employee+"')"
					+ "AND MONTH(`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"')AND `Reason for closing` = ('"+status+"')");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				if (status.equalsIgnoreCase("open")){
					daysJobOpen.add(Integer.parseInt(result.getString(1)));
				}
				if (status.equalsIgnoreCase("sold")){
					daysJobSold.add(Integer.parseInt(result.getString(1)));
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill employeeJobNumbers","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillDaysJobSold(String employee, int month, String year)
	{
		try {
			//String name = myCreo.decrypt(employee);
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("SELECT ABS(DATEDIFF( `Primary Record` , CURDATE( ))) FROM `jobs` WHERE `Passed To` = ('"+employee+"')"
					+ "AND MONTH(`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"')");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				daysJobOpen.add(Integer.parseInt(result.getString(1)));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill employeeJobNumbers","Missing info",2);
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
	/**
	 * @return the employeesJobNumbers
	 */
	public ArrayList<Integer> getEmployeesJobNumbers() {
		return employeesJobNumbers;
	}
	/**
	 * @param employeesJobNumbers the employeesJobNumbers to set
	 */
	public void setEmployeesJobNumbers(ArrayList<Integer> employeesJobNumbers) {
		this.employeesJobNumbers = employeesJobNumbers;
	}
	/**
	 * @return the daysJobOpen
	 */
	public ArrayList<Integer> getDaysJobOpen() {
		return daysJobOpen;
	}
	/**
	 * @param daysJobOpen the daysJobOpen to set
	 */
	public void setDaysJobOpen(ArrayList<Integer> daysJobOpen) {
		this.daysJobOpen = daysJobOpen;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return the daysJobSold
	 */
	public ArrayList<Integer> getDaysJobSold() {
		return daysJobSold;
	}
	/**
	 * @param daysJobSold the daysJobSold to set
	 */
	public void setDaysJobSold(ArrayList<Integer> daysJobSold) {
		this.daysJobSold = daysJobSold;
	}
	/**
	 * @return the employeeSoldJobNumbers
	 */
	public ArrayList<Integer> getEmployeeSoldJobNumbers() {
		return employeeSoldJobNumbers;
	}
	/**
	 * @param employeeSoldJobNumbers the employeeSoldJobNumbers to set
	 */
	public void setEmployeeSoldJobNumbers(ArrayList<Integer> employeeSoldJobNumbers) {
		this.employeeSoldJobNumbers = employeeSoldJobNumbers;
	}



}
