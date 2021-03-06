package org.zazukoians.zz2h.style

import javax.ws.rs.core.MediaType
import org.apache.clerezza.rdf.scala.utils.RichGraphNode
import org.apache.clerezza.rdf.ontologies._
import org.apache.clerezza.rdf.core._
import org.apache.clerezza.rdf.utils._
import org.apache.clerezza.rdf.scala.utils.Preamble._
import org.osgi.service.component.annotations._
import javax.ws.rs.ext.MessageBodyWriter
import javax.ws.rs.ext.Provider
import javax.ws.rs.Produces
import org.apache.clerezza.rdf.utils.GraphNode
import java.lang.reflect.Type
import java.io.PrintWriter
import java.lang.annotation.Annotation
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap

/**
 * Renders resources using ld2h on the client.
 */
@Provider
@Produces(Array("text/html"))
class ResourceRenderlet extends MessageBodyWriter[GraphNode] {

  def renderedPage() = {
    <html xmlns="http://www.w3.org/1999/xhtml" class="fetch" resource="" context="http://zz2h.zazukoians.org/modes/FullPage">
      <head>
        <script src="//code.jquery.com/jquery-2.1.4.min.js"></script>
        <!-- <script id="data" type="text/turtle">%graph%</script> -->
        <link rel="matchers" href="//cdn.zazuko.com/rdf2h/rdf2h.github.io/v0.0.1/2015/rdf2h-points.ttl" type="text/turtle"/>
        <link rel="matchers" href="http://data.admin.ch/rdf2h/matchers.ttl" type="text/turtle"/>
        <link rel="matchers" href="http://data.admin.ch/rdf2h/site-matchers.ttl" type="text/turtle"/>
        <script src="//cdnjs.cloudflare.com/ajax/libs/systemjs/0.19.31/system.js"></script>
        <script src="//cdn.zazuko.com/rdf2h/rdf2h/v0.3.0/dist/rdf-ext.js"></script>
        <script src="//cdn.zazuko.com/rdf2h/rdf2h/v0.3.0/dist/rdf2h.js"></script>
        <script src="//cdn.zazuko.com/rdf2h/ld2h/v0.4.1/dist/ld2h.js"></script>
        <script src="//cdn.zazuko.com/retog/rdf-store-ldp-browser/v0.3.0-rc2f/dist/rdf-store-ldp.js"></script>
      </head>
      <body>
        <script type="text/javascript">
          $(function () {{
            //working around  problem eith unregistered and broken parsers
            var parsers =  new LdpStore.ParserUtil({{
                    'text/turtle': LdpStore.parsers.findParsers('text/turtle')[0]
                }});
            var store = new LdpStore({{
                'parsers': parsers
            }});
            rdf.parsers = parsers;
            LD2h.store = store;
            window.ld2hRendered = LD2h.expand();
        }});
        </script>
      </body>
    </html>

  }
  def getSize(node: GraphNode, clazz: Class[_], classType: Type, annotations: Array[Annotation],
    mediaType: MediaType): Long = -1

  def isWriteable(clazz: Class[_], classType: Type, annotations: Array[Annotation],
    mediaType: MediaType): Boolean = {
      mediaType.isCompatible(MediaType.TEXT_HTML_TYPE) && clazz == classOf[GraphNode];
  }
  
  def writeTo(node: GraphNode, clazz: Class[_], classType: Type, annotations: Array[Annotation],
    mediaType: MediaType, map: MultivaluedMap[String, Object],
    outputStream: java.io.OutputStream): Unit = {
      val out = new PrintWriter(outputStream);
      out.print(renderedPage.toString);
      out.flush();
    }
      
}
