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
import org.apache.clerezza.rdf.scala.utils.EzGraph
import org.apache.clerezza.rdf.scala.utils.Preamble
import org.apache.clerezza.rdf.utils.GraphNode
import org.apache.clerezza.rdf.utils.UriMutatingGraph
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat
import org.apache.clerezza.rdf.core.serializedform.Parser



class GraphBackedGraphNodeProvider(g: Graph) extends GraphNodeProvider {

   
  override def existsLocal(iri: IRI): Boolean = {
    !getLocal(iri).getNodeContext.isEmpty
  }

  override def get(iri: IRI): GraphNode = {
    getLocal(iri)
  }

  override def getLocal(iri: IRI): GraphNode = {
    new GraphNode(iri, g)
  }
}
