/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a factory class for creating a FileHandler without revealing
 * how they are constructed. It is to be used for creating generic FileHandler
 * commands, specifically to deal with creating file transfer commands.
 * 
 * @author Joe Osborn
 *
 */
public class FileHandlerFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);

	/**
	 * Default constructor
	 */
	public FileHandlerFactory() {
	}

	/**
	 * Method which gets a FileHandler and subsequently executes it to initiate a
	 * file transfer.
	 * 
	 * @return
	 */
	public FileHandler getFileHandler(String source, String destination, ConnectionConfiguration sourceConfig, ConnectionConfiguration destinationConfig) throws IOException {
		FileHandler handler = null;

		if (sourceConfig.hostname == null || destinationConfig.hostname == null) {
			logger.error("you didn't provide a hostname in the ConnectionConfiguration for the files to be transfered to! Exiting.");
		}
		
		if(isLocal(sourceConfig.hostname) && isLocal(destinationConfig.hostname)) {
			handler = new LocalFileHandler(source, destination);
		}
		else {
			handler = new RemoteFileHandler(source, destination, sourceConfig, destinationConfig);
		}
		
		return handler;
	}

	
	/**
	 * A function to check whether or not the provided hostname by the user in
	 * CommandFactory is a local hostname or remote hostname.
	 * 
	 * @param host - String of the hostname to be checked
	 * @return boolean - returns true if the hostname matches that of the local
	 *         hostname, false otherwise.
	 */
	private boolean isLocal(String host) {

		// Get the local hostname address
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostname = addr.getHostName();

		// If the local hostname is the same as the hostname provided, then it is local
		if (hostname == host)
			return true;
		else
			return false;

	}
}
