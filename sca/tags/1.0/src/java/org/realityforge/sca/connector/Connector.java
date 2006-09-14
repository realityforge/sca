/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.connector;

import panmx.annotations.Impact;
import panmx.annotations.MBean;
import panmx.annotations.MxAttribute;
import panmx.annotations.MxOperation;
import panmx.annotations.MxField;

/**
 * The Connector is a base class for connectors. Connectors establish a
 * connection to a resource and attempt to maintain the connection and reconnect
 * when the connection fails.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004/06/21 05:54:29 $
 * @mx.component
 */
@MBean
public class Connector
{
    /** The associated ping policy for connector. */
    private PingPolicy m_pingPolicy = NeverPingPolicy.POLICY;

    /** The associated reconnection policy for connector. */
    private ReconnectionPolicy m_reconnectPolicy = AlwaysReconnectPolicy.POLICY;

    /** The associated monitor that receives events about connector. */
    private ConnectorMonitor m_monitor = NullMonitor.MONITOR;

    /** The underlying connection. */
    private ConnectorConnection m_connection;

    /** A flag indicating whether the connection is "active". */
    private boolean m_active;

    /** A flag indicating whether the connection is "connected". */
    private boolean m_connected;

    /** Time at which last transmission occured. */
    private long m_lastTxTime;

    /**
     * The last message transmitted. May be null. Simply used to display status
     * on the web page.
     */
    private Object m_lastTxMessage;

    /** Time at which last receive occured. */
    private long m_lastRxTime;

    /**
     * The last message received. May be null. Simply used to display status on
     * the web page.
     */
    private Object m_lastRxMessage;

    /** The time the last connection attempt started. */
    private long m_lastConnectionTime;

    /** Number of sequential failed connection attempts. */
    private int m_connectionAttempts;

    /** The reason the last conenction attempt failed. */
    private String m_connectionError;

    /** The time at which last ping occured. */
    private long m_lastPingTime;

    /**
     * Specify the ping policy that connector will use.
     *
     * @param pingPolicy the policy
     */
    public void setPingPolicy( final PingPolicy pingPolicy )
    {
        if( null == pingPolicy )
        {
            throw new NullPointerException( "pingPolicy" );
        }
        m_pingPolicy = pingPolicy;
    }

    /**
     * Specify the reconnection policy that connector will use.
     *
     * @param reconnectPolicy the policy
     */
    public void setReconnectPolicy( final ReconnectionPolicy reconnectPolicy )
    {
        if( null == reconnectPolicy )
        {
            throw new NullPointerException( "reconnectPolicy" );
        }
        m_reconnectPolicy = reconnectPolicy;
    }

    /**
     * Specify the connection that connector will manage.
     *
     * @param connection the connection
     */
    public void setConnection( final ConnectorConnection connection )
    {
        if( null == connection )
        {
            throw new NullPointerException( "connection" );
        }
        m_connection = connection;
    }

    /**
     * Specify the monitor to receive events from connector.
     *
     * @param monitor the monitor
     */
    public void setMonitor( final ConnectorMonitor monitor )
    {
        if( null == monitor )
        {
            throw new NullPointerException( "monitor" );
        }
        m_monitor = monitor;
    }

    /**
     * Method called to indicate transmission occured.
     *
     * @param message the message
     */
    public void transmissionOccured( final Object message )
    {
        m_lastTxTime = System.currentTimeMillis();
        m_lastTxMessage = message;
    }

    /**
     * Method called to indicate receive occured.
     *
     * @param message the message
     */
    public void receiveOccured( final Object message )
    {
        m_lastRxTime = System.currentTimeMillis();
        m_lastRxMessage = message;
    }

    /**
     * Method called to indicate bidirectional communication occured.
     *
     * @param message the message
     */
    public void commOccured( final Object message )
    {
        final long now = System.currentTimeMillis();
        m_lastTxTime = now;
        m_lastRxTime = now;
        m_lastRxMessage = message;
        m_lastTxMessage = message;
    }

