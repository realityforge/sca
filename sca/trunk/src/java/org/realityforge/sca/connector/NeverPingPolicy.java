/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * Ping policy for never performing ping.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004/06/21 05:54:29 $
 */
public class NeverPingPolicy
    implements PingPolicy
{
    /** Constant containing instance of never ping policy. */
    public static final NeverPingPolicy POLICY = new NeverPingPolicy();

    /**
     * @see PingPolicy#checkPingConnection
     */
    public boolean checkPingConnection()
    {
        return false;
    }

    /**
     * @see PingPolicy#nextPingCheck
     */
    public long nextPingCheck()
    {
        return Long.MAX_VALUE;
    }
}
