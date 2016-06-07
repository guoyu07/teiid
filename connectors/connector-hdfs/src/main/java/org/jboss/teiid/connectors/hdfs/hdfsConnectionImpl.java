/*
 * ${license}
 */
package org.jboss.teiid.connectors.hdfs;


import javax.resource.ResourceException;

import org.teiid.resource.spi.BasicConnection;
import org.teiid.logging.LogManager;
import org.teiid.core.BundleUtil;
import org.teiid.logging.LogConstants;

/**
 * Connection to the resource. You must define hdfsConnection interface, that 
 * extends the "javax.resource.cci.Connection"
 */
public class hdfsConnectionImpl extends BasicConnection implements hdfsConnection {

	public static final BundleUtil UTIL = BundleUtil.getBundleUtil(hdfsConnectionImpl.class);


    private hdfsManagedConnectionFactory config;

    public hdfsConnectionImpl(hdfsManagedConnectionFactory env) {
        this.config = env;
        // todo: connect to your source here
        
        LogManager.logDetail(LogConstants.CTX_CONNECTOR, "hdfs Connection has been created."); //$NON-NLS-1$

    }
    
    @Override
    public void close() {
        LogManager.logDetail(LogConstants.CTX_CONNECTOR, "hdfs Connection has been closed."); //$NON-NLS-1$
    	
    }
}
