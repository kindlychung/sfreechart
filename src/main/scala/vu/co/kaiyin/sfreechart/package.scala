package vu.co.kaiyin

import java.util.Locale

import apple.laf.JRSUIConstants.Orientation
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory => cf, JFreeChart}
import org.jfree.data.category.CategoryDataset
import org.jfree.data.general.PieDataset
import org.jfree.data.xy.{IntervalXYDataset, XYDataset}

import scala.math.Ordering.BooleanOrdering

/**
  * Created by kaiyin on 2/9/16.
  */
package object sfreechart {



  def xyLine(
              dataset: XYDataset,
              title: String = "XY line chart", xAxisLabel: String = "x", yAxisLabel: String = "y",
              orientation: PlotOrientation = PlotOrientation.VERTICAL, legend: Boolean = true,
              tooltips: Boolean = true, urls: Boolean = true
            ): JFreeChart = {
    cf.createXYLineChart(
      title, xAxisLabel, yAxisLabel,
      dataset, orientation, legend,
      tooltips, urls
    )
  }


  def lineChart(
                 dataset: CategoryDataset,
                 title: String = "Line chart",
                 categoryAxisLabel: String = "Category",
                 valueAxisLabel: String = "Value",
                 orientation: PlotOrientation = PlotOrientation.VERTICAL,
                 legend: Boolean = true, tooltips: Boolean = true, urls: Boolean = true
               ): JFreeChart = {
    cf.createLineChart(
      title, categoryAxisLabel, valueAxisLabel,
      dataset, orientation, legend, tooltips, urls
    )
  }

  def pieChart4(
                 dataset: PieDataset,
                 previousDataset: PieDataset,
                 title: String = "Pie chart",
                 percentDiffForMaxScale: Int = 20,
                 greenForIncrease: Boolean = true,
                 legend: Boolean = true,
                 tooltips: Boolean = true,
                 locale: Locale = new Locale("en", "US"),
                 subTitle: Boolean = true,
                 showDifference: Boolean = true
               ): JFreeChart = {
    cf.createPieChart(
      title, dataset, previousDataset, percentDiffForMaxScale,
      greenForIncrease, legend, tooltips, locale, subTitle, showDifference
    )
  }

  def pieChart3(
                 dataset: PieDataset,
                 previousDataset: PieDataset,
                 title: String = "Pie chart",
                 percentDiffForMaxScale: Int = 20,
                 greenForIncrease: Boolean = true,
                 legend: Boolean = true,
                 tooltips: Boolean = true,
                 urls: Boolean = true,
                 subTitle: Boolean = true,
                 showDifference: Boolean = true
               ): JFreeChart = {
    cf.createPieChart(
      title, dataset, previousDataset, percentDiffForMaxScale,
      greenForIncrease, legend, tooltips, urls, subTitle, showDifference
    )
  }

  def pieChart2(
                 dataset: PieDataset,
                 title: String = "Pie chart",
                 legend: Boolean = true,
                 tooltips: Boolean = true,
                 locale: Locale = new Locale("en", "US")
               ): JFreeChart = {
    cf.createPieChart(
      title, dataset, legend, tooltips, locale
    )
  }

  def pieChart1(
                 dataset: PieDataset,
                 title: String = "Pie chart",
                 legend: Boolean = true,
                 tooltips: Boolean = true,
                 urls: Boolean = true
               ): JFreeChart = {
    cf.createPieChart(
      title, dataset, legend, tooltips, urls
    )
  }

}
