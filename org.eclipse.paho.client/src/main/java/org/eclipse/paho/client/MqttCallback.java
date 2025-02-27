/*******************************************************************************
 * Copyright (c) 2009, 2018 IBM Corp.
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
 *    James Sutton - MQTT V5 implementation 
 */
package org.eclipse.paho.client;

import org.eclipse.paho.common.MqttException;
import org.eclipse.paho.common.MqttMessage;
import org.eclipse.paho.common.packet.MqttProperties;

/**
 * Enables an application to be notified when asynchronous events related to the
 * client occur. Classes implementing this interface can be registered on both
 * types of client: {@link IMqttClient#setCallback(MqttCallback)} and
 * {@link IMqttAsyncClient#setCallback(MqttCallback)}
 */
public interface MqttCallback {

	/**
	 * This method is called when the server gracefully disconnects from the client
	 * by sending a disconnect packet, or when the TCP connection is lost due to a
	 * network issue or if the client encounters an error.
	 * 
	 * @param disconnectResponse
	 *            a {@link MqttDisconnectResponse} containing relevant properties
	 *            related to the cause of the disconnection.
	 */
    void disconnected(MqttDisconnectResponse disconnectResponse);

	/**
	 * This method is called when an exception is thrown within the MQTT client. The
	 * reasons for this may vary, from malformed packets, to protocol errors or even
	 * bugs within the MQTT client itself. This callback surfaces those errors to
	 * the application so that it may decide how best to deal with them.
	 * 
	 * For example, The MQTT server may have sent a publish message with an invalid
	 * topic alias, the MQTTv5 specification suggests that the client should
	 * disconnect from the broker with the appropriate return code, however this is
	 * completely up to the application itself.
	 * 
	 * @param exception
	 *            - The exception thrown causing the error.
	 */
    void mqttErrorOccurred(MqttException exception);

	/**
	 * This method is called when a message arrives from the server.
	 *
	 * <p>
	 * This method is invoked synchronously by the MQTT client. An acknowledgment is
	 * not sent back to the server until this method returns cleanly.
	 * </p>
	 * <p>
	 * If an implementation of this method throws an <code>Exception</code>, then
	 * the client will be shut down. When the client is next re-connected, any QoS 1
	 * or 2 messages will be redelivered by the server.
	 * </p>
	 * <p>
	 * Any additional messages which arrive while an implementation of this method
	 * is running, will build up in memory, and will then back up on the network.
	 * </p>
	 * <p>
	 * If an application needs to persist data, then it should ensure the data is
	 * persisted prior to returning from this method, as after returning from this
	 * method, the message is considered to have been delivered, and will not be
	 * reproducible.
	 * </p>
	 * <p>
	 * It is possible to send a new message within an implementation of this
	 * callback (for example, a response to this message), but the implementation
	 * must not disconnect the client, as it will be impossible to send an
	 * acknowledgment for the message being processed, and a deadlock will occur.
	 * </p>
	 *
	 * @param topic
	 *            name of the topic on the message was published to
	 * @param message
	 *            the actual message.
	 * @throws Exception
	 *             if a terminal error has occurred, and the client should be shut
	 *             down.
	 */
    void messageArrived(String topic, MqttMessage message) throws Exception;

	/**
	 * Called when delivery for a message has been completed, and all
	 * acknowledgments have been received. For QoS 0 messages it is called once the
	 * message has been handed to the network for delivery. For QoS 1 it is called
	 * when PUBACK is received and for QoS 2 when PUBCOMP is received. The token
	 * will be the same token as that returned when the message was published.
	 *
	 * @param token
	 *            the delivery token associated with the message.
	 */
    void deliveryComplete(IMqttToken token);

	/**
	 * Called when the connection to the server is completed successfully.
	 * 
	 * @param reconnect
	 *            If true, the connection was the result of automatic reconnect.
	 * @param serverURI
	 *            The server URI that the connection was made to.
	 */
    void connectComplete(boolean reconnect, String serverURI);

	/**
	 * Called when an AUTH packet is received by the client.
	 * 
	 * @param reasonCode
	 *            The Reason code, can be Success (0), Continue authentication (24)
	 *            or Re-authenticate (25).
	 * @param properties
	 *            The {@link MqttProperties} to be sent, containing the
	 *            Authentication Method, Authentication Data and any required User
	 *            Defined Properties.
	 */
    void authPacketArrived(int reasonCode, MqttProperties properties);

}
