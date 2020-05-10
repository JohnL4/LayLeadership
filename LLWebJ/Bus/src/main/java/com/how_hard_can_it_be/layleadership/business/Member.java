package com.how_hard_can_it_be.layleadership.business;

import java.util.Objects;

/**
 * A church member.
 */
public class Member
{
    private final long    _id;
    private       String  _firstName;
    private       String  _lastName;
    private       String  _phoneNumber;
    private       String  _emailAddress;

    /**
     * Deceased, moved, etc.
     */
    private       boolean _active;

    private       String  _comments;

    public Member( long anid, String aFirstName, String aLastName, String aPhoneNumber, String anEmailAddress,
                   boolean anActive, String aComments )
    {
        _id           = anid;
        _firstName    = aFirstName;
        _lastName     = aLastName;
        _phoneNumber  = aPhoneNumber;
        _emailAddress = anEmailAddress;
        _active       = anActive;
        _comments     = aComments;
    }

    public long getId()
    {
        return _id;
    }

    public String getFirstName()
    {
        return _firstName;
    }

    public void setFirstName( String aFirstName )
    {
        _firstName = aFirstName;
    }

    public String getLastName()
    {
        return _lastName;
    }

    public void setLastName( String aLastName )
    {
        _lastName = aLastName;
    }

    public String getPhoneNumber()
    {
        return _phoneNumber;
    }

    public void setPhoneNumber( String aPhoneNumber )
    {
        _phoneNumber = aPhoneNumber;
    }

    public String getEmailAddress()
    {
        return _emailAddress;
    }

    public void setEmailAddress( String aEmailAddress )
    {
        _emailAddress = aEmailAddress;
    }

    public boolean isActive()
    {
        return _active;
    }

    public void setActive( boolean aActive )
    {
        _active = aActive;
    }

    public String getComments()
    {
        return _comments;
    }

    public void setComments( String aComments )
    {
        _comments = aComments;
    }

    @Override
    public boolean equals( Object aO )
    {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;
        Member member = (Member) aO;
        return _id == member._id &&
               _active == member._active &&
               Objects.equals( _firstName, member._firstName ) &&
               Objects.equals( _lastName, member._lastName ) &&
               Objects.equals( _phoneNumber, member._phoneNumber ) &&
               Objects.equals( _emailAddress, member._emailAddress ) &&
               Objects.equals( _comments, member._comments );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( _id, _firstName, _lastName, _phoneNumber, _emailAddress, _active, _comments );
    }

    @Override
    public String toString()
    {
        return "Member{" +
               "_id=" + _id +
               ", _firstName='" + _firstName + '\'' +
               ", _lastName='" + _lastName + '\'' +
               ", _phoneNumber='" + _phoneNumber + '\'' +
               ", _emailAddress='" + _emailAddress + '\'' +
               ", _active=" + _active +
               ", _comments='" + _comments + '\'' +
               '}';
    }
}
