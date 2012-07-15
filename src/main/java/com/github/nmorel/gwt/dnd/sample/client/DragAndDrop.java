package com.github.nmorel.gwt.dnd.sample.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DragAndDrop
    implements EntryPoint
{

    private static String[] names = new String[] { "Gabriel", "Arthur", "Louise", "Rapha\u00ebl", "Adam", "Chlo\u00e9",
        "Paul", "Alexandre", "Louis", "Emma", "Antoine", "Maxime", "Alice", "In\u00e8s", "Sarah", "Jeanne", "Lucas" };

    private FlowPanel playerPanel = new FlowPanel();

    private Map<String, Widget> playerWidget = new HashMap<String, Widget>();

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {

        RootPanel
            .get()
            .add(
                new Label(
                    "Drag a player to the team and position he gonna play. To remove a player from a team, drag it back to the player's list." ) );

        playerPanel.getElement().getStyle().setPadding( 10, Unit.PX );
        RootPanel.get().add( playerPanel );

        for ( int i = 0; i < names.length; i++ )
        {
            playerPanel.add( createPlayer( names[i] ) );
        }

        final CellTable<Team> table = new CellTable<Team>();

        // Name Column
        table.addColumn( new TextColumn<Team>() {
            @Override
            public String getValue( Team object )
            {
                return object.getName();
            }
        }, "Team Name" );

        for ( int i = 0; i < 5; i++ )
        {
            addPositionColumn( i, table );
        }

        table.setRowData( Arrays.asList( new Team( "Team 1" ), new Team( "Team 2" ) ) );

        RootPanel.get().add( table );

        // Adding handler to handle the drag from player cell that will remove a
        // player from team
        playerPanel.addBitlessDomHandler( new DragOverHandler() {
            @Override
            public void onDragOver( DragOverEvent event )
            {
                System.out.println( "drag over player panel : " + event.getDataTransfer().getData( "player" ) );
            }
        }, DragOverEvent.getType() );
        playerPanel.addBitlessDomHandler( new DropHandler() {
            @Override
            public void onDrop( DropEvent event )
            {
                String player = event.getDataTransfer().getData( "player" );
                System.out.println( "drop on player panel : " + player );
                String column = event.getDataTransfer().getData( "column" );
                String row = event.getDataTransfer().getData( "row" );
                if ( null != column && null != row )
                {
                    playerWidget.get( player ).setVisible( true );
                    table.getVisibleItem( Integer.parseInt( row ) ).getPositions()[Integer.parseInt( column ) - 1] =
                        null;
                    table.redrawRow( Integer.parseInt( row ) );
                }
            }
        }, DropEvent.getType() );
    }

    private Widget createPlayer( final String name )
    {
        final InlineLabel player = new InlineLabel( name );

        // Styling stuff
        Style style = player.getElement().getStyle();
        style.setBackgroundColor( "red" );
        style.setCursor( Cursor.POINTER );
        style.setProperty( "borderRadius", "5px" );
        style.setPadding( 5, Unit.PX );
        style.setMargin( 5, Unit.PX );

        // Need to declare the element draggable
        player.getElement().setDraggable( Element.DRAGGABLE_TRUE );
        // Add data to the drag event
        player.addDragStartHandler( new DragStartHandler() {
            @Override
            public void onDragStart( DragStartEvent event )
            {
                System.out.println( "drag start player : " + name );
                event.getDataTransfer().setData( "player", name );
                event.getDataTransfer().setDragImage( player.getElement(), 10, 10 );
            }
        } );
        player.addDragEndHandler( new DragEndHandler() {
            @Override
            public void onDragEnd( DragEndEvent event )
            {
                System.out.println( "drag end player : " + name );
            }
        } );

        playerWidget.put( name, player );

        return player;
    }

    private void addPositionColumn( final int position, AbstractCellTable<Team> table )
    {

        Column<Team, String> column = new Column<Team, String>( new PlayerCell() ) {
            @Override
            public String getValue( Team object )
            {
                return object.getPositions()[position];
            }
        };
        column.setFieldUpdater( new FieldUpdater<Team, String>() {
            @Override
            public void update( int index, Team object, String value )
            {
                String oldPlayer = object.getPositions()[position];
                if ( null != oldPlayer )
                {
                    playerWidget.get( oldPlayer ).setVisible( true );
                }
                if ( null != value )
                {
                    playerWidget.get( value ).setVisible( false );
                }
                object.getPositions()[position] = value;
            }
        } );
        table.addColumn( column, "Position " + ( position + 1 ) );
    }
}
