package myCreo;
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
import java.sql.SQLException;
import java.text.DateFormatSymbols;
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

public class CountyChart{

	private double windowsDoors;
	private double doors;
	private double windows;
	private double wdSupply;
	private double conservatory;
	private double sunLounge;
	private double cavAttInsul;
	private double glazing;
	private double grosfillex;
	private double forCladding;
	private double intDoors;
	private double attInsul;
	private double spirare;
	private double cavInsul;
	private double fsg;
	private double guttering;
	
	private JFrame frame;

	HashMap<String, Integer> prodTypePerCounty = new HashMap<String, Integer>();
	HashMap<String, Integer> productOnDay = new HashMap<String, Integer>();

	private ArrayList<Integer> dateOfSale = new ArrayList<Integer>();
	private ArrayList<String> product = new ArrayList<String>();
	private ArrayList<Integer> noOfProducts = new ArrayList<Integer>();

	private int monthChosen;
	private String yearChosen;
	private String county;
	Connection connect;

	public CountyChart(int monthChosen, String yearChosen, String county){
		this.monthChosen = monthChosen;
		this.yearChosen = yearChosen;
		this.county = county;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		fillProductsPerCounty(monthChosen, yearChosen,county);
		fillProductTypePerCounty(monthChosen, yearChosen, county);
		fillProductOnDay(monthChosen, yearChosen, county);
		connect.close();
		// row keys...
		final String series1 = "Windows & Doors";
		final String series2 = "Doors";
		final String series3 = "Windows";
		final String series4 = "WD supply";
		final String series5 = "Conservatory";
		final String series6 = "Sun Lounge";
		final String series7 = "Cav & Attic Insul";
		final String series8 = "Glazing";
		final String series9 = "Grossfillex";
		final String series10 = "Fortex Cladding";
		final String series11 = "Internal Doors";
		final String series12 = "Attic Insul";
		final String series13 = "Spirare";
		final String series14 = "Cav Insul";
		final String series15 = "F/S/G";
		final String series16 = "Guttering";


		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(windowsDoors, series1, "Windows & Doors");
		dataset.setValue(doors, series2, "Doors");
		dataset.setValue(windows, series3, "Windows");
		dataset.setValue(wdSupply, series4, "WD Supply");
		dataset.setValue(conservatory, series5, "Conservatory");
		dataset.setValue(sunLounge, series6, "Sub Lounge");
		dataset.setValue(cavAttInsul, series7, "Cav & Att Insul");
		dataset.setValue(glazing, series8, "Glazing");
		dataset.setValue(grosfillex, series9, "Grossfillex");
		dataset.setValue(forCladding, series10, "Fortex cladding");
		dataset.setValue(intDoors, series11, "Internal Doors");
		dataset.setValue(attInsul, series12, "Attin Insul");
		dataset.setValue(spirare, series13, "Spirare");
		dataset.setValue(cavInsul, series14, "Cav Insul");
		dataset.setValue(fsg, series15, "F/S/G");
		dataset.setValue(guttering, series16, "Guttering");
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
		if (prodTypePerCounty.isEmpty()){
			pieDataset.setValue("No Values", 100);
		}
		else{
			for (Entry<String, Integer> entry : prodTypePerCounty.entrySet()){
				pieDataset.setValue(entry.getKey(), entry.getValue());
			}
		}

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

		//Graph chart setup
		final XYSeries lineSeries1 = new XYSeries("Windows & Doors");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Windows & Doors")){
				lineSeries1.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries1.add(j,0);
			}
		}

		final XYSeries lineSeries2 = new XYSeries("Doors");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Doors")){
				lineSeries2.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries2.add(j,0);
			}
		}
		final XYSeries lineSeries3 = new XYSeries("Windows");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Windows")){
				lineSeries3.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries3.add(j,0);
			}
		}
		final XYSeries lineSeries4 = new XYSeries("WD Supply Only");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("WD Supply Only")){
				lineSeries4.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries4.add(j,0);
			}
		}
		final XYSeries lineSeries5 = new XYSeries("Conservatory");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Conservatory")){
				lineSeries5.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries5.add(j,0);
			}
		}
		final XYSeries lineSeries6 = new XYSeries("Sun Lounge");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Sun Lounge")){
				lineSeries6.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries6.add(j,0);
			}
		}
		final XYSeries lineSeries7 = new XYSeries("Cav and Attic Insulation");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Cav and Attic Insulation")){
				lineSeries7.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries7.add(j,0);
			}
		}
		final XYSeries lineSeries8 = new XYSeries("Glazing");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Glazing")){
				lineSeries8.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries8.add(j,0);
			}
		}
		final XYSeries lineSeries9 = new XYSeries("Grosfillex");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Grosfillex")){
				lineSeries9.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries9.add(j,0);
			}
		}
		final XYSeries lineSeries10 = new XYSeries("Fortex Cladding");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Fortex Cladding")){
				lineSeries10.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries10.add(j,0);
			}
		}
		final XYSeries lineSeries11 = new XYSeries("Internal Doors");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Internal Doors")){
				lineSeries11.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries11.add(j,0);
			}
		}
		final XYSeries lineSeries12 = new XYSeries("Attic Insulation");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Attic insulation")){
				lineSeries12.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries12.add(j,0);
			}
		}
		final XYSeries lineSeries13 = new XYSeries("Spirare");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Spirare")){
				lineSeries13.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries13.add(j,0);
			}
		}
		final XYSeries lineSeries14 = new XYSeries("Cavity Insulation");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Cavity Insulation")){
				lineSeries14.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries14.add(j,0);
			}
		}
		final XYSeries lineSeries15 = new XYSeries("F/S/G");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("F/S/G")){
				lineSeries15.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries15.add(j,0);
			}
		}
		final XYSeries lineSeries16 = new XYSeries("Guttering");
		for (int j = 0; j < product.size(); j ++){
			if (product.get(j).equalsIgnoreCase("Guttering")){
				lineSeries16.add(dateOfSale.get(j),noOfProducts.get(j));
			}
			else{
				lineSeries16.add(j,0);
			}
		}

		final XYSeriesCollection lineDataset = new XYSeriesCollection();
		lineDataset.addSeries(lineSeries1);
		lineDataset.addSeries(lineSeries2);
		lineDataset.addSeries(lineSeries3);
		lineDataset.addSeries(lineSeries4);
		lineDataset.addSeries(lineSeries5);
		lineDataset.addSeries(lineSeries6);
		lineDataset.addSeries(lineSeries7);
		lineDataset.addSeries(lineSeries8);
		lineDataset.addSeries(lineSeries9);
		lineDataset.addSeries(lineSeries10);
		lineDataset.addSeries(lineSeries11);
		lineDataset.addSeries(lineSeries12);
		lineDataset.addSeries(lineSeries13);
		lineDataset.addSeries(lineSeries14);
		lineDataset.addSeries(lineSeries15);
		lineDataset.addSeries(lineSeries16);

		JFreeChart lineChart = ChartFactory.createXYLineChart("Line Chart Demo ","Date","No Of Products", lineDataset, PlotOrientation.VERTICAL,
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
		linePlot.getRenderer().setSeriesPaint(3, Color.BLUE);
		linePlot.getRenderer().setSeriesPaint(4, Color.BLACK);
		linePlot.getRenderer().setSeriesPaint(5, Color.CYAN);
		linePlot.getRenderer().setSeriesPaint(6, Color.MAGENTA);
		linePlot.getRenderer().setSeriesPaint(7, Color.YELLOW);
		linePlot.getRenderer().setSeriesPaint(8, Color.PINK);
		linePlot.getRenderer().setSeriesPaint(9, Color.ORANGE);
		linePlot.getRenderer().setSeriesPaint(10, Color.BLUE);
		linePlot.getRenderer().setSeriesPaint(11, Color.BLACK);
		linePlot.getRenderer().setSeriesPaint(12, Color.CYAN);
		linePlot.getRenderer().setSeriesPaint(13, Color.MAGENTA);
		linePlot.getRenderer().setSeriesPaint(14, Color.YELLOW);
		linePlot.getRenderer().setSeriesPaint(15, Color.PINK);

		// change the auto tick unit selection to integer units only...
		NumberAxis yAxis = (NumberAxis) linePlot.getRangeAxis();
		yAxis.setTickUnit(new NumberTickUnit(1));

		NumberAxis xAxis = (NumberAxis) linePlot.getDomainAxis();
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(30);
		xAxis.setTickUnit(new NumberTickUnit(1)); 




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

		frame = new JFrame("Sales for " + county + " for the month of " + getMonth(monthChosen));
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
					JOptionPane.showMessageDialog(null,"Cannot print county chart","",1);
					e1.printStackTrace();
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Cannot exit county chart","",1);
					e1.printStackTrace();
				}
			}
		});
		/////////////////////////////////////////
		frame.pack();
		frame.setVisible(true);
		frame.setLayout(new GridBagLayout());     
		GridBagConstraints c = new GridBagConstraints(); 

		pieChart = new JFreeChart("Products by county for Month", piePlot);
		pieChart.removeLegend();
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
		c.ipady = 350;
		c.weightx = 0.5;     
		c.gridx = 1;     
		c.gridy = 0;    
		frame.add(new ChartPanel (lineChart),c);


		chart = new JFreeChart("Products by county for Month", plot);
		chart.setBackgroundPaint(Color.WHITE);
		chart.removeLegend();
		c.fill = GridBagConstraints.HORIZONTAL;     
		c.ipady = 335;      //make this component tall     
		c.weightx = 0.0; 
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridwidth = 3;     
		c.gridx = 0;     
		c.gridy = 1;     
		frame.add(new ChartPanel (chart),c); 


		frame.setVisible(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,screen.height - 30);
	}

	public void fillProductsPerCounty(int month, String year, String county) throws SQLException
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("SELECT * FROM `jobs` WHERE `County` = ('"+county+"')"
					+ "AND MONTH(`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"')");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				if (result.getString(10).equalsIgnoreCase("Windows & Doors")){
					windowsDoors ++;
				}
				if (result.getString(10).equalsIgnoreCase("Doors")){
					doors ++;
				}
				if (result.getString(10).equalsIgnoreCase("Windows")){
					windows ++;
				}
				if (result.getString(10).equalsIgnoreCase("WD Supply Only")){
					wdSupply ++;
				}
				if (result.getString(10).equalsIgnoreCase("Conservatory")){
					conservatory ++;
				}
				if (result.getString(10).equalsIgnoreCase("Sun Lounge")){
					sunLounge ++;
				}
				if (result.getString(10).equalsIgnoreCase("Cav & Attic Insulation")){
					cavAttInsul ++;
				}
				if (result.getString(10).equalsIgnoreCase("Glazing")){
					glazing ++;
				}
				if (result.getString(10).equalsIgnoreCase("Grosfillex")){
					grosfillex ++;
				}
				if (result.getString(10).equalsIgnoreCase("Fortex Cladding")){
					forCladding ++;
				}
				if (result.getString(10).equalsIgnoreCase("Attic Insulation")){
					attInsul ++;
				}
				if (result.getString(10).equalsIgnoreCase("Spirare")){
					spirare ++;
				}
				if (result.getString(10).equalsIgnoreCase("Cavity Insulation")){
					cavInsul ++;
				}
				if (result.getString(10).equalsIgnoreCase("F/S/G")){
					fsg ++;
				}
				if (result.getString(10).equalsIgnoreCase("Guttering")){
					guttering ++;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobStatus","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillProductTypePerCounty(int month, String year, String county)
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select `Product Type`,count(*)from "
					+ "jobs WHERE `County` = ('"+county+"') AND MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `Product Type`");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				prodTypePerCounty.put(result.getString(1),Integer.parseInt(result.getString(2)));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill product type per county","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillProductOnDay(int month, String year, String county)
	{
		try {
			PreparedStatement selectStatement;
			for (int i = 0; i < 32; i ++){
				selectStatement = (PreparedStatement) connect.prepareStatement ("select `Product Type`,count(*)from "
						+ "jobs WHERE `County` = ('"+county+"') AND DAY (`Primary Record`) = ('"+i+"') AND MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `Product Type`");
				ResultSet result = selectStatement.executeQuery();
				while (result.next())
				{
					product.add(result.getString(1));
					noOfProducts.add(Integer.parseInt(result.getString(2)));
					dateOfSale.add(i);
				}
				if (!result.next()){
					product.add("Null");
					noOfProducts.add(0);
					dateOfSale.add(i);
				}
			} 
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill product on day","Missing info",2);
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
	public double getWindowsDoors() {
		return windowsDoors;
	}
	public void setWindowsDoors(double windowsDoors) {
		this.windowsDoors = windowsDoors;
	}
	public double getDoors() {
		return doors;
	}
	public void setDoors(double doors) {
		this.doors = doors;
	}
	public double getWdSupply() {
		return wdSupply;
	}
	public void setWdSupply(double wdSupply) {
		this.wdSupply = wdSupply;
	}
	public double getCavAttInsul() {
		return cavAttInsul;
	}
	public void setCavAttInsul(double cavAttInsul) {
		this.cavAttInsul = cavAttInsul;
	}
	public double getGlazing() {
		return glazing;
	}
	public void setGlazing(double glazing) {
		this.glazing = glazing;
	}
	public double getGrosfillex() {
		return grosfillex;
	}
	public void setGrosfillex(double grosfillex) {
		this.grosfillex = grosfillex;
	}
	public double getForCladding() {
		return forCladding;
	}
	public void setForCladding(double forCladding) {
		this.forCladding = forCladding;
	}
	public double getIntDoors() {
		return intDoors;
	}
	public void setIntDoors(double intDoors) {
		this.intDoors = intDoors;
	}
	public double getAttInsul() {
		return attInsul;
	}
	public void setAttInsul(double attInsul) {
		this.attInsul = attInsul;
	}
	public double getSpirare() {
		return spirare;
	}
	public void setSpirare(double spirare) {
		this.spirare = spirare;
	}
	public double getCavInsul() {
		return cavInsul;
	}
	public void setCavInsul(double cavInsul) {
		this.cavInsul = cavInsul;
	}
	public double getFsg() {
		return fsg;
	}
	public void setFsg(double fsg) {
		this.fsg = fsg;
	}
	public double getGuttering() {
		return guttering;
	}
	public void setGuttering(double guttering) {
		this.guttering = guttering;
	}
}