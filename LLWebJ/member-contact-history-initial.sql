/*
select ct.ContactTaskId, a.FirstName Assignee, m.FirstName, ct.Type, er.Name, ct.Summary
    from ContactTask ct
        join Member m on m.MemberId = ct.MemberId
        join Member a on a.MemberId = ct.AssigneeId
        left join EnumReference er on er.Value = ct.Type
            and er.TableName = 'ContactTask' and er.ColumnName = 'Type'
*/
            
insert into MemberContactHistory (ContactTaskId, MemberId, ContactDate, Notes, Result, NextContactTask)
select ct.ContactTaskId
        -- , ct.AssigneeId
        -- , a.FirstName Assignee
        , ct.MemberId
        /*
        , m.FirstName
        , ct.Type
        , er.Name
        , ct.Summary
        */
        , date('now')
        , 'Initial contact, thinking about it'
        , 3 -- Considering
        , ct.ContactTaskId
    from ContactTask ct
        join Member m on m.MemberId = ct.MemberId
        join Member a on a.MemberId = ct.AssigneeId
        left join EnumReference er on er.Value = ct.Type
            and er.TableName = 'ContactTask' and er.ColumnName = 'Type'
    where ct.Type = 2