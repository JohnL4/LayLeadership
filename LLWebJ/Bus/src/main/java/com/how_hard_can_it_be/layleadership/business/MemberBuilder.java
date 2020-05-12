package com.how_hard_can_it_be.layleadership.business;

public class MemberBuilder
{
    private long _id;
    private String  _firstName;
    private String  _lastName;
    private String  _phoneNumber;
    private String  _emailAddress;
    private boolean _Active;
    private String  _comments;

    public MemberBuilder setId( long aId )
    {
        _id = aId;
        return this;
    }

    public MemberBuilder setFirstName( String aFirstName )
    {
        _firstName = aFirstName;
        return this;
    }

    public MemberBuilder setLastName( String aLastName )
    {
        _lastName = aLastName;
        return this;
    }

    public MemberBuilder setPhoneNumber( String aPhoneNumber )
    {
        _phoneNumber = aPhoneNumber;
        return this;
    }

    public MemberBuilder setEmailAddress( String aEmailAddress )
    {
        _emailAddress = aEmailAddress;
        return this;
    }

    public MemberBuilder setActive( boolean aActive )
    {
        _Active = aActive;
        return this;
    }

    public MemberBuilder setComments( String aComments )
    {
        _comments = aComments;
        return this;
    }

    public Member createMember()
    {
        return new Member( _id, _firstName, _lastName, _phoneNumber, _emailAddress, _Active, _comments );
    }
}
