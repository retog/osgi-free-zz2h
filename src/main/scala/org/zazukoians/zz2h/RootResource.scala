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
    /*val static = ConfigDirProvider.publicNode.getSubPath(path); 
    if (static.exists) {
      throw new RuntimeException("hooray: "+path);
    }*/
    //set vary: Accept header
    Response.ok({
        val iri : IRI = new IRI(uriInfo.getAbsolutePath().toString());
        val result = gnp.getLocal(iri)
        result
    }).header("Vary", "Accept").build();
    
  }
}
