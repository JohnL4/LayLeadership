package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.MemberActivity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberActivityMapper
{
    MemberActivityMapper INSTANCE = Mappers.getMapper( MemberActivityMapper.class);

    MemberActivity memberActivityDtoToMemberActivity( MemberActivityDto aMemberActivityDto);
}
