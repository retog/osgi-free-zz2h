package org.example.rest

import org.apache.clerezza.jaxrs.rdf.providers.GraphWriter
import org.apache.clerezza.rdf.core.serializedform.Serializer
import javax.ws.rs.ext.Provider

/**
 *
 * @author user
 */
@Provider
class MyGraphWriter extends GraphWriter {
  bindSerializer(Serializer.getInstance)
}
