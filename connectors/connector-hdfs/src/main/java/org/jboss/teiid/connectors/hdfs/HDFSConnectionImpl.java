/*
 * ${license}
 */
package org.jboss.teiid.connectors.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.resource.ResourceException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.teiid.core.BundleUtil;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.resource.spi.BasicConnection;

/**
 * Connection to the resource. You must define HDFSConnection interface, that
 * extends the "javax.resource.cci.Connection"
 */
public class HDFSConnectionImpl extends BasicConnection implements HDFSConnection {

	public static final BundleUtil UTIL = BundleUtil.getBundleUtil(HDFSConnectionImpl.class);

	private HDFSManagedConnectionFactory config;

	public HDFSConnectionImpl(HDFSManagedConnectionFactory config) throws URISyntaxException, IOException {
		this.config = config;

		Configuration conf = new Configuration();		
		URI uri = new URI("hdfs://" + config.getHost() + ":" + config.getPort());
		conf.set("fs.default.name", "hdfs://" + config.getHost() + ":" + config.getPort());
		FileSystem fileSystem = FileSystem.get(uri, conf);
		LogManager.logDetail(LogConstants.CTX_CONNECTOR, "HDFS Connection has been created."); //$NON-NLS-1$
	}

	@Override
	public void close() throws ResourceException {
		LogManager.logDetail(LogConstants.CTX_CONNECTOR, "HDFS Connection has been closed."); //$NON-NLS-1$

	}
}
