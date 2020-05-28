package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.ActivityType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * An activity at the church such as a committee, a Sunday school class or some other non-committee team.
 */
@Entity( name = "Activity")
public class ActivityDto {

    @Id @Column( name = "ActivityId")
    private long _id;

    /**
     * Name of the committee, Sunday School class, team, etc.
     */
    @Column( name = "Name")
    private String _name;

    @Column( name = "Type")
    private ActivityType _activityType;

    /**
     * How many meetings the activity has per year.
     */
    @Column( name = "Meetings_Per_Year")
    private int _meetingsPerYear;

    /**
     * How many extra hours of effort the activity requires per month, above the time spent in meetings.
     */
    @Column( name = "Extra_Hours_Per_Month")
    private int _extraHoursPerMonth;

    /**
     * The marketing/recruiting blurb for the activity, to be put up on a web site somewhere or read over the phone
     * to a possible recruit.
     */
    @Column( name = "Blurb")
    private String _blurb;
}
