/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * Callback used to determine whether to ping a Connection.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004/06/21 05:54:29 $
 */
public interface PingPolicy
{
    /**
     * Return true if need to ping connection.
     *
     * @return true if need to ping connection.
     */
    boolean checkPingConnection();

    /**
     * Return the time at which the next ping should occur. If the value {@link
     * Long#MAX_VALUE} is returned then no pinging will ever occur.
     *
     * @return the time that ping should be checked
     */
    long nextPingCheck();
}
