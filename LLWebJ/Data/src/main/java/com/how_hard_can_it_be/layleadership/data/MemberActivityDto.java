package com.how_hard_can_it_be.layleadership.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity( name = "MemberActivity")
public class MemberActivityDto {

    @Id @Column( name = "MemberActivityId")
    private long _id;

    /**
     * Id of the {@link MemberDto} involved.
     */
    @Column( name = "MemberId")
    private long _memberId;

    /**
     * Id of the {@link ActivityDto} involved.
     */
    @Column( name = "ActivityId")
    private long _activityId;

    /**
     * The date (at midnight, 12:00 am) on which which the {@link MemberDto} startd the {@link ActivityDto}.
     */
    @Column( name = "StartDate")
    private Date _startDate;

    /**
     * The date (at 11:59 pm) on which the {@link MemberDto} ended the {@link ActivityDto}.
     */
    @Column( name = "EndDate")
    private Date _endDate;

    /**
     * Any (confidential to Lay Leadership) notes on the member's tenure during this activity.
     */
    @Column( name = "TenureNotes")
    private String _tenureNotes;

    /**
     * True iff the member was a chair or in some other leadership role during this activity.
     */
    @Column( name = "Chair")
    private boolean _chair;
}
