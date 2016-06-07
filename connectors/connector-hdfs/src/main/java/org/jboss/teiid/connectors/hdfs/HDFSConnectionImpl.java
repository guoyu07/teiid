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
 * Connection to the resource. You must define HDFSConnection interface, that
 * extends the "javax.resource.cci.Connection"
 */
public class HDFSConnectionImpl extends BasicConnection implements HDFSConnection {

	public static final BundleUtil UTIL = BundleUtil.getBundleUtil(HDFSConnectionImpl.class);

	private HDFSManagedConnectionFactory config;

	public HDFSConnectionImpl(HDFSManagedConnectionFactory env) {
		this.config = env;
		// todo: connect to your source here

		LogManager.logDetail(LogConstants.CTX_CONNECTOR, "HDFS Connection has been created."); //$NON-NLS-1$

	}

	@Override
	public void close() throws ResourceException {
		LogManager.logDetail(LogConstants.CTX_CONNECTOR, "HDFS Connection has been closed."); //$NON-NLS-1$

	}
}
