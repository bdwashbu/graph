package controllers

import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import java.io.File, java.io.FilenameFilter
import java.util.Arrays
import scala.collection.mutable.ListBuffer
import play.api.data._
import play.api.data.Forms._
import play.api.{Routes}
import play.api._
import play.api.mvc._
import play.api.data._
import views.html._
import models._
import scala.io.Source._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap

object Application extends Controller {

  def main = Action {
    Ok(views.html.main("test"))
  }
  
  case class GraphData(var pressureAlt: Double,
                       var radioAltitude: Double,
                       var gpsLatitude: Double,
                       var gpsLongitude: Double,
                       var ilsLocation: Double,
                       var groundSpeed: Double,
                       var gpsAltitude: Double,
                       var trackAngle: Double,
                       var gps_vs_fpm: Double,
                       var verticalSpeed: Double)
                       
  def getGraphData(fileName: String, setString: String) = Action { implicit request =>
    val lines = fromFile("C:\\Scala\\Git\\graph\\app\\assets\\mops\\" + fileName).getLines
    var results = ListBuffer[GraphData]()
    val commands = lines.flatMap(_.split(",").map(_.trim)).filter(_.contains(')'))
    var timerCounter = 0
    
    var currentInfo = GraphData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    
    for (command <- commands) {
      //println("WHAAAAAT " + command)
      var endIndex = 0
      var endFound = false
      for (x <- command) if (endFound == false) {
         endIndex += 1
         if (command.charAt(endIndex) == ')')
           endFound = true
      }
      try {
        if (command.size > 8) {
          if (command.contains("Set_palt")) {
              currentInfo.pressureAlt = command.subSequence(9, endIndex).toString.toFloat
          } else if (command.contains("Set_ralt")) {
              currentInfo.radioAltitude = command.subSequence(9, endIndex).toString.toFloat
          } else if (command.contains("Set_gps.pos.lat")) {
              currentInfo.gpsLatitude = command.subSequence(16, endIndex).toString.toFloat
          } else if (command.contains("Set_gps.pos.lon")) {
              currentInfo.gpsLongitude = command.subSequence(16, endIndex).toString.toFloat
          } else if (command.contains("Set_ils.loc")) {
              currentInfo.ilsLocation = command.subSequence(12, endIndex).toString.toFloat
          } else if (command.contains("Set_gps.gs_knts")) {
              currentInfo.groundSpeed = command.subSequence(16, endIndex).toString.toFloat
          } else if (command.contains("Set_gps.gpsalt")) {
              currentInfo.gpsAltitude = command.subSequence(15, endIndex).toString.toFloat
          } else if (command.contains("Set_gps.trk")) {
              currentInfo.trackAngle = command.subSequence(12, endIndex).toString.toFloat
          } else if (command.contains("Set_gps.gps_vs_fpm")) {
              currentInfo.gps_vs_fpm = command.subSequence(19, endIndex).toString.toFloat
          } else if (command.contains("Set_bvs")) {
              currentInfo.verticalSpeed = command.subSequence(8, endIndex).toString.toFloat
          } else if (command.contains("Set_gps.meas_time")) {
            results += currentInfo.copy()
            timerCounter += 1
          }
       } 
      } catch {
           case ioe: NumberFormatException => println("BROKEN " + command)
         }
    }
    
     val finalResult = setString match {
       case "Set_palt" =>
            results.map(_.pressureAlt).foldLeft("")(_ + ", " + _)
        case "Set_ralt" =>
            results.map(_.radioAltitude).foldLeft("")(_ + ", " + _)
        case "Set_gps.pos.lat" =>
            results.map(_.gpsLatitude).foldLeft("")(_ + ", " + _)
        case "Set_gps.pos.lon" =>
            results.map(_.gpsLongitude).foldLeft("")(_ + ", " + _)
        case "Set_ils.loc" =>
            results.map(_.ilsLocation).foldLeft("")(_ + ", " + _)
        case "Set_gps.gs_knts" =>
            results.map(_.groundSpeed).foldLeft("")(_ + ", " + _)
        case "Set_gps.gpsalt" =>
            results.map(_.gpsAltitude).foldLeft("")(_ + ", " + _)
        case "Set_gps.trk" =>
            results.map(_.trackAngle).foldLeft("")(_ + ", " + _)
        case "Set_gps.gps_vs_fpm" =>
            results.map(_.gps_vs_fpm).foldLeft("")(_ + ", " + _)
        case "Set_bvs" =>
            results.map(_.verticalSpeed).foldLeft("")(_ + ", " + _)
     }
    
    Ok(finalResult)
  }
 
  def getList(dirName: String) = {
    views.html.graph(dirName)
  }
}