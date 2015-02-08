package controllers

import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import java.io.File, java.io.FilenameFilter
import java.util.Arrays
import scala.collection.mutable.ListBuffer
import play.api.data._
import play.api.data.Forms._

object Application extends Controller {

  def main = Action {
    Ok(views.html.main("test"))
  }
  
  val userForm = Form(
    "dir" -> text
  )
  
  def getList = Action { implicit request =>
    val (dir) = userForm.bindFromRequest.get
    println("HEEEERE")
    Ok(views.html.graph(dir))
    //Ok("GOT POST: " + dir)
  }

  def getDirs(dir: String): List[(String, String)] = {

    if (dir == null) {
      Nil
    } else {
      var result = ListBuffer[(String, String)]()
      val newDir = if (dir.charAt(dir.length() - 1) == '\\') {
        dir.substring(0, dir.length() - 1) + "/";
      } else if (dir.charAt(dir.length() - 1) != '/') {
        dir + "/";
      } else {
        ""
      }

      val finalDir = java.net.URLDecoder.decode(newDir, "UTF-8");

      if (new File(finalDir).exists()) {
        val files = new File(finalDir).list(new FilenameFilter() {
          override def accept(dir: File, name: String): Boolean = {
            name.charAt(0) != '.';
          }
        })
        Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
        // All dirs
        for (file <- files) {
          if (new File(finalDir, file).isDirectory()) {
            val what = (finalDir, file)
            result += what
          }
        }
        result.toList
      } else {
        Nil
      }
    }
  }
  
  def getFiles(dir: String): List[(String, String, String)] = {

    if (dir == null) {
      Nil
    } else {
      var result = ListBuffer[(String, String, String)]()
      val newDir = if (dir.charAt(dir.length() - 1) == '\\') {
        dir.substring(0, dir.length() - 1) + "/";
      } else if (dir.charAt(dir.length() - 1) != '/') {
        dir + "/";
      } else {
        ""
      }

      val finalDir = java.net.URLDecoder.decode(newDir, "UTF-8");

      if (new File(finalDir).exists()) {
        val files = new File(finalDir).list(new FilenameFilter() {
          override def accept(dir: File, name: String): Boolean = {
            name.charAt(0) != '.';
          }
        })
        Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);

        // All files
        for (file <- files) {
          if (!new File(finalDir, file).isDirectory()) {
            val dotIndex = file.lastIndexOf('.');
            val ext = if (dotIndex > 0) file.substring(dotIndex + 1) else "";
            val what = (ext, finalDir, file)
            result += what
          }
        }
        result.toList
      } else {
        Nil
      }
    }
  }
}