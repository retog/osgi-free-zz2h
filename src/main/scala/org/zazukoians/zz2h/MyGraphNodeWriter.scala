package org.zazukoians.zz2h

import org.apache.clerezza.jaxrs.rdf.providers.GraphNodeWriter
import org.apache.clerezza.rdf.core.serializedform.Serializer
import javax.ws.rs.ext.Provider

/**
 *
 * @author user
 */
@Provider
class MyGraphNodeWriter extends GraphNodeWriter {
  bindSerializer(Serializer.getInstance)
}
