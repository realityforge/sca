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
public class LimitingReconnectPolicyTestCase
    extends TestCase
{
    public void testAlwaysReconnectPolicy()
        throws Exception
    {
        final AlwaysReconnectPolicy policy = AlwaysReconnectPolicy.POLICY;
        assertEquals( "attemptConnection",
                      true,
                      policy.attemptConnection( 0, 0 ) );
        assertEquals( "disconnectOnError",
                      true,
                      policy.disconnectOnError( new Throwable() ) );
        assertEquals( "reconnectOnDisconnect",
                      true,
                      policy.reconnectOnDisconnect() );
    }
}
