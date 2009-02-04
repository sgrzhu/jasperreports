/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.chartthemes.simple;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRChart;

import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.ui.Align;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.UnitType;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ChartThemesApp.java 2548 2009-01-27 12:44:51Z teodord $
 */
public class SimpleSettingsFactory
{

	public static final Color COLOR_0 = new Color(250, 97, 18);
	public static final Color COLOR_1 = new Color(237, 38, 42);
	public static final Color COLOR_2 = new Color(0, 111, 60);
	public static final Color COLOR_3 = new Color(250, 223, 18);
	public static final Color COLOR_4 = new Color(47, 137, 187);
	public static final Color COLOR_5 = new Color(231, 133, 35);
	public static final Color COLOR_6 = new Color(229, 1, 140);
	public static final Color COLOR_7 = new Color(234, 171, 53);

	public static final List COLORS =	new ArrayList(){{
		add(new ColorProvider(COLOR_0));
		add(new ColorProvider(COLOR_1));
		add(new ColorProvider(COLOR_2));
		add(new ColorProvider(COLOR_3));
		add(new ColorProvider(COLOR_4));
		add(new ColorProvider(COLOR_5));
		add(new ColorProvider(COLOR_6));
		add(new ColorProvider(COLOR_7));
		}};
		
		public static final List COLORS_DARKER =	new ArrayList(){{
			add(new ColorProvider(COLOR_0.darker()));
			add(new ColorProvider(COLOR_1.darker()));
			add(new ColorProvider(COLOR_2.darker()));
			add(new ColorProvider(COLOR_3.darker()));
			add(new ColorProvider(COLOR_4.darker()));
			add(new ColorProvider(COLOR_5.darker()));
			add(new ColorProvider(COLOR_6.darker()));
			add(new ColorProvider(COLOR_7.darker()));
			}};
		
	public static final List GRADIENT_PAINTS = new ArrayList(){{
		add(new GradientPaintProvider(0, 0, COLOR_0, 0, 0, COLOR_0.darker()));
		add(new GradientPaintProvider(0, 0, COLOR_1, 0, 0, COLOR_1.darker()));
		add(new GradientPaintProvider(0, 0, COLOR_2, 0, 0, COLOR_2.darker()));
		add(new GradientPaintProvider(0, 0, COLOR_3, 0, 0, COLOR_3.darker()));
		add(new GradientPaintProvider(0, 0, COLOR_4, 0, 0, COLOR_4.darker()));
		add(new GradientPaintProvider(0, 0, COLOR_5, 0, 0, COLOR_5.darker()));
		add(new GradientPaintProvider(0, 0, COLOR_6, 0, 0, COLOR_6.darker()));
		add(new GradientPaintProvider(0, 0, COLOR_7, 0, 0, COLOR_7.darker()));
    }};

	public static final List STROKES =	new ArrayList(){{
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		}};
	
