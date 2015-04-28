package myCreo;
/**
 * @author derekok
 *
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

import com.mysql.jdbc.PreparedStatement;

public class ReferralsChart {
	private double repeatCus;
	private double wordOfMouth;
	private double wlrfm;
	private double vans;
	private double flyer;
	private double stand;
	private double citySquare;
	private JFrame frame;

	private ArrayList<Integer> dateOfReferral = new ArrayList<Integer>();
	private ArrayList<String> referral = new ArrayList<String>();
	private ArrayList<Integer> noOfReferrals = new ArrayList<Integer>();

	HashMap<String, Integer> referralsPerCounty = new HashMap<String, Integer>();
	String [] referenceType = {"Repeat Customer", "Word Of Mouth", "WLRFM", "Vans", "Flyer/Ad", "Stand", "City Square"};
	private int monthChosen;
	private String yearChosen;
	Connection connect;

	public ReferralsChart(int monthCosen, String yearChosen){
		this.monthChosen = monthCosen;
		this.yearChosen = yearChosen;
	}
	public void draw() throws Exception{

		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		fillReferencesPerCounty(monthChosen, yearChosen);
		fillReferralsOnDay(monthChosen, yearChosen);
		connect.close();
		// Line graph setup
		final XYSeries lineSeries1 = new XYSeries("Repeat customer");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("old customer")){
				lineSeries1.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}
		final XYSeries lineSeries2 = new XYSeries("Word of mouth");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Word-of-mouth")){
				lineSeries2.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}

		final XYSeries lineSeries3 = new XYSeries("WLR");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("WLR")){
				lineSeries3.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}

		final XYSeries lineSeries4 = new XYSeries("Company Vans");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Company Vans")){
				lineSeries4.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}

		final XYSeries lineSeries5 = new XYSeries("Flyer/Advert");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Flyer/Advert")){
				lineSeries5.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}

		final XYSeries lineSeries6 = new XYSeries("Grant");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Grant")){
				lineSeries6.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}

		final XYSeries lineSeries7 = new XYSeries("City square");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("City square")){
				lineSeries7.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}
		final XYSeries lineSeries8 = new XYSeries("Advertising");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Advertising")){
				lineSeries8.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}
		final XYSeries lineSeries9 = new XYSeries("B.O.I Tramore");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("B.O.I Tramore")){
				lineSeries9.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}
		final XYSeries lineSeries10 = new XYSeries("Golden Pages");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Golden Pages")){
				lineSeries10.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}
		final XYSeries lineSeries11 = new XYSeries("Knows Company");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Knows Company")){
				lineSeries11.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}
		final XYSeries lineSeries12 = new XYSeries("Recommended");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("recommended")){
				lineSeries12.add(dateOfReferral.get(i),noOfReferrals.get(i));
			}
		}
		final XYSeries lineSeries13 = new XYSeries("Website");
		for(int i = 0; i < referral.size(); i ++){
			if (referral.get(i).equalsIgnoreCase("Website")){
				lineSeries13.add(dateOfReferral.get(i),noOfReferrals.get(i));
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

		JFreeChart lineChart = ChartFactory.createXYLineChart("Line Chart Demo ","Date of referral","No Of referrals", lineDataset, PlotOrientation.VERTICAL,
				true,true,false );
		lineChart.setBackgroundPaint(Color.white);

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
		linePlot.getRenderer().setSeriesPaint(6, Color.YELLOW);
		//linePlot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);

		// change the auto tick unit selection to integer units only...
		NumberAxis yAxis = (NumberAxis) linePlot.getRangeAxis();
		yAxis.setTickUnit(new NumberTickUnit(1));

		NumberAxis xAxis = (NumberAxis) linePlot.getDomainAxis();
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(30);
		// Pie chart setup
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		if (referralsPerCounty.isEmpty()){
			pieDataset.setValue("No Values", 100);
		}
		else{
			for (Entry<String, Integer> entry : referralsPerCounty.entrySet()){
				pieDataset.setValue(entry.getKey(), entry.getValue());
			}
		}
		JFreeChart pieChart = ChartFactory.createPieChart3D("Referral type",pieDataset, false,
				true,false );
		PiePlot3D piePlot = (PiePlot3D) pieChart.getPlot();
		piePlot.setStartAngle(270);
		piePlot.setDirection(Rotation.ANTICLOCKWISE);
		piePlot.setForegroundAlpha(0.60f);
		piePlot.setBackgroundPaint(Color.WHITE);
		piePlot.setInteriorGap(0.04);
		piePlot.setOutlineVisible(false);
		//piePlot.setSimpleLabels(true);
		//piePlot.setLabelGenerator(null);
		StandardPieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0},{2}");
		piePlot.setLabelGenerator(labelGenerator);

		piePlot.setLabelFont(new Font("Times New Roman", Font.BOLD, 14));
		piePlot.setLabelLinkPaint(Color.BLACK);
		piePlot.setLabelLinkStroke(new BasicStroke(1.0f));
		piePlot.setLabelPaint(Color.BLACK);
		piePlot.setLabelOutlinePaint(null);
		piePlot.setLabelShadowPaint(null);
		piePlot.setLabelBackgroundPaint(null);

		// Frame setup for grid layout
		frame = new JFrame("Referrals for the month of " + getMonth(monthChosen));
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
					JOptionPane.showMessageDialog(null,"Cannot print referrals chart","",1);
					e1.printStackTrace();
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Cannot exit referrals chart","",1);
					e1.printStackTrace();
				}
			}
		});
		/////////////////////////////////////////
		frame.setLayout(new GridBagLayout());     
		GridBagConstraints c = new GridBagConstraints();

		pieChart = new JFreeChart("Referrals for Month", piePlot);
		pieChart.setBackgroundPaint(Color.WHITE);
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.ipady = 350;
		c.weightx = 0.5;     
		c.gridx = 2;     
		c.gridy = 0;     
		frame.add(new ChartPanel (pieChart),c);

		lineChart = new JFreeChart("Referrals for Month", linePlot);
		lineChart.setBackgroundPaint(Color.WHITE);
		c.fill = GridBagConstraints.HORIZONTAL;     
		c.ipady = 335;      //make this component tall     
		c.weightx = 0.0; 
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridwidth = 3;     
		c.gridx = 0;     
		c.gridy = 1;     
		frame.add(new ChartPanel (lineChart),c);

		frame.pack();
		frame.setVisible(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,screen.height - 30);
	}
	public void fillReferencesPerCounty(int month, String year)
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select referral,count(*)from jobs WHERE MONTH (`Primary Record`) ="
					+ "('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by referral");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				referralsPerCounty.put(result.getString(1),Integer.parseInt(result.getString(2)));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could not fill referrals");
			e.printStackTrace();
		}
	}
	public void getJobsReferenceForMonth(String referral, int month, String year) throws SQLException
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement("SELECT * FROM `jobs` WHERE `Referral` = ('"+referral+"')"
					+ "AND MONTH(`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"')");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				if (referral.equals("Repeat customer")){
					repeatCus ++;
				}
				if (referral.equals("Word Of Mouth")){
					wordOfMouth ++;
				}
				if (referral.equals("WLRFM")){
					wlrfm ++;
				}
				if (referral.equals("Vans")){
					vans ++;
				}
				if (referral.equals("Flyer/Ad")){
					flyer ++;
				}
				if (referral.equals("Stand")){
					stand ++;
				}
				if (referral.equals("City Square")){
					citySquare ++;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobStatus","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillReferralsOnDay(int month, String year)
	{
		try {
			PreparedStatement selectStatement;
			for (int i = 0; i < 32; i ++){
				selectStatement = (PreparedStatement) connect.prepareStatement ("SELECT DAYOFMONTH(  `Primary Record` ) ,`Referral`,count(*)from "
						+ "jobs WHERE DAY (`Primary Record`) = ('"+i+"') AND MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `Referral`");
				ResultSet result = selectStatement.executeQuery();
				while (result.next())
				{
					dateOfReferral.add(Integer.parseInt(result.getString(1)));
					referral.add(result.getString(2));
					noOfReferrals.add(Integer.parseInt(result.getString(3)));
				}
			} 
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill referrals on day","Missing info",2);
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
	/**
	 * @return the repeatCus
	 */
	public double getRepeatCus() {
		return repeatCus;
	}
	/**
	 * @param repeatCus the repeatCus to set
	 */
	public void setRepeatCus(double repeatCus) {
		this.repeatCus = repeatCus;
	}
	/**
	 * @return the wordOfMouth
	 */
	public double getWordOfMouth() {
		return wordOfMouth;
	}
	/**
	 * @param wordOfMouth the wordOfMouth to set
	 */
	public void setWordOfMouth(double wordOfMouth) {
		this.wordOfMouth = wordOfMouth;
	}
	/**
	 * @return the wlrfm
	 */
	public double getWlrfm() {
		return wlrfm;
	}
	/**
	 * @param wlrfm the wlrfm to set
	 */
	public void setWlrfm(double wlrfm) {
		this.wlrfm = wlrfm;
	}
	/**
	 * @return the vans
	 */
	public double getVans() {
		return vans;
	}
	/**
	 * @param vans the vans to set
	 */
	public void setVans(double vans) {
		this.vans = vans;
	}
	/**
	 * @return the flyer
	 */
	public double getFlyer() {
		return flyer;
	}
	/**
	 * @param flyer the flyer to set
	 */
	public void setFlyer(double flyer) {
		this.flyer = flyer;
	}
	/**
	 * @return the stand
	 */
	public double getStand() {
		return stand;
	}
	/**
	 * @param stand the stand to set
	 */
	public void setStand(double stand) {
		this.stand = stand;
	}
	/**
	 * @return the citySquare
	 */
	public double getCitySquare() {
		return citySquare;
	}
	/**
	 * @param citySquare the citySquare to set
	 */
	public void setCitySquare(double citySquare) {
		this.citySquare = citySquare;
	}
	public String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month-1];
	}

}
