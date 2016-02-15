package vu.co.kaiyin.sfreechart

import vu.co.kaiyin.sfreechart.implicits._
import vu.co.kaiyin.sfreechart.plots.Plots._

import scala.util.Random.nextGaussian

object SfreechartDemo {
  def main(args: Array[String]) {
    pieChart1(Array(
      ("c1", 43.1), ("c2", 23.5), ("c3", 54.7), ("c4", 12.4)
    ).toPieDataset).vis("Sample pie chart!")

    xyLine(Map(
      "first" -> Array((1.0, 1.0), (2.0, 4.0), (3.0, 9.0), (4.0, 16.0)),
      "second" -> Array((1.0, 0.5), (2.0, 1.0), (3.0, 1.5), (4.0, 2.0))
    ).toXYSeriesCollection).vis("Sample xy line chart!")

    def zipWith[A, B](xs: Seq[A])(f: A => B): Seq[(A, B)] = {
      val ys = xs.map(f)
      xs.zip(ys)
    }
    xyLine(
      zipWith((-10.0 to 10.0 by 0.01))(math.sin).toArray.toXYSeriesCollection(),
      title = "Sine",
      legend = false
    ).vis("y = sin x")

    lineChart(Array(
      (12.0, "Popularity", "jdk 1.0"),
      (4.0, "Popularity", "jdk 1.1"),
      (520.0, "Popularity", "jdk 1.2"),
      (804.0, "Popularity", "jdk 1.3"),
      (291.0, "Popularity", "jdk 1.4"),
      (212.0, "Classes", "jdk 1.0"),
      (504.0, "Classes", "jdk 1.1"),
      (1520.0, "Classes", "jdk 1.2"),
      (1804.0, "Classes", "jdk 1.3"),
      (2991.0, "Classes", "jdk 1.4")
    ).toCategoryDataset).vis()
    fastScatter(Array(
      (1 to 20).map(_ => nextGaussian().toFloat).toArray,
      (1 to 20).map(_ => nextGaussian().toFloat).toArray )
    ).vis();
    {
      val x = (1 to 10000).map(_.toFloat).toArray
      val y = x.map(i => i * nextGaussian().toFloat * 3F).toArray
      fastScatter(Array(x, y)).vis()
    }
    {
      val x = (-2.0 to 2.0 by 0.01).toArray
      val y = (-2.0 to 2.0 by 0.01).toArray
      val xyzData = (for {
        i <- x
        j <- y
      } yield {
        Array(i, j, i * math.exp(-math.pow(i, 2) - math.pow(j, 2)))
      }).transpose
      fastScatter(xyzData.toFloats, grid = (false, false), pointSize = 3, pointAlpha = 1F) vis()
    }
    {
      val x = (-12.0 to 12.0 by 0.1).toArray
      val y = (-12.0 to 12.0 by 0.1).toArray
      val xyzData = (for {
        i <- x
        j <- y
      } yield {
        val s = math.sin(i)
        val c = math.cos(j)
        Array(i, j, s + c)
      }).transpose
      contourPlot(xyzData.toXYZDataset()).vis()
      fastScatter(xyzData.toFloats, grid = (false, false), pointSize = 4, pointAlpha = 1F).vis()
    }
    histogram((1 to 10000).map(_ => nextGaussian()).toArray.toHistogramDataset(bins = 20)).vis();
    {
      // x coordinates
      val x = ((1 to 500) ++ (1 to 500)).map(_.toFloat).toArray
      // y coordinates, part a
      val ya = (1 to 500).map(i => i * nextGaussian().toFloat).toArray
      // y coordinates, part b, with higher mean
      val yb = (1 to 500).map(i => i * nextGaussian().toFloat + 1000F).toArray
      // the color array. here we have a categories, 1F for lower y values, 2F for higher y values.
      val c = Array.fill(500)(1F) ++ Array.fill(500)(2F)
      fastScatter(Array(x, ya ++ yb, c), false, pointAlpha = 0.8F, pointSize = 6).vis()
    }
  }
}
