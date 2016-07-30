package org.zazukoians.zz2h

import java.util.EnumSet
import javax.servlet.DispatcherType
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.resource.Resource

object Main {

  def main(args: Array[String]) {
    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")
    val jettyServer = new Server(5000)
    val all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.INCLUDE, 
      DispatcherType.REQUEST)
    val jerseyFilter = context.addFilter(classOf[org.glassfish.jersey.servlet.ServletContainer], "/*", 
      all)
    jerseyFilter.setInitParameter("jersey.config.server.provider.packages", this.getClass.getPackage.getName)
    jerseyFilter.setInitParameter("jersey.config.servlet.filter.forwardOn404", "true")
    jerseyFilter.setInitParameter("jersey.config.server.provider.scanning.recursive", "true")
    val resourceRoot = Resource.newResource(classOf[App].getResource("/META-INF/resources/"))
    context.setBaseResource(resourceRoot)
    val holderPwd = new ServletHolder("default", classOf[DefaultServlet])
    holderPwd.setInitParameter("dirAllowed", "true")
    context.addServlet(holderPwd, "/")
    jettyServer.setHandler(context)
    try {
      jettyServer.start()
      jettyServer.join()
    } finally {
      jettyServer.destroy()
    }
  }
}
