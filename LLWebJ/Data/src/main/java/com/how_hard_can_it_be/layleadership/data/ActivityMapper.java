package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityMapper
{
    ActivityMapper INSTANCE = Mappers.getMapper( ActivityMapper.class);

    Activity activityDtoToActivity(ActivityDto anActivityDto);
}
