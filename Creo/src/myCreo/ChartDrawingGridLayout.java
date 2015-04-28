package myCreo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JFrame;

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

public class ChartDrawingGridLayout {
	private int monthChosen;
	private String yearChosen;
	
	public ChartDrawingGridLayout(int monthCosen, String yearChosen){
		this.monthChosen = monthCosen;
		this.yearChosen = yearChosen;
		//ChartDrawingGridLayout CDGL = new ChartDrawingGridLayout();
	}
	public void draw() throws SQLException{
		// row keys...
        final String series1 = "Open";
        final String series2 = "Closed";
        final String series3 = "Unassigned";


		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(100, series1, "Open");
		dataset.setValue(50, series2, "Closed");
		dataset.setValue(80, series3, "Unassigned");
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
		 renderer1.setBaseItemLabelFont(new Font("Trajan Pro", Font.BOLD, 15));
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
			pieDataset.setValue("Wat", new Double(10));
			pieDataset.setValue("Wex", new Double(10));
			pieDataset.setValue("Gal", new Double(10));
			pieDataset.setValue("Dub", new Double(10));
			pieDataset.setValue("Cork", new Double(10));
			pieDataset.setValue("Kil", new Double(10));
			pieDataset.setValue("Car", new Double(10));
			pieDataset.setValue("Kildare", new Double(10));
			pieDataset.setValue("Ker", new Double(10));
			pieDataset.setValue("Lim", new Double(10));
			
			JFreeChart pieChart = ChartFactory.createPieChart3D("Jobs by county",pieDataset, false,
					true,false );
			PiePlot3D piePlot = (PiePlot3D) pieChart.getPlot();
			piePlot.setStartAngle(270);
			piePlot.setDirection(Rotation.ANTICLOCKWISE);
			piePlot.setForegroundAlpha(0.60f);
			piePlot.setBackgroundPaint(Color.WHITE);
			piePlot.setInteriorGap(0.04);
			piePlot.setOutlineVisible(false);
			piePlot.setSimpleLabels(true);
			//piePlot.setLabelGenerator(null);
			StandardPieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0},{2}");
			piePlot.setLabelGenerator(labelGenerator);

			piePlot.setLabelFont(new Font("Trajan Pro", Font.BOLD, 10));
			//piePlot.setLabelLinkPaint(Color.BLACK);
			//piePlot.setLabelLinkStroke(new BasicStroke(1.0f));
			piePlot.setLabelPaint(Color.BLACK);
			piePlot.setLabelOutlinePaint(null);
			piePlot.setLabelShadowPaint(null);
			piePlot.setLabelBackgroundPaint(null);

			//Graph chart setup
			final XYSeries lineSeries1 = new XYSeries("Open");
			lineSeries1.add(1.0, 1.0);
			lineSeries1.add(2.0, 4.0);
			lineSeries1.add(3.0, 3.0);

	        final XYSeries lineSeries2 = new XYSeries("Closed");
	        lineSeries2.add(1.0, 5.0);
	        lineSeries2.add(2.0, 7.0);
	        lineSeries2.add(3.0, 6.0);

	        final XYSeries lineSeries3 = new XYSeries("Unassigned");
	        lineSeries3.add(3.0, 4.0);
	        lineSeries3.add(4.0, 3.0);
	        lineSeries3.add(5.0, 2.0);

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
	        yAxis.setTickUnit(new NumberTickUnit(0.5));
	        
	        NumberAxis xAxis = (NumberAxis) linePlot.getDomainAxis();
	        xAxis.setLowerBound(0);
	        xAxis.setUpperBound(30);
	        
	        // Frame setup for grid layout
			JFrame frame = new JFrame("Chart");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setBackground(Color.WHITE);
			frame.setLayout(new GridLayout(2, 2));//rows, columns
			frame.add(new ChartPanel (pieChart));
			frame.add(new ChartPanel (pieChart));
			frame.add(new ChartPanel (pieChart));
			frame.add(new ChartPanel (pieChart));
			//frame.getContentPane().add(new ChartPanel(chart), BorderLayout.NORTH);
			//frame.getContentPane().add(new ChartPanel(pieChart), BorderLayout.EAST);
			//frame.getContentPane().add(new ChartPanel(chart), BorderLayout.SOUTH);
			//frame.getContentPane().add(new ChartPanel(chart), BorderLayout.WEST);
			frame.pack();
			frame.setVisible(true);
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setBounds(0, 0,screen.width,screen.height - 30);
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
