package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A {@link Member} DTO class.
 */
@Entity( name="Member")
public class MemberDto
{
    @Id @Column( name="MemberId")
    private long   _id;

    @Column( name="FirstName")
    private String _firstName;

    @Column( name="lastName")
    private String _lastName;

    @Column( name="phoneNumber")
    private String _phoneNumber;

    @Column( name="emailAddress")
    private String _emailAddress;

    @Column( name="comments")
    private String _comments;

    /**
     * Deceased, moved, etc.
     */
    @Column( name="Active")
    private boolean _active;

    public MemberDto()
    {
    }

    public MemberDto( long aId, String aFirstName, String aLastName, String aPhoneNumber, String aEmailAddress,
                      boolean aActive, String aComments )
    {
        _id           = aId;
        _firstName    = aFirstName;
        _lastName     = aLastName;
        _phoneNumber  = aPhoneNumber;
        _emailAddress = aEmailAddress;
        _active       = aActive;
        _comments     = aComments;
    }

    public long getId()
    {
        return _id;
    }

//    public void setId( long aId )
//    {
//        _id = aId;
//    }

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
}
