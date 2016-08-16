package org.zazukoians.zz2h

import org.apache.clerezza.commons.rdf.Graph
import org.apache.clerezza.commons.rdf.IRI
import org.apache.clerezza.commons.rdf.impl.sparql.SparqlClient
import org.apache.clerezza.rdf.utils.GraphNode
import org.apache.clerezza.rdf.utils.UriMutatingGraph
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider

class DescribeGraphNodeProvider(endpoint: IRI, 
         replacementMap:  Map[String, String]) extends GraphNodeProvider {
  val sparqlClient = new SparqlClient(endpoint.getUnicodeString)
  
  //not actually used, only here to implement the interface
  def existsLocal(iri: IRI): Boolean = {
    //TODO make configurable
    sparqlClient.queryResult("ASK {"+mutateUriReverse(iri)+" ?p ?o}").asInstanceOf[Boolean];
  }
  def get(iri: IRI): GraphNode = getLocal(iri);
  def getLocal(iri: IRI): GraphNode = {
    //TODO make query configurable (this is virtuoso version)
    val raw = sparqlClient.queryResult("define sql:describe-mode \"CBD\" DESCRIBE "+mutateUriReverse(iri)).asInstanceOf[Graph];
    mutateUris(new GraphNode(iri, raw))
  }
  
  protected def mutateUris(node: GraphNode) : GraphNode = {
    val g: Graph = node.getGraph
    val iri: IRI = node.getNode.asInstanceOf[IRI]
    return new GraphNode(mutateUri(iri), mutateUris(g));
  }
  
  protected def mutateUris(graph: Graph) : Graph = {
    var g: Graph = graph
    for (b <- replacementMap) {
      val sourcePrefix = b._1
      val targetPrefix = b._2
      g = new UriMutatingGraph(g, sourcePrefix, targetPrefix)
    }
    return g
  }
 
  
  protected def mutateUri(origIri: IRI) : IRI = {
    var iri: IRI = origIri
    for (b <- replacementMap) {
      val sourcePrefix = b._1
      val targetPrefix = b._2
      if (iri.getUnicodeString().startsWith(sourcePrefix)) {
          val uriRest = iri.getUnicodeString()
                  .substring(sourcePrefix.length);
          iri = new IRI(targetPrefix+uriRest);
      }
    }
    return iri;
  }
  protected def mutateUriReverse(origIri: IRI) : IRI = {
    var iri: IRI = origIri
    for (b <- replacementMap) {
      val sourcePrefix = b._2
      val targetPrefix = b._1
      if (iri.getUnicodeString().startsWith(sourcePrefix)) {
          val uriRest = iri.getUnicodeString()
                  .substring(sourcePrefix.length);
          iri = new IRI(targetPrefix+uriRest);
      }
    }
    return iri;
  }
  
}
