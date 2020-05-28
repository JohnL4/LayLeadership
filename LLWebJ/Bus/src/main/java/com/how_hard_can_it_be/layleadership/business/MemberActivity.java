package com.how_hard_can_it_be.layleadership.business;

import java.util.Date;
import java.util.Objects;

/**
 * An {@link Activity} a {@link Member} has or is engaged in.
 */
public class MemberActivity
{
    private long    _id;
    private long    _memberId; // TODO: should be Member, not id
    private long    _activityId; // TODO: should be Activity, not id
    private Date    _startDate;
    private Date    _endDate;
    private String  _tenureNotes;
    private boolean _chair;

    private MemberActivity(
            long _id,
            long _memberId,
            long _activityId,
            Date _startDate,
            Date _endDate,
            String _tenureNotes, boolean _chair)
    {
        this._id          = _id;
        this._memberId    = _memberId;
        this._activityId  = _activityId;
        this._startDate   = _startDate;
        this._endDate     = _endDate;
        this._tenureNotes = _tenureNotes;
        this._chair       = _chair;
    }

    public long get_id()
    {
        return _id;
    }

    public void set_id(long _id)
    {
        this._id = _id;
    }

    public long get_memberId()
    {
        return _memberId;
    }

    public void set_memberId(long _memberId)
    {
        this._memberId = _memberId;
    }

    public long get_activityId()
    {
        return _activityId;
    }

    public void set_activityId(long _activityId)
    {
        this._activityId = _activityId;
    }

    public Date get_startDate()
    {
        return _startDate;
    }

    public void set_startDate(Date _startDate)
    {
        this._startDate = _startDate;
    }

    public Date get_endDate()
    {
        return _endDate;
    }

    public void set_endDate(Date _endDate)
    {
        this._endDate = _endDate;
    }

    public String get_tenureNotes()
    {
        return _tenureNotes;
    }

    public void set_tenureNotes(String _tenureNotes)
    {
        this._tenureNotes = _tenureNotes;
    }

    public boolean is_chair()
    {
        return _chair;
    }

    public void set_chair(boolean _chair)
    {
        this._chair = _chair;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberActivity that = (MemberActivity) o;
        return _id == that._id &&
                _memberId == that._memberId &&
                _activityId == that._activityId &&
                _chair == that._chair &&
                Objects.equals(_startDate, that._startDate) &&
                Objects.equals(_endDate, that._endDate) &&
                Objects.equals(_tenureNotes, that._tenureNotes);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_id, _memberId, _activityId, _startDate, _endDate, _tenureNotes, _chair);
    }

    @Override
    public String toString()
    {
        return "MemberActivity{" +
                "_id=" + _id +
                ", _memberId=" + _memberId +
                ", _activityId=" + _activityId +
                ", _startDate=" + _startDate +
                ", _endDate=" + _endDate +
                ", _tenureNotes='" + _tenureNotes + '\'' +
                ", _chair=" + _chair +
                '}';
    }

    public static MemberActivityBuilder getBuilder() { return new MemberActivityBuilder(); }

    public static class MemberActivityBuilder
    {
        private long id;
        private long    memberId;
        private long    activityId;
        private Date    startDate;
        private Date    endDate;
        private String  tenureNotes;
        private boolean chair;

        public MemberActivityBuilder set_id(long id)
        {
            this.id = id;
            return this;
        }

        public MemberActivityBuilder set_memberId(long memberId)
        {
            this.memberId = memberId;
            return this;
        }

        public MemberActivityBuilder set_activityId(long activityId)
        {
            this.activityId = activityId;
            return this;
        }

        public MemberActivityBuilder set_startDate(Date startDate)
        {
            this.startDate = startDate;
            return this;
        }

        public MemberActivityBuilder set_endDate(Date endDate)
        {
            this.endDate = endDate;
            return this;
        }

        public MemberActivityBuilder set_tenureNotes(String tenureNotes)
        {
            this.tenureNotes = tenureNotes;
            return this;
        }

        public MemberActivityBuilder set_chair(boolean chair)
        {
            this.chair = chair;
            return this;
        }

        public MemberActivity createMemberActivity()
        {
            return new MemberActivity(id, memberId, activityId, startDate, endDate, tenureNotes, chair);
        }
    }
}
