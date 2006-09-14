/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

/**
 * Ping policy that pings on specified period.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004/06/21 05:54:29 $
 */
public class PeriodicPingPolicy
    implements PingPolicy
{
    /** The period between pings. */
    private final long m_period;

    /** The associated connector. */
    private final Connector m_connector;

    /**
     * Create policy with specified period.
     *
     * @param period the period
     * @param connector the associated connector
     */
    public PeriodicPingPolicy( final long period,
                               final Connector connector )
    {
        m_period = period;
        m_connector = connector;
    }

    /**
     * @see PingPolicy#checkPingConnection()
     */
    public boolean checkPingConnection()
    {
        return true;
    }

    /**
     * @see PingPolicy#nextPingCheck()
     */
    public long nextPingCheck()
    {
        return m_connector.getLastPingTime() + m_period;
    }
}
