package vu.co.kaiyin.sfreechart.plots

import java.awt.RenderingHints

import org.jfree.chart.axis.{AxisLocation, NumberAxis}
import org.jfree.chart.plot.{PlotOrientation, XYPlot}
import org.jfree.chart.renderer.xy.{StandardXYBarPainter, XYBarRenderer, XYBlockRenderer}
import org.jfree.chart.title.PaintScaleLegend
import org.jfree.chart.{ChartFactory => cf, _}
import org.jfree.data.Range
import org.jfree.data.general.DatasetUtilities
import org.jfree.data.xy.{IntervalXYDataset, XYZDataset}
import org.jfree.ui.{RectangleEdge, RectangleInsets}
import vu.co.kaiyin.sfreechart.ColorPaintScale

/**
  * Created by kaiyin on 2/10/16.
  */
object Plots {


  def histogram(
                 dataset: IntervalXYDataset,
                 title: String = "Histogram",
                 xAxisLabel: String = "Intervals",
                 yAxisLabel: String = "Count",
                 orientation: PlotOrientation = PlotOrientation.VERTICAL,
                 legend: Boolean = true,
                 tooltips: Boolean = true,
                 urls: Boolean = true,
                 alpha: Float = 0.5F,
                 outline: Boolean = true,
                 shadow: Boolean = false
               ): JFreeChart = {
    val hist = cf.createHistogram(
      title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls
    )
    val xyPlot = hist.getPlot.asInstanceOf[XYPlot]
    xyPlot.setForegroundAlpha(alpha)
    val renderer = xyPlot.getRenderer.asInstanceOf[XYBarRenderer]
    renderer.setDrawBarOutline(outline)
    renderer.setBarPainter(new StandardXYBarPainter())
    renderer.setShadowVisible(shadow)
    hist
  }

  def contourPlot(dataset: XYZDataset, title: String = "Contour plot", scaleTitle: String = "Scale"): JFreeChart = {
    val xAxis = new NumberAxis("x")
    val yAxis = new NumberAxis("y")
    val blockRenderer = new XYBlockRenderer
    val zBounds: Range = DatasetUtilities.findZBounds(dataset)
    println(zBounds.getLowerBound, zBounds.getUpperBound)
    val paintScale = new ColorPaintScale(zBounds.getLowerBound, zBounds.getUpperBound)
    blockRenderer.setPaintScale(paintScale)
    val xyPlot = new XYPlot(dataset, xAxis, yAxis, blockRenderer)
    xyPlot.setAxisOffset(new RectangleInsets(1D, 1D, 1D, 1D))
    xyPlot.setDomainPannable(true)
    xyPlot.setRangePannable(true)
    xyPlot.setDomainGridlinesVisible(false)
    xyPlot.setRangeGridlinesVisible(false)
    val chart = new JFreeChart(title, xyPlot)
    chart.removeLegend()
    val scaleAxis = new NumberAxis(scaleTitle)
    val paintScaleLegend = new PaintScaleLegend(paintScale, scaleAxis)
    paintScaleLegend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT)
    paintScaleLegend.setPosition(RectangleEdge.BOTTOM)
    chart.addSubtitle(paintScaleLegend)
    chart
  }


  def fastScatter(
                   data: Array[Array[Float]],
                   continuousColor: Boolean = true,
                   title: String = "Scatter plot",
                   scaleTitle: String = "scale",
                   pointSize: Int = 5,
                   pointAlpha: Float = 0.5F,
                   grid: (Boolean, Boolean) = (true, true)
                 ): JFreeChart = {
    val xAxis = new NumberAxis("x")
    val yAxis = new NumberAxis("y")
    xAxis.setAutoRangeIncludesZero(false)
    yAxis.setAutoRangeIncludesZero(false)
    val fsPlot = new EFastScatterPlot(data, continuousColor, xAxis, yAxis, scaleTitle, pointSize, pointAlpha)
    fsPlot.setDomainPannable(true)
    fsPlot.setRangePannable(true)
    fsPlot.setDomainGridlinesVisible(grid._1)
    fsPlot.setRangeGridlinesVisible(grid._2)
    val chart = new JFreeChart(title, fsPlot)
    if (fsPlot.paintScaleLegend != null) {
      chart.addSubtitle(fsPlot.paintScaleLegend)
    }
    if (fsPlot.colorLegend != null) {
      chart.addSubtitle(fsPlot.colorLegend)
    }
    chart.getRenderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    chart
  }

}

