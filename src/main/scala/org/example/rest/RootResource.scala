package org.example.rest


import javax.ws.rs._
import javax.ws.rs.core._
import org.apache.clerezza.rdf.core._
import org.apache.clerezza.rdf.utils._
import org.apache.clerezza.rdf.scala.utils._
import Preamble._
import org.apache.clerezza.rdf.ontologies._


@Path("hello")
class RootResource {

  @GET
  def hello(): String = {
    "hello world"
  }
  
  @GET
  @Path("rdf")
  def graph(@Context uriInfo: UriInfo) =
    {
      def schema(localName: String) = {
        ("http://schema.org/"+localName).iri
      }
      
      val resource = uriInfo.getRequestUri().toString().iri;
      val g = new EzGraph() {
        (
          resource.a(schema("WebPage")) -- schema("headline") --> "Hello world".lang("en")
          -- RDFS.seeAlso  --> "http://zazukoians.org/".iri
         )
      }
      //val node = new GraphNode(resource,g)
      Response.ok(g).header("Vary", "Accept").build();
    }
}