    /**
     * Method called to failure to communicate.
     *
     * @param t the error
     */
    public void commErrorOccured( final Throwable t )
    {
        m_connectionError = t.toString();
        if( getReconnectPolicy().disconnectOnError( t ) )
        {
            disconnect();
            if( getReconnectPolicy().reconnectOnDisconnect() )
            {
                connect();
            }
        }
    }

    /**
     * Return the time at which last ping occured.
     *
     * @return the time at which last ping occured.
     * @mx.attribute
     */
    @MxAttribute( description = "The time the last ping occured",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public long getLastPingTime()
    {
        return m_lastPingTime;
    }

    /**
     * Return the time at which last transmission occured.
     *
     * @return the time at which last transmission occured.
     * @mx.attribute
     */
    @MxAttribute( description = "The time the last tx occured",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public long getLastTxTime()
    {
        return m_lastTxTime;
    }

    /**
     * Return the last message transmitted.
     *
     * @return the last message transmitted.
     * @mx.attribute
     */
    @MxAttribute( description = "The last message txed",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public Object getLastTxMessage()
    {
        return m_lastTxMessage;
    }

    /**
     * Return the time at which last receive occured.
     *
     * @return the time at which last receive occured.
     * @mx.attribute
     */
    @MxAttribute( description = "The last time a message was rxed",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public long getLastRxTime()
    {
        return m_lastRxTime;
    }

    /**
     * Return the last message received.
     *
     * @return the last message received.
     * @mx.attribute
     */
    @MxAttribute( description = "The last message rxed",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public Object getLastRxMessage()
    {
        return m_lastRxMessage;
    }

    /**
     * Return true if connector is active.
     *
     * @return true if connector is active.
     * @mx.attribute
     */
    @MxAttribute( description = "Whether the connector is active",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public boolean isActive()
    {
        return m_active;
    }

    /**
     * Set the flag to indicate if the connector is active or inactive.
     *
     * @param active the flag to indicate if the connector is active or
     * inactive.
     * @mx.attribute
     */
    @MxAttribute( )
    public void setActive( final boolean active )
    {
        m_active = active;
    }

    /**
     * Return true if Connector connected.
     *
     * @return true if Connector connected.
     * @mx.attribute
     */
    @MxAttribute( description = "Whether the connector is connected",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public boolean isConnected()
    {
        return m_connected;
    }

    /**
     * Set the connected state.
     *
     * @param connected the connected state.
     */
    protected void setConnected( final boolean connected )
    {
        m_connected = connected;
    }

    /**
     * Return the time the last connection attempt started.
     *
     * @return the time the last connection attempt started.
     * @mx.attribute
     */
    @MxAttribute( description = "The time the last connection attempt started",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public long getLastConnectionTime()
    {
        return m_lastConnectionTime;
    }

    /**
     * Return the number of sequential failed connection attempts.
     *
     * @return the number of sequential failed connection attempts.
     * @mx.attribute
     */
    @MxAttribute( description = "The number of sequential failed connection attempts",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public int getConnectionAttempts()
    {
        return m_connectionAttempts;
    }

    /**
     * Return the last connection error. The error could be caused either by
     * connection failure or failure during operation.
     *
     * @return the last connection error
     * @mx.attribute
     */
    @MxAttribute( description = "The error for the last connection attempt",
      fields = @MxField( name = "currencyTimeLimit", value = "-1" ) )
    public String getConnectionError()
    {
        return m_connectionError;
    }

    /**
     * Method to make connector establish connection.
     *
     * @mx.operation
     */
    @MxOperation( description = "Make the connector connect", impact = Impact.ACTION )
    public void connect()
    {
        final long now = System.currentTimeMillis();

        synchronized( getSyncLock() )
        {
            if( isConnected() )
            {
                disconnect();
            }

            while( !isConnected() && isActive() )
            {
                final boolean connect = getReconnectPolicy().
                    attemptConnection( m_lastConnectionTime,
                                       m_connectionAttempts );
                if( !connect )
                {
                    getMonitor().skippingConnectionAttempt();
                    return;
                }
                getMonitor().attemptingConnection();
                try
                {
                    m_lastConnectionTime = now;
                    getConnection().doConnect();
                    m_lastPingTime = System.currentTimeMillis();
                    commOccured( null );
                    m_connectionAttempts = 0;
                    m_connectionError = null;
                    setConnected( true );
                    getMonitor().connectionEstablished();
                }
                catch( final Throwable t )
                {
                    m_connectionAttempts++;
                    m_connectionError = t.toString();
                    getMonitor().errorConnecting( t );
                    performDisconnect();
                }
            }
        }
    }

    /**
     * Method to disconect Connector.
     *
     * @mx.operation
     */
    @MxOperation( description = "Make the connector disconnect", impact = Impact.ACTION )
    public void disconnect()
    {
        synchronized( getSyncLock() )
        {
            if( isConnected() )
            {
                getMonitor().attemptingDisconnection();
                setConnected( false );
                performDisconnect();
            }
        }
    }

    /**
     * Actually perform the disconnection.
     */
    private void performDisconnect()
    {
        try
        {
            getConnection().doDisconnect();
        }
        catch( final Throwable t )
        {
            getMonitor().errorDisconnecting( t );
        }
    }

    /**
     * Check to see if need to ping connection and if so then perform ping.
     * Return the time that ping should be next checked at.
     *
     * @return the time that ping should be re-checked.
     */
    public long checkPing()
    {
        final PingPolicy pingPolicy = getPingPolicy();
        final boolean doPing = pingPolicy.checkPingConnection();
        final long result = pingPolicy.nextPingCheck();
        if( doPing )
        {
            ping();
        }
        return result;
    }

    /**
     * Attempt to verify Connector is connected. If not connected then the
     * connector will attempt to establish a connection.
     *
     * @return true if connected
     */
    public boolean verifyConnected()
    {
        synchronized( getSyncLock() )
        {
            if( !isConnected() )
            {
                connect();
            }
            return isConnected();
        }
    }

    /**
     * Attempt to ping connection. By default just calls {@link
     * #validateConnection}.
     *
     * @return true if connected and ping successful.
     */
    public boolean ping()
    {
        m_lastPingTime = System.currentTimeMillis();
        return validateConnection();
    }

    /**
     * Attempt to verify Connector is connected. If not connected then the
     * connector will attempt to establish a connection.
     *
     * @return true if connected
     */
    public boolean validateConnection()
    {
        synchronized( getSyncLock() )
        {
            if( !verifyConnected() )
            {
                return false;
            }
            else
            {
                getMonitor().attemptingValidation();
                doValidateConnection();
                return isConnected();
            }
        }
    }

    /**
     * Utility method that actully does the work of validating connection. If an
     * error occurs the connection will be disconnected and the error recorded.
     */
    void doValidateConnection()
    {
        try
        {
            getConnection().doValidateConnection();
        }
        catch( final Throwable t )
        {
            m_connectionError = t.toString();
            getMonitor().errorValidatingConnection( t );
            disconnect();
            if( getReconnectPolicy().reconnectOnDisconnect() )
            {
                connect();
            }
        }
    }

    /**
     * Return the object that will be used to synchronization
     * connection/disconnection.
     *
     * @return the sync lock
     */
    protected Object getSyncLock()
    {
        return this;
    }

    /**
     * Return the ping policy.
     *
     * @return the ping policy.
     */
    protected PingPolicy getPingPolicy()
    {
        return m_pingPolicy;
    }

    /**
     * Return the reconnection policy.
     *
     * @return the reconnection policy.
     */
    protected ReconnectionPolicy getReconnectPolicy()
    {
        return m_reconnectPolicy;
    }

    /**
     * Return the monitor.
     *
     * @return the monitor.
     */
    protected ConnectorMonitor getMonitor()
    {
        return m_monitor;
    }

    /**
     * Return the connection.
     *
     * @return the connection
     */
    protected ConnectorConnection getConnection()
    {
        if( null == m_connection )
        {
            throw new NullPointerException( "connection" );
        }
        return m_connection;
    }
}
