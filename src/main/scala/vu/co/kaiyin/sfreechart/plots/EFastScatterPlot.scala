package vu.co.kaiyin.sfreechart.plots

import java.awt._
import java.awt.geom.Rectangle2D
import org.jcolorbrewer.ColorBrewer
import org.jfree.chart.{LegendItemSource, LegendItem, LegendItemCollection}
import org.jfree.chart.axis.{AxisLocation, AxisLabelLocation, NumberAxis}
import org.jfree.chart.plot.CrosshairState
import org.jfree.chart.plot.FastScatterPlot
import org.jfree.chart.plot.PlotRenderingInfo
import org.jfree.chart.title.{LegendTitle, PaintScaleLegend}
import org.jfree.ui.RectangleEdge
import vu.co.kaiyin.sfreechart.{ColorPaintScale, InterpolateUtils, minMax}

/**
  * Created by kaiyin on 2/11/16.
  */
class EFastScatterPlot(
                        data: Array[Array[Float]],
                        continuousColor: Boolean = true,
                        domainAxis: NumberAxis,
                        rangeAxis: NumberAxis,
                        scaleTitle: String = "scale",
                        pointSize: Int,
                        private var alpha: Float = 0.5F,
                        colorBlindSave: Boolean = false
                      ) extends FastScatterPlot(data, domainAxis, rangeAxis) {

  alpha = if (alpha > 1F) 1F else alpha
  val xs = data(0)
  val ys = data(1)
  require((data.length == 2 || data.length == 3) &&
    xs.length == ys.length)
  val colors = if (data.length > 2) {
    require(data(2).length == xs.length)
    Some(data(2))
  } else None

  private[this] var _paintScaleLegend: PaintScaleLegend = null

  def paintScaleLegend: PaintScaleLegend = _paintScaleLegend

  private def paintScaleLegend_=(value: PaintScaleLegend): Unit = {
    _paintScaleLegend = value
  }

  private[this] var _colorLegend: LegendTitle = null

  def colorLegend: LegendTitle = _colorLegend

  private[this] def colorLegend_=(value: LegendTitle): Unit = {
    _colorLegend = value
  }


  val colorBrewer = ColorBrewer.getDivergingColorPalettes(colorBlindSave)(0)
  val getColor = colors match {
    case Some(cs) => {
      val scaleAxis = new NumberAxis(scaleTitle)
      if (continuousColor) {
        val palette: Array[Color] = colorBrewer.getColorPalette(2)
        //        val palette: Array[Color] = Array(new Color(0, 0, 255, 255), new Color(255, 1, 1, 255))
        val (cMin, cMax) = minMax(cs)
        val cRange = cMax - cMin
        val denominator = cRange + {
          if(cRange == 0) 0.0001 else 0
        }
        val paintScale = new ColorPaintScale(cMin, cMax, palette(0), palette(1))
        paintScaleLegend = new PaintScaleLegend(paintScale, scaleAxis)
        paintScaleLegend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT)
        paintScaleLegend.setPosition(RectangleEdge.BOTTOM)
        (x: Float) =>
          InterpolateUtils.colorInterpolate(palette(0), palette(1), ((x - cMin) / denominator).toDouble)
      } else {
        val colorSet = cs.toSet.toArray
        val nColor = colorSet.length
        require(nColor < 10)
        val paletteMap: Map[Float, Color] = colorSet.zip(colorBrewer.getColorPalette(nColor)).toMap
        val lic = new LegendItemCollection
        paletteMap.foreach {
          x => lic.add(new LegendItem(x._1.toString, x._2))
        }
        colorLegend = new LegendTitle(new LegendItemSource {
          override def getLegendItems: LegendItemCollection = lic
        })
        (x: Float) => paletteMap(x)
      }
    }
    case _ => (_: Float) => Color.black
  }


  override def render(g2: Graphics2D, dataArea: Rectangle2D, info: PlotRenderingInfo, crosshairState: CrosshairState): Unit = {
    g2.setPaint(Color.black)
    val comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
    g2.setComposite(comp)
    colors match {
      case None => {
        xs.zip(ys).foreach {
          case (x, y) => {
            drawPoints(g2, dataArea, x, y)
          }
        }
      }
      case Some(cs) => {
        val (cMin, cMax) = minMax(cs)
        xs.zip(ys).zip(cs).foreach {
          case ((x, y), c) => {
            drawPoints(g2, dataArea, x, y, getColor(c))
          }
        }
      }
    }
  }

  def drawPoints(g2: Graphics2D, dataArea: Rectangle2D, x: Float, y: Float, color: Color): Unit = {
    val transX = this.getDomainAxis.valueToJava2D(x, dataArea, RectangleEdge.BOTTOM).asInstanceOf[Int]
    val transY = this.getRangeAxis.valueToJava2D(y, dataArea, RectangleEdge.LEFT).asInstanceOf[Int]
    // todo: change paint color
    g2.setPaint(color)
    g2.fillOval(transX, transY, pointSize, pointSize)
  }

  def drawPoints(g2: Graphics2D, dataArea: Rectangle2D, x: Float, y: Float): Unit = {
    val transX = this.getDomainAxis.valueToJava2D(x, dataArea, RectangleEdge.BOTTOM).asInstanceOf[Int]
    val transY = this.getRangeAxis.valueToJava2D(y, dataArea, RectangleEdge.LEFT).asInstanceOf[Int]
    g2.fillOval(transX, transY, pointSize, pointSize)
  }

}

