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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

import com.mysql.jdbc.PreparedStatement;

public class JobsChart{


	private double openJobs;
	private double closedJobs;
	private double unassignedJobs;

	private ArrayList<String> counties = new ArrayList<String>();
	private ArrayList<Integer> openJobsOnDay = new ArrayList<Integer>();	
	private ArrayList<Integer> closedJobsOnDay = new ArrayList<Integer>();
	private ArrayList<Integer> unassignedJobsOnDay = new ArrayList<Integer>();
	HashMap<String, Integer> jobsPerCounty = new HashMap<String, Integer>();
	String [] jobStatus = {"live", "closed", "not taken"};
	java.sql.Connection connect;

	private int monthChosen;
	private String yearChosen;
	private JFrame frame;

	public JobsChart(int monthChosen, String yearChosen){
		this.monthChosen = monthChosen;
		this.yearChosen = yearChosen;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		fillCounties(monthChosen, yearChosen);
		fillJobsPerCounty(monthChosen, yearChosen);
		for (int i = 0; i < jobStatus.length; i ++){
			fillJobCategories(yearChosen, monthChosen, jobStatus[i]);
			getJobsStatusForMonth(jobStatus[i],monthChosen, yearChosen);
		}
		connect.close();

		// row keys...
		final String series1 = "Open";
		final String series2 = "Closed";
		final String series3 = "Unassigned";


		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(openJobs, series1, "Open");
		dataset.setValue(closedJobs, series2, "Closed");
		dataset.setValue(unassignedJobs, series3, "Unassigned");
		JFreeChart chart = ChartFactory.createBarChart("Job Status Chart", "Current jobs", "Number of jobs", dataset,
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
		final GradientPaint gp1 = new GradientPaint(
				0.0f, 0.0f, Color.RED, 
				0.0f, 0.0f, Color.lightGray
				);
		final GradientPaint gp2 = new GradientPaint(
				0.0f, 0.0f, Color.ORANGE, 
				0.0f, 0.0f, Color.lightGray
				);
		renderer1.setSeriesPaint(0, gp0);
		renderer1.setSeriesPaint(1, gp1);
		renderer1.setSeriesPaint(2, gp2);
		chart.getCategoryPlot().setRenderer(renderer1);

		// Pie chart setup
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		if (counties.size()<1){
			pieDataset.setValue("No Values", new Double(100));
		}
		else{
			for (Entry<String, Integer> entry : jobsPerCounty.entrySet()){
				pieDataset.setValue(entry.getKey(), entry.getValue());
			}
		}
		/*if (counties.size() > 0){
			for (int i = 0; i < counties.size(); i ++){
				pieDataset.setValue(counties.get(i).toString(),i);
			}
		}
		else{
			pieDataset.setValue("No Values", new Double(27.8));
		}*/
		JFreeChart pieChart = ChartFactory.createPieChart3D("Jobs by county",pieDataset, true,
				true,false );
		PiePlot3D piePlot = (PiePlot3D) pieChart.getPlot();
		piePlot.setBackgroundPaint(null);
		piePlot.setStartAngle(270);
		piePlot.setDirection(Rotation.ANTICLOCKWISE);
		piePlot.setForegroundAlpha(0.60f);
		piePlot.setInteriorGap(0.04);
		piePlot.setOutlineVisible(false);
		//piePlot.setSimpleLabels(true);
		StandardPieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0},{2}");
		piePlot.setLabelGenerator(labelGenerator);
		piePlot.setLabelFont(new Font("Times New Roman", Font.BOLD, 14));
		piePlot.setLabelPaint(Color.BLACK);
		piePlot.setLabelOutlinePaint(null);
		piePlot.setLabelShadowPaint(null);
		piePlot.setLabelBackgroundPaint(null);

		//Line graph chart setup
		final XYSeries lineSeries1 = new XYSeries("Open");
		for (int i = 0; i < 31; i ++){
			lineSeries1.add(i,openJobsOnDay.get(i).doubleValue());
		}

		final XYSeries lineSeries2 = new XYSeries("Closed");
		for (int i = 0; i < 31; i ++){
			lineSeries2.add(i,closedJobsOnDay.get(i).doubleValue());
		}

		final XYSeries lineSeries3 = new XYSeries("Unassigned");
		for (int i = 0; i < 31; i ++){
			lineSeries3.add(i,unassignedJobsOnDay.get(i).doubleValue());
		}

		final XYSeriesCollection lineDataset = new XYSeriesCollection();
		lineDataset.addSeries(lineSeries1);
		lineDataset.addSeries(lineSeries2);
		lineDataset.addSeries(lineSeries3);

		JFreeChart lineChart = ChartFactory.createXYLineChart("Line Chart Demo ","Date","No Of Jobs", lineDataset, PlotOrientation.VERTICAL,
				true,true,false );
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final XYPlot linePlot = lineChart.getXYPlot();
		linePlot.setBackgroundPaint(Color.lightGray);
		linePlot.setDomainGridlinePaint(Color.white);
		linePlot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
		lineRenderer.setSeriesLinesVisible(0, true);
		lineRenderer.setSeriesShapesVisible(1, true);
		linePlot.setRenderer(lineRenderer);
		linePlot.getRenderer().setSeriesPaint(0, Color.GREEN);
		linePlot.getRenderer().setSeriesPaint(1, Color.RED);
		linePlot.getRenderer().setSeriesPaint(2, Color.ORANGE);

		// change the auto tick unit selection to integer units only...
		NumberAxis yAxis = (NumberAxis) linePlot.getRangeAxis();
		yAxis.setTickUnit(new NumberTickUnit(1));

		NumberAxis xAxis = (NumberAxis) linePlot.getDomainAxis();
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(30);




		/////////////////////////////////////////////////////
		// Frame setup for grid layout
		/*JFrame frame = new JFrame("Chart");
		frame.setLayout(new GridLayout(2, 2));
		frame.add(new ChartPanel (chart));
		frame.add(new ChartPanel (lineChart));
		frame.add(new ChartPanel (pieChart));
		frame.add(new ChartPanel (pieChart));
		//frame.getContentPane().add(new ChartPanel(chart), BorderLayout.NORTH);
		//frame.getContentPane().add(new ChartPanel(pieChart), BorderLayout.EAST);
		//frame.getContentPane().add(new ChartPanel(chart), BorderLayout.SOUTH);
		//frame.getContentPane().add(new ChartPanel(chart), BorderLayout.WEST);
		frame.pack();
		frame.setVisible(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,screen.height);*/
		//frame.setSize(500, 500);

		frame = new JFrame("Chart");
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
					JOptionPane.showMessageDialog(null,"Cannot print jobs chart","",1);
					e1.printStackTrace();
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Cannot print county chart","",1);
					e1.printStackTrace();
				}
			}
		});
		/////////////////////////////////////////
		frame.pack();
		frame.setVisible(true);
		frame.setLayout(new GridBagLayout());     
		GridBagConstraints c = new GridBagConstraints(); 

		chart = new JFreeChart("Job Status for Month", plot);
		chart.setBackgroundPaint(Color.WHITE);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 350;
		c.weightx = 0.5;     
		c.gridx = 1;     
		c.gridy = 0;     
		frame.add(new ChartPanel (chart),c);       
		pieChart = new JFreeChart("Jobs by county for Month", piePlot);
		pieChart.setBackgroundPaint(Color.WHITE);
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.ipady = 350;
		c.weightx = 0.5;     
		c.gridx = 2;     
		c.gridy = 0;     
		frame.add(new ChartPanel (pieChart),c);       
		lineChart = new JFreeChart("Jobs by date for Month", linePlot);
		lineChart.setBackgroundPaint(Color.WHITE);
		c.fill = GridBagConstraints.HORIZONTAL;     
		c.ipady = 335;      //make this component tall     
		c.weightx = 0.0; 
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridwidth = 3;     
		c.gridx = 0;     
		c.gridy = 1;     
		frame.add(new ChartPanel (lineChart),c); 


		frame.setVisible(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,screen.height - 30);
	}
	public void getJobsStatusForMonth(String status, int month, String year) throws SQLException
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("SELECT * FROM `jobs` WHERE `Status` = ('"+status+"')"
					+ "AND MONTH(`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"')");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				if (status.equals("live")){
					openJobs ++;
				}
				if (status.equals("closed")){
					closedJobs ++;
				}
				if (status.equals("not taken")){
					unassignedJobs ++;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobStatus","Missing info",2);
			e.printStackTrace();
		}
	}

	public void fillCounties(int month, String year) throws SQLException
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("SELECT * FROM `jobs` WHERE MONTH (`Primary Record`) = "
					+ "('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"')");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				counties.add(result.getString(8));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill counties","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillJobsPerCounty(int month, String year)
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select county,count(*)from jobs WHERE MONTH (`Primary Record`) ="
					+ "('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by county");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				jobsPerCounty.put(result.getString(1),Integer.parseInt(result.getString(2)));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobCsPerCounty","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillJobCategories(String year, int month, String state)
	{
		try {
			PreparedStatement countRows;
			for (int i = 0; i < 31; i ++){
				countRows = (PreparedStatement) connect.prepareStatement("SELECT COUNT( * ) FROM jobs WHERE "
						+ "YEAR (`Primary Record`) = ('"+year+"') AND MONTH(`Primary Record`) = ('"+month+"')AND DAY (`Primary Record`) = ('"+i+"') AND `Status` = ('"+state+"')");
				ResultSet rows = countRows.executeQuery();
				while (rows.next())
				{
					if (state.equals("not taken")){
						unassignedJobsOnDay.add(rows.getInt(1));
					}
					if (state.equals("live")){
						openJobsOnDay.add(rows.getInt(1));
					}
					if (state.equals("closed")){
						closedJobsOnDay.add(rows.getInt(1));
					}
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobCategories","Missing info",2);
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
}
