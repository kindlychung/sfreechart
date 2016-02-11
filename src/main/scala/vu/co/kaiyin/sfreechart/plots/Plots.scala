package vu.co.kaiyin.sfreechart.plots

import java.awt.{Shape, Stroke, RenderingHints}
import javax.swing.JFrame

import org.jfree.chart.plot.{PlotOrientation, XYPlot}
import org.jfree.chart.{ChartFactory => cf}
import org.jfree.chart.renderer.GrayPaintScale
import org.jfree.chart.renderer.xy.XYBlockRenderer
import org.jfree.chart.title.PaintScaleLegend
import org.jfree.chart._
import org.jfree.chart.axis.{AxisLocation, NumberAxis}
import org.jfree.data.Range
import org.jfree.data.general.DatasetUtilities
import org.jfree.data.statistics.HistogramDataset
import org.jfree.data.xy.{IntervalXYDataset, XYZDataset}
import org.jfree.ui.{RectangleEdge, RectangleInsets}
import vu.co.kaiyin.sfreechart.{ColorPaintScale, ExtendedFastScatterPlot}
import vu.co.kaiyin.sfreechart.implicits._
import scala.util.Random.nextGaussian

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
                 pannable: Boolean = false
               ): JFreeChart = {
    val hist = cf.createHistogram(
      title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls
    )
    val xyPlot = hist.getPlot.asInstanceOf[XYPlot]
    if (pannable) {
      xyPlot.setDomainPannable(true)
      xyPlot.setRangePannable(true)
    }
    xyPlot.setForegroundAlpha(alpha)
    hist
  }

  def controuPlot(dataset: XYZDataset, title: String = "Contour plot", scaleTitle: String = "Scale"): JFreeChart = {
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
    val chart = new JFreeChart(title, xyPlot)
    chart.removeLegend()
    val scaleAxis = new NumberAxis(scaleTitle)
    val paintScaleLegend = new PaintScaleLegend(paintScale, scaleAxis)
    paintScaleLegend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT)
    paintScaleLegend.setPosition(RectangleEdge.BOTTOM)
    chart.addSubtitle(paintScaleLegend)
    chart
  }


  def fastScatter(data: Array[Array[Float]], title: String = "Scatter plot", pointSize: Int = 5, pointAlpha: Float = 0.3F): JFreeChart = {
    val xAxis = new NumberAxis("x")
    val yAxis = new NumberAxis("y")
    xAxis.setAutoRangeIncludesZero(false)
    yAxis.setAutoRangeIncludesZero(false)
    val fsPlot = new ExtendedFastScatterPlot(data, xAxis, yAxis, pointSize, pointAlpha)
    fsPlot.setDomainPannable(true)
    fsPlot.setRangePannable(true)
    val chart = new JFreeChart(title, fsPlot)
    chart.getRenderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    chart
  }

  def main(args: Array[String]) {
    //    fastScatter(Array(Array(1.0F, 2.0F, 3.0F), Array(1.0F, 2.0F, 3.0F))).vis()
    val x = (1 to 10000).map(_.toFloat).toArray
    val y = x.map(i => i * nextGaussian().toFloat * 3F).toArray
    fastScatter(Array(x, y)).vis()
    val x1 = (-13.0 to 13.0 by 0.2).toArray
    val y1 = (-13.0 to 13.0 by 0.2).toArray
    val xyzData = (for {
      i <- x1
      j <- y1
      if i > j
    } yield Array(i, j, math.sin(i) + math.cos(j))).transpose
    controuPlot(xyzData.toXYZDataset()).vis()
    histogram((1 to 10000).map(_ => nextGaussian()).toArray.toHistogramDataset()).vis()
  }
}