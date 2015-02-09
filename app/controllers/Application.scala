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

object Application extends Controller {

  def main = Action {
    Ok(views.html.main("test"))
  }
  
  def getAltitudeData(fileName: String) = Action { implicit request =>
    val lines = fromFile("C:\\Scala\\Git\\graph\\app\\assets\\mops\\" + fileName).getLines
    var results = ListBuffer[Float]()
    lines.foreach { line =>
        if (line.contains("Set_palt")) {
           val loc = line.indexOfSlice("Set_palt")
           if (loc != -1) {}
             var i = loc
             var endFound = false
             for (x <- line) if (endFound == false) {
               i += 1
               if (line.charAt(i) == ')')
                 endFound = true
             }
             results += line.subSequence(loc + 9, i).toString.toFloat
        }
    }
    println(results.toList)
    Ok(results.toList.map(_.toString).reduce{_ + ", " + _})
  }
  
  def getLatData(fileName: String) = Action { implicit request =>
    val lines = fromFile("C:\\Scala\\Git\\graph\\app\\assets\\mops\\" + fileName).getLines
    var results = ListBuffer[Float]()
    lines.foreach { line =>
        if (line.contains("Set_gps.pos.lat")) {
           val loc = line.indexOfSlice("Set_gps.pos.lat")
           if (loc != -1) {}
             var i = loc
             var endFound = false
             for (x <- line) if (endFound == false) {
               i += 1
               if (line.charAt(i) == ')')
                 endFound = true
             }
             results += line.subSequence(loc + 16, i).toString.toFloat / 360000
        }
    }
    println(results.toList)
    Ok(results.toList.map(_.toString).reduce{_ + ", " + _})
  }
  
  def getList(dirName: String) = {
    views.html.graph(dirName)
  }
}