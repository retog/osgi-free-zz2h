package org.zazukoians.zz2h


import javax.ws.rs._
import javax.ws.rs.core._
import org.apache.clerezza.commons.rdf.IRI
import org.apache.clerezza.rdf.core._
import org.apache.clerezza.rdf.utils._
import org.apache.clerezza.rdf.scala.utils._
import Preamble._
import org.apache.clerezza.rdf.ontologies._
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider


@Path("")
class RootResource {

  var gnp: GraphNodeProvider = new Zz2hGraphNodeProvider
  
  @GET
  @Path("{path: .*}")
  def get(@Context uriInfo: UriInfo) : Response = {
    val iri : IRI = new IRI(uriInfo.getAbsolutePath().toString());
    val node = gnp.getLocal(iri)
    val context = node.getNodeContext
    if (context.size == 0) {
      Response.status(404).build();
    } else {
      Response.ok({
        node
      }).header("Vary", "Accept").build();
    }
  }
}
