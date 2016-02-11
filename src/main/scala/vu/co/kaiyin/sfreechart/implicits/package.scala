package vu.co.kaiyin.sfreechart

import javax.swing.JFrame

import org.jfree.chart.{ChartPanel, ChartFrame, JFreeChart}
import org.jfree.data.category.{DefaultCategoryDataset, CategoryDataset}
import org.jfree.data.general.{DefaultPieDataset, PieDataset}
import org.jfree.data.statistics.HistogramDataset
import org.jfree.data.xy.{DefaultXYZDataset, XYZDataset, XYSeriesCollection, XYSeries}
import scala.language.implicitConversions


/**
  * Created by kaiyin on 2/10/16.
  */
package object implicits {

  private def seqTupleToSeries(seq: Seq[(Double, Double)], seriesTitle: String): XYSeries = {
    val s = new XYSeries(seriesTitle)
    seq.foreach {
      x => s.add(x._1, x._2)
    }
    s
  }

  implicit def seqTupleToSeries1(seq: Seq[(Double, Double)]): XYSeries = {
    seqTupleToSeries(seq, java.util.UUID.randomUUID().toString)
  }


  implicit def mapSeqTupleToSeriesCollection(m: Map[String, Seq[(Double, Double)]]): XYSeriesCollection = {
    val res = new XYSeriesCollection()
    m.foreach {
      case (seriesTitle, seq) => {
        val s: XYSeries = seqTupleToSeries(seq, seriesTitle)
        seq.foreach {
          x => s.add(x._1, x._2)
        }
        res.addSeries(s)
      }
    }
    res
  }


  implicit def seqTupleToSeriesCollection(seq: Seq[(Double, Double)]): XYSeriesCollection = {
    val res = new XYSeriesCollection()
    res.addSeries(seq)
    res
  }

  implicit def seqTupleToPieData(seq: Seq[(String, Double)]): PieDataset = {
    val res = new DefaultPieDataset()
    seq.foreach {
      x => res.setValue(x._1, x._2)
    }
    res
  }

  implicit def seqTupleToCategoryData(seq: Seq[(Double, String, String)]): CategoryDataset = {
    val res = new DefaultCategoryDataset
    seq.foreach {
      case (value, seriesName, categoryName) => res.addValue(value, seriesName, categoryName)
    }
    res
  }


  implicit class ChartToFrame(chart: JFreeChart) {
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
    def minMax = {
      a.foldLeft((a(0), a(0))) {
        (acc, x) => (math.min(acc._1, x), math.max(acc._2, x))
      }
    }

    def toHistogramDataset(seriesName: String = "H1", bins: Int = 10) = {
      val histData = new HistogramDataset()
      val (minimum, maximum) = a.minMax
      histData.addSeries(seriesName, a, bins, minimum, maximum)
      histData
    }
  }

  implicit class Array2DJFreeHelper(a: Array[Array[Double]]) {
    def toFloats = {
      a.map(x => x.map(_.toFloat))
    }

    def toXYZDataset(seriesName: String = "first") = {
      require(a.length == 3)
      val sizes = a.map(_.length).toSet
      require(sizes.size == 1)
      val res = new DefaultXYZDataset()
      res.addSeries(seriesName, a)
      res
    }
  }


}
