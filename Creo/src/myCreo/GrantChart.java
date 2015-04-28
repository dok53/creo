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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import com.mysql.jdbc.PreparedStatement;

public class GrantChart{

	private JFrame frame;

	HashMap<String, Integer> openProdType = new HashMap<String, Integer>();
	HashMap<String, Integer> closedProdType = new HashMap<String, Integer>();

	private int monthChosen;
	private String yearChosen;
	Connection connect;

	public GrantChart(int monthChosen, String yearChosen){
		this.monthChosen = monthChosen;
		this.yearChosen = yearChosen;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		fillOpenProdType(monthChosen, yearChosen);
		fillClosedProdType(monthChosen, yearChosen);
		connect.close();
		
		// Pie chart 1 setup
		////////////////////////////////////////////////////////////////////////////////////////
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		if (openProdType.isEmpty()){
			pieDataset.setValue("No Values", 100);
		}
		else{
			for (Entry<String, Integer> entry : openProdType.entrySet()){
				pieDataset.setValue(entry.getKey(), entry.getValue());
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
		if (closedProdType.isEmpty()){
			pieDataset2.setValue("No values", 100);
		}
		else{
			for (Entry<String, Integer> entry : closedProdType.entrySet()){
				pieDataset2.setValue(entry.getKey(), entry.getValue());
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

		frame = new JFrame("Grant stats for the month of " + getMonth(monthChosen));
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
		/////////////////////////////////////////
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.pack();
		frame.setVisible(true);
		frame.setLayout(new GridBagLayout());     
		GridBagConstraints c = new GridBagConstraints(); 

		pieChart = new JFreeChart("Grant sales", piePlot);
		pieChart.setBackgroundPaint(Color.WHITE);
		pieChart.removeLegend();
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.ipady = screen.height - 70;
		c.weightx = 0.5;     
		c.gridx = 1;     
		c.gridy = 0;     
		frame.add(new ChartPanel (pieChart),c);

		pieChart2 = new JFreeChart("Grant loss", piePlot2);
		pieChart2.setBackgroundPaint(Color.WHITE);
		pieChart2.removeLegend();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = screen.height - 70;
		c.weightx = 0.5;     
		c.gridx = 2;     
		c.gridy = 0;    
		frame.add(new ChartPanel (pieChart2),c);
 


		frame.setVisible(true);
		frame.setBounds(0, 0,screen.width,screen.height - 30);
	}

	public void fillOpenProdType(int month, String year) throws SQLException
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select `Product Type`,count(*)from jobs WHERE MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') AND `Referral` = 'grant' AND `Reason for closing` = 'not closed' Group by `Product Type`");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				openProdType.put(result.getString(1), Integer.parseInt(result.getString(2)));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobStatus","Missing info",2);
			e.printStackTrace();
		}
	}
	public void fillClosedProdType(int month, String year) throws SQLException
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select `Product Type`,count(*)from jobs WHERE MONTH (`Primary Record`) = ('"+month+"') AND YEAR (`Primary Record`) = ('"+year+"') AND `Referral` = 'grant' AND `Reason for closing` != 'not closed' Group by `Product Type`");
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				closedProdType.put(result.getString(1), Integer.parseInt(result.getString(2)));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill jobStatus","Missing info",2);
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
	
}