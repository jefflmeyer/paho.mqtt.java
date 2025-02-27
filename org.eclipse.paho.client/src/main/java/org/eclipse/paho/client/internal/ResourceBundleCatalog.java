/*******************************************************************************
 * Copyright (c) 2009, 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    https://www.eclipse.org/legal/epl-2.0
 * and the Eclipse Distribution License is available at 
 *   https://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *    Dave Locke - initial API and implementation and/or initial documentation
 */
package org.eclipse.paho.client.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleCatalog extends MessageCatalog {
	
	private ResourceBundle bundle;
	
	public ResourceBundleCatalog() {
		//MAY throws MissingResourceException
		bundle = ResourceBundle.getBundle("org.eclipse.paho.client.internal.nls.messages");
	}

	protected String getLocalizedMessage(int id) {
		try {
			return bundle.getString(Integer.toString(id));
		} catch(MissingResourceException mre) {
			return "MqttException";
		}
	}
}
