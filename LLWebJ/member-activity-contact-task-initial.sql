/*
insert into memberActivity (memberid, activityid, startdate, enddate)
select MemberId, ActivityId, date('2019-08-01'), date('2022-07-31')
    from member, activity
    where FirstName in ('Alice','Bob','Carrie','John')
        and Activity.Name = 'Lay Leadership'
*/
/*
insert into ContactTask (MemberId, AssigneeId, Type)
select MemberId, 6, 1
    from Member
    where FirstName in ('David','Ellen','Fred','Gail')
*/

