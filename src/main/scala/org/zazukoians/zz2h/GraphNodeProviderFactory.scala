package org.zazukoians.zz2h

import java.io.File
import java.io.FileInputStream
import org.apache.clerezza.commons.rdf.Graph
import org.apache.clerezza.commons.rdf.IRI
import org.apache.clerezza.commons.rdf.Literal
import org.apache.clerezza.commons.rdf.impl.sparql.SparqlGraph
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph
import org.apache.clerezza.rdf.ontologies.DC
import org.apache.clerezza.rdf.ontologies.RDF
import org.apache.clerezza.rdf.ontologies.RDFS
import org.apache.clerezza.rdf.scala.utils.CollectedIter
import org.apache.clerezza.rdf.scala.utils.EzGraph
import org.apache.clerezza.rdf.scala.utils.Preamble
import org.apache.clerezza.rdf.scala.utils.RichGraphNode
import org.apache.clerezza.rdf.utils.GraphNode
import org.apache.clerezza.rdf.utils.UriMutatingGraph
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat
import org.apache.clerezza.rdf.core.serializedform.Parser
import org.wymiwyg.commons.util.dirbrowser.PathNode


import org.apache.clerezza.rdf.core.serializedform.Parser

object GraphNodeProviderFactory {
  
  val parser: Parser = Parser.getInstance
  
  def getGraphNodeProvider: GraphNodeProvider = {
    val cgRemoteFile = ConfigDirProvider.configNode.getSubPath("remote-content-graph.ttl")
    if (cgRemoteFile.exists) {
      getGraphNodeProviderForRemote(cgRemoteFile);
    } else {
      val cgFile = ConfigDirProvider.configNode.getSubPath("content-graph.ttl")
      if (cgFile.exists) {
        val g = parser.parse(cgFile.getInputStream, SupportedFormat.TURTLE)
        new GraphBackedGraphNodeProvider(g)
      } else {
        throw new RuntimeException("No configuration found")
      }
    }
  }
  
  private def getGraphNodeProviderForRemote(config: PathNode): GraphNodeProvider = {
    val locationG = parser.parse(config.getInputStream, SupportedFormat.TURTLE)
    val p = new Preamble(locationG)
    import p._
    def zz2h(localName: String) = ("http://zz2h.zazukoians.org/ontology/"+localName).iri
    val endpoint = zz2h("sparqlEndpoint")/-RDF.`type`
    val replacementNodes: CollectedIter[RichGraphNode] = endpoint/zz2h("replacement")
    val useDescribe = (endpoint/zz2h("useDescribe")*) == "true"
    if (useDescribe) {
      var replacementMap: Map[String, String] = Map(); 
      for (r <- replacementNodes) {
        val sourcePrefix = (r/zz2h("sourcePrefix")*)
        val targetPrefix = (r/zz2h("targetPrefix")*)
        replacementMap += (sourcePrefix -> targetPrefix)
      }
      new DescribeGraphNodeProvider(endpoint.getNode.asInstanceOf[IRI], replacementMap)
    } else {
      var g: Graph = new SparqlGraph(endpoint.getNode.asInstanceOf[IRI].getUnicodeString)
      for (r <- replacementNodes) {
        val sourcePrefix = (r/zz2h("sourcePrefix")*)
        val targetPrefix = (r/zz2h("targetPrefix")*)
        g = new UriMutatingGraph(g, sourcePrefix, targetPrefix)
      }
      new GraphBackedGraphNodeProvider(g)
    }
  }
}
