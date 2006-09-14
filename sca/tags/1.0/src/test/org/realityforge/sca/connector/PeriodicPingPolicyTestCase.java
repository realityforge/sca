/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004/06/21 05:54:29 $
 */
public class PeriodicPingPolicyTestCase
    extends TestCase
{
    public void testNeverPingPolicy()
        throws Exception
    {
        final long lastPingTime = System.currentTimeMillis();
        final MockConnector connector = new MockConnector( 0,
                                                           0,
                                                           lastPingTime );
        final PeriodicPingPolicy policy = new PeriodicPingPolicy( 100,
                                                                  connector );
        assertEquals( "nextPingCheck",
                      lastPingTime + 100,
                      policy.nextPingCheck() );
        assertEquals( "checkPingConnection",
                      true,
                      policy.checkPingConnection() );
    }
}