	public static final List OUTLINE_STROKES =	new ArrayList(){{
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		}};
	
    
	/**
	 *
	 */
	public static final ChartThemeSettings createChartThemeSettings()
	{
		ChartThemeSettings settings = new ChartThemeSettings();
		

		ChartSettings chartSettings = settings.getChartSettings();
		chartSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		chartSettings.setBackgroundImage(new FileImageProvider("jasperreports.gif"));
		chartSettings.setBackgroundImageAlignment(new Integer(Align.TOP_RIGHT));
		chartSettings.setBackgroundImageAlpha(new Float(1f));
		chartSettings.getFont().setBold(Boolean.TRUE);
		chartSettings.setBorderVisible(Boolean.TRUE);
		chartSettings.setBorderPaint(new ColorProvider(Color.GREEN));
		chartSettings.setBorderStroke(new BasicStroke(1f));
		chartSettings.setAntiAlias(Boolean.TRUE);
		chartSettings.setTextAntiAlias(Boolean.TRUE);
		chartSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));

		TitleSettings titleSettings = settings.getTitleSettings();
		titleSettings.setShowTitle(Boolean.TRUE);
		titleSettings.setPosition(new Byte(JRChart.EDGE_TOP));
		titleSettings.setForegroundPaint(new ColorProvider(Color.black));
		titleSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		titleSettings.getFont().setBold(Boolean.TRUE);
		titleSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		titleSettings.setVerticalAlignment(VerticalAlignment.TOP);
		titleSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));
		
		TitleSettings subtitleSettings = settings.getSubtitleSettings();
		subtitleSettings.setShowTitle(Boolean.TRUE);
		subtitleSettings.setPosition(new Byte(JRChart.EDGE_TOP));
		subtitleSettings.setForegroundPaint(new ColorProvider(Color.red));
		subtitleSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		subtitleSettings.getFont().setBold(Boolean.TRUE);
		subtitleSettings.setHorizontalAlignment(HorizontalAlignment.LEFT);
		subtitleSettings.setVerticalAlignment(VerticalAlignment.CENTER);
		subtitleSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));

		LegendSettings legendSettings = settings.getLegendSettings();
		legendSettings.setShowLegend(Boolean.TRUE);
		legendSettings.setPosition(new Byte(JRChart.EDGE_BOTTOM));
		legendSettings.setForegroundPaint(new ColorProvider(Color.black));
		legendSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		legendSettings.getFont().setBold(Boolean.TRUE);
		legendSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		legendSettings.setVerticalAlignment(VerticalAlignment.BOTTOM);
		//FIXMETHEME legendSettings.setBlockFrame();
		legendSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));
		
		PlotSettings plotSettings = settings.getPlotSettings();
		plotSettings.setOrientation(PlotOrientation.VERTICAL);
//		plotSettings.setForegroundAlpha(new Float(0.5f));
		plotSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
//		plotSettings.setBackgroundAlpha(new Float(0.5f));
		plotSettings.setBackgroundImage(new FileImageProvider("jasperreports.gif"));
		plotSettings.setBackgroundImageAlpha(new Float(0.5f));
		plotSettings.setBackgroundImageAlignment(new Integer(Align.NORTH_WEST));
		plotSettings.setLabelRotation(new Double(0));
		plotSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));
		plotSettings.setOutlineVisible(Boolean.TRUE);
		plotSettings.setOutlinePaint(new ColorProvider(Color.red));
		plotSettings.setOutlineStroke(new BasicStroke(1f));
		plotSettings.setSeriesColorSequence(COLORS);
