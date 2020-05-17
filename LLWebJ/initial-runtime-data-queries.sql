select * from Member
;
-- Current Lay Leadership Committee Assignments
select ma.*
        , m.FirstName 
        , a.Name
    from MemberActivity ma
        join Member m on ma.MemberId = m.MemberId
        join Activity a on a.ActivityId = ma.ActivityId
    where a.Name = 'Lay Leadership'
        and ma.StartDate <= date('now')
        and date('now') <= ma.EndDate
;
-- Task assignments with history
select ct.*
        , a.FirstName Assignee
        , m.FirstName Member
        , ter.Name TaskType
        , mch.*
        , rer.Name ResultType
    from ContactTask ct
        join Member a on a.MemberId = ct.AssigneeId
        join Member m on m.MemberId = ct.MemberId
        left join EnumReference ter on ter.Value = ct.Type
            and ter.TableName = 'ContactTask' and ter.ColumnName = 'Type'
        left join MemberContactHistory mch on mch.MemberId = ct.MemberId
        left join EnumReference rer on rer.Value = mch.Result
            and rer.TableName = 'MemberContactHistory' and rer.ColumnName = 'Result'