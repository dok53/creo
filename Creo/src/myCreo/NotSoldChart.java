package myCreo;
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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import com.mysql.jdbc.PreparedStatement;

public class NotSoldChart{

	private JFrame frame;
	HashMap<String, Integer> prodTypePerCounty = new HashMap<String, Integer>();
	private ArrayList<String> reason = new ArrayList<String>();

	private int monthChosen;
	private String yearChosen;
	Connection connect;

	public NotSoldChart(int monthChosen, String yearChosen){
		this.monthChosen = monthChosen;
		this.yearChosen = yearChosen;
	}
	public void draw() throws Exception{
		CreoApp myCreo = new CreoApp();
		connect = DriverManager.getConnection("jdbc:mysql://" + myCreo.serverSettings[0] + ":3306/ampliogl_creoDB", myCreo.serverSettings[1], myCreo.serverSettings[2]);
		fillProductTypeNotSoldPerCounty(monthChosen, yearChosen);
		connect.close();

		// Pie chart setup
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		if (prodTypePerCounty.isEmpty()){
			pieDataset.setValue("No Values", 100);
		}
		else{ 
			int i = 0;
			for (Entry<String, Integer> entry : prodTypePerCounty.entrySet()){
				pieDataset.setValue(entry.getKey() + " " + reason.get(i), entry.getValue());
				i ++;
			}
		}

		JFreeChart pieChart = ChartFactory.createPieChart3D("Failed sales by county",pieDataset, true,
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

		frame = new JFrame("Failed Sales for the month of " + getMonth(monthChosen));
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
		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
		frame.setLayout(new GridBagLayout());     
		GridBagConstraints c = new GridBagConstraints(); 

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		pieChart = new JFreeChart("Failed sales by county for Month", piePlot);
		pieChart.removeLegend();
		pieChart.setBackgroundPaint(Color.WHITE);
		c.fill = GridBagConstraints.BOTH; 
		c.ipady = screen.height;
		c.weightx = 0.5;    
		c.gridx = 2;     
		c.gridy = 0;     
		frame.add(new ChartPanel (pieChart),c);
		frame.setVisible(true);
		frame.setBounds(0, 0,screen.width,screen.height - 30);
	}	
	// First method worked on
	public void fillProductTypeNotSoldPerCounty(int month, String year)
	{
		try {
			PreparedStatement selectStatement;
			selectStatement = (PreparedStatement) connect.prepareStatement ("select `Product Type`, `reason for closing`,count(*)from `jobs` WHERE `Status` = 'closed'  AND MONTH (`Primary Record`) = ('"+month+"')"
					+ " AND YEAR (`Primary Record`) = ('"+year+"') AND `Reason for closing` != 'sold' Group by `Product Type`");//AND `County` = ('"+county+"')
			ResultSet result = selectStatement.executeQuery();
			while (result.next())
			{
				prodTypePerCounty.put(result.getString(1),Integer.parseInt(result.getString(3)));
				reason.add(result.getString(2));

				//System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Cannot fill product type per county","Missing info",2);
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