//		plotSettings.setSeriesGradientPaintSequence(GRADIENT_PAINTS);
		plotSettings.setSeriesOutlinePaintSequence(COLORS_DARKER);
		plotSettings.setSeriesStrokeSequence(STROKES);
		plotSettings.setSeriesOutlineStrokeSequence(OUTLINE_STROKES);
		plotSettings.setDomainGridlineVisible(Boolean.TRUE);
		plotSettings.setDomainGridlinePaint(new ColorProvider(Color.DARK_GRAY));
		plotSettings.setDomainGridlineStroke(new BasicStroke(0.5f));
		plotSettings.setRangeGridlineVisible(Boolean.TRUE);
		plotSettings.setRangeGridlinePaint(new ColorProvider(Color.BLACK));
		plotSettings.setRangeGridlineStroke(new BasicStroke(0.5f));
		
		AxisSettings domainAxisSettings = settings.getDomainAxisSettings();
		domainAxisSettings.setVisible(Boolean.TRUE);
		domainAxisSettings.setLocation(AxisLocation.BOTTOM_OR_RIGHT);
		domainAxisSettings.setLinePaint(new ColorProvider(Color.green));
		domainAxisSettings.setLineStroke(new BasicStroke(1f));
		domainAxisSettings.setLineVisible(Boolean.TRUE);
		domainAxisSettings.setLabel("Domain Axis");
		domainAxisSettings.setLabelAngle(new Double(0.0));
		domainAxisSettings.setLabelPaint(new ColorProvider(Color.magenta));
		domainAxisSettings.getLabelFont().setBold(Boolean.TRUE);
		domainAxisSettings.getLabelFont().setItalic(Boolean.TRUE);
		domainAxisSettings.getLabelFont().setFontName("Comic Sans MS");
		domainAxisSettings.getLabelFont().setFontSize(12);
		domainAxisSettings.setLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 1, 1));
		domainAxisSettings.setLabelVisible(Boolean.TRUE);
		domainAxisSettings.setTickLabelPaint(new ColorProvider(Color.cyan));
		domainAxisSettings.getTickLabelFont().setBold(Boolean.TRUE);
		domainAxisSettings.getTickLabelFont().setItalic(Boolean.FALSE);
		domainAxisSettings.getTickLabelFont().setFontName("Arial");
		domainAxisSettings.getTickLabelFont().setFontSize(10);
		domainAxisSettings.setTickLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 0.5, 0.5));
		domainAxisSettings.setTickLabelsVisible(Boolean.TRUE);
		domainAxisSettings.setTickMarksInsideLength(new Float(0.1f));
		domainAxisSettings.setTickMarksOutsideLength(new Float(0.2f));
		domainAxisSettings.setTickMarksPaint(new ColorProvider(Color.ORANGE));
		domainAxisSettings.setTickMarksStroke(new BasicStroke(1f));
		domainAxisSettings.setTickMarksVisible(Boolean.TRUE);
		domainAxisSettings.setTickCount(new Integer(5));

		
		AxisSettings rangeAxisSettings = settings.getRangeAxisSettings();
		rangeAxisSettings.setVisible(Boolean.TRUE);
		rangeAxisSettings.setLocation(AxisLocation.TOP_OR_RIGHT);
		rangeAxisSettings.setLinePaint(new ColorProvider(Color.yellow));
		rangeAxisSettings.setLineStroke(new BasicStroke(1f));
		rangeAxisSettings.setLineVisible(Boolean.TRUE);
		rangeAxisSettings.setLabel("Range Axis");
		rangeAxisSettings.setLabelAngle(new Double(Math.PI/2.0));
		rangeAxisSettings.setLabelPaint(new ColorProvider(Color.green));
		rangeAxisSettings.getLabelFont().setBold(Boolean.TRUE);
		rangeAxisSettings.getLabelFont().setItalic(Boolean.TRUE);
		rangeAxisSettings.getLabelFont().setFontName("Comic Sans MS");
		rangeAxisSettings.getLabelFont().setFontSize(12);
		rangeAxisSettings.setLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 1, 1));
		rangeAxisSettings.setLabelVisible(Boolean.TRUE);
		rangeAxisSettings.setTickLabelPaint(new ColorProvider(Color.pink));
		rangeAxisSettings.getTickLabelFont().setBold(Boolean.FALSE);
		rangeAxisSettings.getTickLabelFont().setItalic(Boolean.TRUE);
		rangeAxisSettings.getTickLabelFont().setFontName("Arial");
		rangeAxisSettings.getTickLabelFont().setFontSize(10);
		rangeAxisSettings.setTickLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 0.5, 0.5));
		rangeAxisSettings.setTickLabelsVisible(Boolean.TRUE);
		rangeAxisSettings.setTickMarksInsideLength(new Float(0.2f));
		rangeAxisSettings.setTickMarksOutsideLength(new Float(0.1f));
		rangeAxisSettings.setTickMarksPaint(new ColorProvider(Color.black));
		rangeAxisSettings.setTickMarksStroke(new BasicStroke(1f));
		rangeAxisSettings.setTickMarksVisible(Boolean.TRUE);
		rangeAxisSettings.setTickCount(new Integer(6));
		
		return settings;
	}
}
