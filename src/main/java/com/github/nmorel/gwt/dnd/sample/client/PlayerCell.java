package com.github.nmorel.gwt.dnd.sample.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class PlayerCell
    extends AbstractCell<String>
{

    interface Template
        extends SafeHtmlTemplates
    {
        @SafeHtmlTemplates.Template( "<i>empty</i>" )
        SafeHtml empty();

        @SafeHtmlTemplates.Template( "<div draggable=\"true\">{0}</div>" )
        SafeHtml draggable( String value );
    }

    private static Template template;

    public PlayerCell()
    {
        super( BrowserEvents.DRAGSTART, BrowserEvents.DRAGEND, BrowserEvents.DRAGENTER, BrowserEvents.DRAGLEAVE,
            BrowserEvents.DRAGOVER, BrowserEvents.DROP );
        if ( null == template )
        {
            template = GWT.create( Template.class );
        }
    }

    @Override
    public void render( Context context, String value, SafeHtmlBuilder sb )
    {
        if ( null == value || value.isEmpty() )
        {
            sb.append( template.empty() );
        }
        else
        {
            sb.append( template.draggable( value ) );
        }
    }

    @Override
    public void onBrowserEvent( Context context, Element parent, String value, NativeEvent event,
                                ValueUpdater<String> valueUpdater )
    {
        String eventType = event.getType();
        if ( BrowserEvents.DRAGENTER.equals( eventType ) )
        {
            System.out.println( "drag enter cell : " + event.getDataTransfer().getData( "player" ) );
        }
        else if ( BrowserEvents.DRAGLEAVE.equals( eventType ) )
        {
            System.out.println( "drag leave cell : " + event.getDataTransfer().getData( "player" ) );
        }
        else if ( BrowserEvents.DRAGOVER.equals( eventType ) )
        {
            System.out.println( "drag over cell : " + event.getDataTransfer().getData( "player" ) );
        }
        else if ( BrowserEvents.DROP.equals( eventType ) )
        {
            String player = event.getDataTransfer().getData( "player" );
            System.out.println( "drop on cell : " + player );
            setValue( context, parent, player );
            if ( null != valueUpdater )
            {
                valueUpdater.update( player );
            }
        }
        else if ( BrowserEvents.DRAGSTART.equals( eventType ) )
        {
            System.out.println( "drag start cell : " + value );
            event.getDataTransfer().setData( "player", value );
            event.getDataTransfer().setData( "column", Integer.toString( context.getColumn() ) );
            event.getDataTransfer().setData( "row", Integer.toString( context.getIndex() ) );
        }
        else if ( BrowserEvents.DRAGEND.equals( eventType ) )
        {
            System.out.println( "drag end cell : " + event.getDataTransfer().getData( "player" ) );
        }
    }

}
