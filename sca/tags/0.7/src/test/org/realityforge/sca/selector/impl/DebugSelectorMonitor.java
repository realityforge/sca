/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.selector.impl;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004/06/21 05:54:30 $
 */
class DebugSelectorMonitor
    implements SelectorMonitor
{
    public void selectorStartup()
    {
        System.out.println( "selectorStartup()" );
    }

    public void enteringSelectorLoop()
    {
        System.out.println( "enteringSelectorLoop()" );
    }

    public void enteringSelect()
    {
        System.out.println( "enteringSelect()" );
    }

    public void selectCompleted( int count )
    {
        System.out.println( "selectCompleted(" + count + ")" );
    }

    public void handlingSelectEvent( SelectionKey key )
    {
        System.out.println( "handlingSelectEvent(" + key + ")" );
    }

    public void exitingSelectorLoop()
    {
        System.out.println( "exitingSelectorLoop()" );
    }

    public void selectorShutdown()
    {
        System.out.println( "selectorShutdown()" );
    }

    public void errorClosingSelector( IOException ioe )
    {
        System.out.println( "errorClosingSelector(" + ioe + ")" );
    }

    public void invalidAttachment( SelectionKey key )
    {
        System.out.println( "invalidAttachment(" + key + ")" );
    }
}
