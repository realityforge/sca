/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * Interface representing policy Connector uses to determine when to
 * re-establish the connection.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004/06/21 05:54:29 $
 */
public interface ReconnectionPolicy
{
    /**
     * Return true to continue with connection attempt.
     *
     * @param connectionAttempts the number of sequential connection failures
     * @param lastConnectionAttemptTime time at which connection was last
     * attempted
     * @return true to continue with connection attempt.
     */
    boolean attemptConnection( long lastConnectionAttemptTime,
                               int connectionAttempts );

    /**
     * Return true to disconnect connection on specified error.
     *
     * @param t the error
     * @return true to disconnect connection.
     */
    boolean disconnectOnError( Throwable t );

    /**
     * Return true to reconnect on any forced disconnection.
     *
     * @return true to reconnect on any forced disconnection.
     */
    boolean reconnectOnDisconnect();
}
