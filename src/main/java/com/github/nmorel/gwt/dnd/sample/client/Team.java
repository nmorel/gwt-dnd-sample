package com.github.nmorel.gwt.dnd.sample.client;

public class Team
{

    private String name;

    private String[] positions = new String[5];

    public Team( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String[] getPositions()
    {
        return positions;
    }

    public void setPositions( String[] positions )
    {
        this.positions = positions;
    }
}
