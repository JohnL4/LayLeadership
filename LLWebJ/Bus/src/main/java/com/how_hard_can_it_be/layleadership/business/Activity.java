package com.how_hard_can_it_be.layleadership.business;

import java.util.Objects;

/**
 * An activity at church, such as a committee.
 */
public class Activity {
    private final long _id;
    private String _name;
    private ActivityType _type;
    private int _meetingsPerYear;
    private int _extraHoursPerMonth;
    private String _blurb;

    private Activity(long _id, String _name, ActivityType _type, int _meetingsPerYear, int _extraHoursPerMonth, String _blurb)
    {
        this._id = _id;
        this._name = _name;
        this._type = _type;
        this._meetingsPerYear = _meetingsPerYear;
        this._extraHoursPerMonth = _extraHoursPerMonth;
        this._blurb = _blurb;
    }

    public long get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public ActivityType get_type() {
        return _type;
    }

    public void set_type(ActivityType _type) {
        this._type = _type;
    }

    public int get_meetingsPerYear() {
        return _meetingsPerYear;
    }

    public void set_meetingsPerYear(int _meetingsPerYear) {
        this._meetingsPerYear = _meetingsPerYear;
    }

    public int get_extraHoursPerMonth() {
        return _extraHoursPerMonth;
    }

    public void set_extraHoursPerMonth(int _extraHoursPerMonth) {
        this._extraHoursPerMonth = _extraHoursPerMonth;
    }

    public String get_blurb() {
        return _blurb;
    }

    public void set_blurb(String _blurb) {
        this._blurb = _blurb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return _id == activity._id &&
                _meetingsPerYear == activity._meetingsPerYear &&
                _extraHoursPerMonth == activity._extraHoursPerMonth &&
                Objects.equals(_name, activity._name) &&
                _type == activity._type &&
                Objects.equals(_blurb, activity._blurb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _name, _type, _meetingsPerYear, _extraHoursPerMonth, _blurb);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                ", _type=" + _type +
                ", _meetingsPerYear=" + _meetingsPerYear +
                ", _extraHoursPerMonth=" + _extraHoursPerMonth +
                ", _blurb='" + _blurb + '\'' +
                '}';
    }

    public static ActivityBuilder getActivityBuilder()
    {
        return new ActivityBuilder();
    }

    public static class ActivityBuilder {
        private long id;
        private String name;
        private ActivityType type;
        private int meetingsPerYear;
        private int extraHoursPerMonth;
        private String blurb;

        public ActivityBuilder set_id(long id) {
            this.id = id;
            return this;
        }

        public ActivityBuilder set_name(String name) {
            this.name = name;
            return this;
        }

        public ActivityBuilder set_type(ActivityType type) {
            this.type = type;
            return this;
        }

        public ActivityBuilder set_meetingsPerYear(int meetingsPerYear) {
            this.meetingsPerYear = meetingsPerYear;
            return this;
        }

        public ActivityBuilder set_extraHoursPerMonth(int extraHoursPerMonth) {
            this.extraHoursPerMonth = extraHoursPerMonth;
            return this;
        }

        public ActivityBuilder set_blurb(String blurb) {
            this.blurb = blurb;
            return this;
        }

        public Activity createActivity() {
            return new Activity(id, name, type, meetingsPerYear, extraHoursPerMonth, blurb);
        }
    }
}
