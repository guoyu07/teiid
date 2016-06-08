/*
 * ${license}
 */
package org.jboss.teiid.connectors.hdfs;

import javax.resource.ResourceException;

import org.teiid.core.BundleUtil;
import org.teiid.resource.spi.BasicConnectionFactory;
import org.teiid.resource.spi.BasicManagedConnectionFactory;

public class HDFSManagedConnectionFactory extends BasicManagedConnectionFactory {

	public static final BundleUtil UTIL = BundleUtil.getBundleUtil(HDFSManagedConnectionFactory.class);

	private String host;
	private Integer port;

	@Override
	public BasicConnectionFactory<HDFSConnectionImpl> createConnectionFactory() throws ResourceException {

		return new BasicConnectionFactory<HDFSConnectionImpl>() {
			@Override
			public HDFSConnectionImpl getConnection() throws ResourceException {
				return new HDFSConnectionImpl(HDFSManagedConnectionFactory.this);
			}
		};
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int prot) {
		this.port = port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		HDFSManagedConnectionFactory other = (HDFSManagedConnectionFactory) obj;

		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;

		return true;
	}

}
