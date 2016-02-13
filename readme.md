# sfreechart: Easy plotting in Scala

This is an abstraction layer over jFreeChart to afford scala users the ability to plot with ease. If you have used R before and find it appealing to visualize you data in the repl with one-liners, you might like this.

This project does not aim to provide publication quality charts, I see it as a convenient tool for the data scientist to have a quick look at their data without any ceremony (i.e. java verbosity). However, if you think there is a way to make the charts look better and produce better image files, feel free to talk about it in the issue tracker. 

This is also a work in progress, I only cover the chart types that I find most useful personally, hence many chart types (including those that are pretty trivial to wrap) are not directly supported here. 

## Include this library in your project

```
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.kindlychung" % "sfreechart" % "0.1"	
```


## Examples

Here is a demo app:


```
package vu.co.kaiyin.sfreechart

import vu.co.kaiyin.sfreechart.implicits._
import vu.co.kaiyin.sfreechart.plots.Plots._
import util.Random.nextGaussian

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
```

And you will see the following results:

![pie](http://i.imgur.com/5kGaKCF.png)

![xy line chart, two lines](http://i.imgur.com/qScTz3z.png)

![xy line chart, sin](http://i.imgur.com/YFJ0A3R.png)

![category line chart](http://i.imgur.com/FdHxPPG.png)

![scatter plot, normal random](http://i.imgur.com/WDqQm4y.png)

![scatter plot, normal random noise](http://i.imgur.com/7IuMypm.png)

![fake contour, exp](http://i.imgur.com/uWwm0yL.png)

![contour plot](http://i.imgur.com/KipnYi5.png)

![fake contour plot using fast scatter, sin + cos](http://i.imgur.com/vWkBuR5.png)

![histogram](http://i.imgur.com/bftKafm.png)

![scatter plot two color groups](http://i.imgur.com/Xuz74Dt.png)












## Contributing

There are many ways to contribute:

* Documentation.
* Write wrappers for more chart types. Please have a look at `src/main/scala/vu/co/kaiyin/sfreechart/package.scala`.
* Write converters for more data structures, for example, spark data frames or data sets. Please have a look at `src/main/scala/vu/co/kaiyin/sfreechart/implicits/package.scala`
