package bindong

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn

/**
  * Created by xxh on 17-11-21.
  */
object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("coastal")
    implicit val executionContext = system.dispatcher
    implicit val materializer = ActorMaterializer()
    val route =
      path("hello") {
        get {
          //complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,"<h1>Say hello to akka-http</h1>"))
          //complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,"<!DOCTYPE html>\n<html lang=\"zh-CN\">\n  <head>\n    <meta charset=\"utf-8\">\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->\n    <title>Bootstrap 101 Template</title>\n\n    <!-- Bootstrap -->\n    <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n\n    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\n    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\n    <!--[if lt IE 9]>\n      <script src=\"https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js\"></script>\n      <script src=\"https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js\"></script>\n    <![endif]-->\n  </head>\n  <body>\n    <h1>hello bootstrap！</h1>\n\n    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n    <script src=\"https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js\"></script>\n    <!-- Include all compiled plugins (below), or include individual files as needed -->\n    <script src=\"js/bootstrap.min.js\"></script>\n  </body>\n</html>"))
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,s"""<!DOCTYPE html>\n<html lang=\"zh-CN\">\n  <head>\n    <meta charset=\"utf-8\">\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->\n    <meta name=\"description\" content=\"\">\n    <meta name=\"author\" content=\"\">\n    <link rel=\"icon\" href=\"../../favicon.ico\">\n\n    <title>Signin Template for Bootstrap</title>\n\n    <!-- Bootstrap core CSS -->\n    <link href=\"https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\">\n\n    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->\n    <link href=\"../../assets/css/ie10-viewport-bug-workaround.css\" rel=\"stylesheet\">\n\n    <!-- Custom styles for this template -->\n    <link href=\"signin.css\" rel=\"stylesheet\">\n\n    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->\n    <!--[if lt IE 9]><script src=\"../../assets/js/ie8-responsive-file-warning.js\"></script><![endif]-->\n    <script src=\"../../assets/js/ie-emulation-modes-warning.js\"></script>\n\n    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\n    <!--[if lt IE 9]>\n      <script src=\"https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js\"></script>\n      <script src=\"https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js\"></script>\n    <![endif]-->\n  </head>\n\n  <body>\n\n    <div class=\"container\">\n\n      <form class=\"form-signin\">\n        <h2 class=\"form-signin-heading\">Please sign in</h2>\n        <label for=\"inputEmail\" class=\"sr-only\">Email address</label>\n        <input type=\"email\" id=\"inputEmail\" class=\"form-control\" placeholder=\"Email address\" required autofocus>\n        <label for=\"inputPassword\" class=\"sr-only\">Password</label>\n        <input type=\"password\" id=\"inputPassword\" class=\"form-control\" placeholder=\"Password\" required>\n        <div class=\"checkbox\">\n          <label>\n            <input type=\"checkbox\" value=\"remember-me\"> Remember me\n          </label>\n        </div>\n        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n      </form>\n\n    </div> <!-- /container -->\n\n\n    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->\n    <script src=\"../../assets/js/ie10-viewport-bug-workaround.js\"></script>\n  </body>\n</html>"""))
        }
      } ~
      path("login"){
        getFromResource("webapp/index.html")
        } ~
      path("index"){
        getFromResourceDirectory("webapp")
        }
    val bindingFuture = Http().bindAndHandle(route,"0.0.0.0",8090)
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
  }
}
