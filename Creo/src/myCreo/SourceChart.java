package myCreo;
/**
 * @author derekok
 *
 */
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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import com.mysql.jdbc.PreparedStatement;

public class SourceChart{

	private double email;
	private double showroom;
	private double telephone;
	private JFrame frame;

	//Arrays for pie chart
	private ArrayList<String> source = new ArrayList<String>();
	private ArrayList<Integer> numOfSource = new ArrayList<Integer>();

	//Arrays for pie chart 2
	private ArrayList<String> productChart2 = new ArrayList<String>();
	private ArrayList<Integer> numOfProductsChart2 = new ArrayList<Integer>();
	private ArrayList<String> sourceChart2 = new ArrayList<String>();

	//Arrays for pie chart 3
	private ArrayList<String> countyChart = new ArrayList<String>();
	private ArrayList<String> sourceChart3 = new ArrayList<String>();
	private ArrayList<Integer> amount = new ArrayList<Integer>();


	private int monthChosen;
	private String yearChosen;
	private String county;
	
	Connection connect;

	public SourceChart(int monthChosen, String yearChosen, String county){
		this.monthChosen = monthChosen;
		this.yearChosen = yearChosen;
		this.county = county;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		fillProductsPerMonth(monthChosen, yearChosen);
		fillProdTypeBySource(monthChosen, yearChosen, county);
		fillProdTypeBySourceChart2(monthChosen, yearChosen, county);
		connect.close();
		/*for (int i = 0; i < counties.size(); i++){
			System.out.println("County : " + counties.get(i)+ " " + product.get(i)+ " " + numOfProducts.get(i));
		}*/

		// Pie chart 1 setup
		////////////////////////////////////////////////////////////////////////////////////////
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		if (source.isEmpty()){
			pieDataset.setValue("No Values", 100);
		}
		else{
			for (int i = 0; i < source.size(); i ++){
				pieDataset.setValue(source.get(i), numOfSource.get(i));
			}
		}
		JFreeChart pieChart = ChartFactory.createPieChart3D("Jobs by county",pieDataset, true,
				true,false );
		pieChart.removeLegend();
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
		////////////////////////////////////////////////////////////////////////////////////////

		// Pie chart 2 setup
		DefaultPieDataset pieDataset2 = new DefaultPieDataset();
		if (countyChart.isEmpty()){
			pieDataset2.setValue("No values", 100);
		}
		else{
			for (int i = 0; i < countyChart.size(); i++) {
				pieDataset2.setValue(countyChart.get(i)+ " " + sourceChart3.get(i), amount.get(i));
			}
		}

		JFreeChart pieChart2 = ChartFactory.createPieChart3D("Jobs by county",pieDataset2, true,
				true,false );
		PiePlot3D piePlot2 = (PiePlot3D) pieChart2.getPlot();
		piePlot2.setBackgroundPaint(null);
		piePlot2.setStartAngle(380);
		piePlot2.setDirection(Rotation.ANTICLOCKWISE);
		piePlot2.setForegroundAlpha(0.60f);
		piePlot2.setInteriorGap(0.04);
		piePlot2.setOutlineVisible(false);
		//piePlot2.setSimpleLabels(true);
		StandardPieSectionLabelGenerator labelGenerator2 = new StandardPieSectionLabelGenerator("{0},{2}");
		piePlot2.setLabelGenerator(labelGenerator2);
		piePlot2.setLabelFont(new Font("Times New Roman", Font.BOLD, 14));
		piePlot2.setLabelPaint(Color.BLACK);
		piePlot2.setLabelOutlinePaint(null);
		piePlot2.setLabelShadowPaint(null);
		piePlot2.setLabelBackgroundPaint(null);
		////////////////////////////////////////////////////////////////////////////////////////

		// Pie chart 3 setup
		DefaultPieDataset pieDataset3 = new DefaultPieDataset();
		if (productChart2.isEmpty()){
			pieDataset3.setValue("No Values", 100);
		}
		else{
			for (int i = 0; i < productChart2.size(); i ++){
				pieDataset3.setValue(productChart2.get(i)+ " " + sourceChart2.get(i), numOfProductsChart2.get(i));
			}
		}

		JFreeChart pieChart3 = ChartFactory.createPieChart3D("Jobs by county",pieDataset3, true,
				true,false );
		PiePlot3D piePlot3 = (PiePlot3D) pieChart3.getPlot();
		piePlot3.setBackgroundPaint(null);
		piePlot3.setStartAngle(270);
		piePlot3.setDirection(Rotation.ANTICLOCKWISE);
		piePlot3.setForegroundAlpha(0.60f);
		piePlot3.setInteriorGap(0.04);
		piePlot3.setOutlineVisible(false);
		//piePlot3.setSimpleLabels(true);
		StandardPieSectionLabelGenerator labelGenerator3 = new StandardPieSectionLabelGenerator("{0},{2}");
		piePlot3.setLabelGenerator(labelGenerator3);
		piePlot3.setLabelFont(new Font("Times New Roman", Font.BOLD, 14));
		piePlot3.setLabelPaint(Color.BLACK);
		piePlot3.setLabelOutlinePaint(null);
		piePlot3.setLabelShadowPaint(null);
		piePlot3.setLabelBackgroundPaint(null);
		////////////////////////////////////////////////////////////////////////////////////////

		frame = new JFrame("Sales for the month of " + getMonth(monthChosen));
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
					JOptionPane.showMessageDialog(null,"Cannot print source chart","",1);
					e1.printStackTrace();
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Cannot exit source chart","",1);
					e1.printStackTrace();
				}
			}
		});
		/////////////////////////////////////////
		frame.pack();
		frame.setVisible(true);
		frame.setLayout(new GridBagLayout());     
		GridBagConstraints c = new GridBagConstraints(); 

		pieChart = new JFreeChart("Products by source for month in county " + county, piePlot);
		pieChart.setBackgroundPaint(Color.WHITE);
		pieChart.removeLegend();
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.ipady = 350;
		c.weightx = 0.5;     
		c.gridx = 1;     
		c.gridy = 0;     
		frame.add(new ChartPanel (pieChart),c);

		pieChart2 = new JFreeChart("Products sold overall for month", piePlot2);
		pieChart2.setBackgroundPaint(Color.WHITE);
		pieChart2.removeLegend();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 350;
		c.weightx = 0.5;     
		c.gridx = 2;     
		c.gridy = 0;    
		frame.add(new ChartPanel (pieChart2),c);


		pieChart3 = new JFreeChart("Products sold in " + county + " for month", piePlot3);
		pieChart3.setBackgroundPaint(Color.WHITE);
		pieChart3.removeLegend();
		c.fill = GridBagConstraints.HORIZONTAL;     
		c.ipady = 335;      //make this component tall     
		c.weightx = 0.0; 
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridwidth = 3;     
		c.gridx = 0;     
		c.gridy = 1;     
		frame.add(new ChartPanel (pieChart3),c); 


		frame.setVisible(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,screen.height - 30);
	}

	public void fillProductsPerMonth(int month, String year) throws SQLException
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select `county`,`source`,count(*)from "
					+ "jobs WHERE MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `source`,`county`");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				countyChart.add(result.getString(1));
				sourceChart3.add(result.getString(2));
				amount.add(Integer.parseInt(result.getString(3)));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobStatus","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillProdTypeBySource(int month, String year, String county)
	{
		try {
			/*selectStatement = (PreparedStatement) myCreo.con.prepareStatement ("select `Product Type`,`source`,`county`,count(*)from "
					+ "jobs WHERE `County` = ('"+county+"') AND MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `Product Type`, `county`");*/
			
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select `source`,count(*)from "
					+ "jobs WHERE `County` = ('"+county+"') AND MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `source`");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				source.add(result.getString(1));
				numOfSource.add(Integer.parseInt(result.getString(2)));
				/*System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3)
						+ " " + Integer.parseInt(result.getString(4)));*/
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill product type by source (source)","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillProdTypeBySourceChart2(int month, String year, String county)
	{
		try {
			/*selectStatement = (PreparedStatement) myCreo.con.prepareStatement ("select `Product Type`,`source`,`county`,count(*)from "
					+ "jobs WHERE `County` = ('"+county+"') AND MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `Product Type`, `county`");*/
			
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select `Product Type`,`source`,count(*)from "
					+ "jobs WHERE `County` = ('"+county+"') AND MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') Group by `Product Type`");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				productChart2.add(result.getString(1));
				sourceChart2.add(result.getString(2));
				numOfProductsChart2.add(Integer.parseInt(result.getString(3)));
				/*System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3)
						+ " " + Integer.parseInt(result.getString(4)));*/
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill product type by source chart 2(source)","Missing info",2);
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
	 * @return the email
	 */
	public double getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(double email) {
		this.email = email;
	}
	/**
	 * @return the showroom
	 */
	public double getShowroom() {
		return showroom;
	}
	/**
	 * @param showroom the showroom to set
	 */
	public void setShowroom(double showroom) {
		this.showroom = showroom;
	}
	/**
	 * @return the telephone
	 */
	public double getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(double telephone) {
		this.telephone = telephone;
	}

}