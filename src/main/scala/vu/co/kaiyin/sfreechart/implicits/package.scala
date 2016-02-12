package vu.co.kaiyin.sfreechart

import javax.swing.JFrame

import org.jfree.chart.{ChartPanel, ChartFrame, JFreeChart}
import org.jfree.data.category.{DefaultCategoryDataset, CategoryDataset}
import org.jfree.data.general.{DefaultPieDataset, PieDataset}
import org.jfree.data.statistics.HistogramDataset
import org.jfree.data.xy.{DefaultXYZDataset, XYZDataset, XYSeriesCollection, XYSeries}


/**
  * Created by kaiyin on 2/10/16.
  */
package object implicits {

  private def arrayTupleToSeries(seq: Array[(Double, Double)], seriesTitle: String): XYSeries = {
    val s = new XYSeries(seriesTitle)
    seq.foreach {
      x => s.add(x._1, x._2)
    }
    s
  }

  implicit class ArrayTupleHelper(a: Array[(Double, Double)]) {
    def toXYSeriesCollection(seriesName: String = "series"): XYSeriesCollection = {
      val res = new XYSeriesCollection()
      res.addSeries(arrayTupleToSeries(a, seriesName))
      res
    }
  }

  implicit class MATHelper(m: Map[String, Array[(Double, Double)]]) {
    def toXYSeriesCollection: XYSeriesCollection = {
      val res = new XYSeriesCollection()
      m.foreach {
        case (seriesTitle, seq) => {
          val s: XYSeries = arrayTupleToSeries(seq, seriesTitle)
          seq.foreach {
            x => s.add(x._1, x._2)
          }
          res.addSeries(s)
        }
      }
      res
    }
  }

  implicit class ASDHelper(a: Array[(String, Double)]) {
    def toPieDataset: PieDataset = {
      val res = new DefaultPieDataset()
      a.foreach {
        x => res.setValue(x._1, x._2)
      }
      res
    }
  }

  implicit class ADSSHelper(a: Array[(Double, String, String)]) {
    def toCategoryDataset: CategoryDataset = {
      val res = new DefaultCategoryDataset
      a.foreach {
        case (value, seriesName, categoryName) => res.addValue(value, seriesName, categoryName)
      }
      res
    }
  }


  implicit class ChartHelper(chart: JFreeChart) {
    def vis(frameTitle: String = "Chart"): JFrame = {
      val panel = new ChartPanel(chart, true)
      panel.setDomainZoomable(true)
      panel.setRangeZoomable(true)
      panel.setMouseWheelEnabled(true)
      val frame = new JFrame()
      frame.setContentPane(panel)
      frame.pack()
      frame.setVisible(true)
      frame
    }
  }

  implicit class ArrayJFreeHelper(a: Array[Double]) {
    def toHistogramDataset(seriesName: String = "H1", bins: Int = 10) = {
      val histData = new HistogramDataset()
      val (minimum, maximum) = minMax(a)
      histData.addSeries(seriesName, a, bins, minimum, maximum)
      histData
    }
  }

  implicit class Array2DJFreeHelper(a: Array[Array[Double]]) {
    def toFloats = {
      a.map(x => x.map(_.toFloat))
    }

    def toXYZDataset(seriesName: String = "first") = {
      // todo: check size
//      checkMatrixSize(a, )
      val res = new DefaultXYZDataset()
      res.addSeries(seriesName, a)
      res
    }
  }


  def checkMatrixSize[T](a: Array[Array[T]], nRow: Int, nCol: Int): Unit = {
    val sizes = a.map(_.length).toSet
    require(
      a.length == nRow &&
        sizes.size == 1 &&
        sizes.apply(0) == nCol
    )
  }
  // todo: check only row number
  // todo: check only col number
//  def checkMatrixSize[T](a: Array[Array[T]], ): Unit = {
//    val sizes = a.map(_.length).toSet
//    require(
//      a.length == nRow &&
//        sizes.size == 1 &&
//        sizes.apply(0) == nCol
//    )
//  }

}
