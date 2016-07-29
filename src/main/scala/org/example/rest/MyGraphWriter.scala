package org.example.rest

import org.apache.clerezza.jaxrs.rdf.providers.GraphWriter
import org.apache.clerezza.rdf.core.serializedform.Serializer

/**
 *
 * @author user
 */
class MyGraphWriter extends GraphWriter {

  bindSerializer(Serializer.getInstance)
}
