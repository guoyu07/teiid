/*
 * ${license}
 */
package org.jboss.teiid.connectors.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.resource.ResourceException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
	private FileSystem fileSystem;
	private URI uri;

	public HDFSConnectionImpl(HDFSManagedConnectionFactory config) {
		this.config = config;

		Configuration conf = new Configuration();
		URI uri = null;
		try {
			uri = new URI("hdfs://" + config.getHost() + ":" + config.getPort());
			conf.set("fs.default.name", "hdfs://" + config.getHost() + ":" + config.getPort());
			fileSystem = FileSystem.get(uri, conf);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogManager.logDetail(LogConstants.CTX_CONNECTOR, "HDFS Connection has been created."); //$NON-NLS-1$
	}

	public String readFromFile(String filePath) {
		byte[] btbuffer = new byte[5];
		String result = null;
		try {
			uri = new URI("hdfs://" + config.getHost() + ":" + config.getPort()+ "/"+ filePath);
			FSDataInputStream in = fileSystem.open(new Path(uri));
			in.read(btbuffer, 0, 5);
			result = new String(btbuffer);
			System.out.println("READING... " + result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void close() throws ResourceException {
		LogManager.logDetail(LogConstants.CTX_CONNECTOR, "HDFS Connection has been closed."); //$NON-NLS-1$

	}
}
