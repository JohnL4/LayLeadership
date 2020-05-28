package com.how_hard_can_it_be.layleadership.business;

public enum ActivityType
{
    /**
     * Activity type is unspecified; sort of a default value.
     */
    Unspecified,

    /**
     * Should only see this on a deserialization failure from the d/b (data integrity failure, basically).
     */
    Unknown,

    /**
     * Activity is a committee.
     */
    Committee,

    /**
     * Activity is Sunday School, which isn't something that is normally recruitd for, but (a) it counts for contacts
     * among church members, and, more importantly (b) TEACHING Sunday School takes some effort and might indicate
     * what kind of bandwidth a church member in this position has.
     */
    SundaySchool,

    /**
     * Kind of like a committee without the formal designation.
     */
    Team

}
