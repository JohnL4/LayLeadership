package com.how_hard_can_it_be.layleadership.data;

import com.how_hard_can_it_be.layleadership.business.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * TODO: move to somewhere else (Svc?).  Maybe mapping shouldn't be in the Data module, and we can just let Data handle
 * d/b access.
 */
@Mapper
public interface MemberMapper
{
    MemberMapper INSTANCE = Mappers.getMapper( MemberMapper.class);

//    @Mapping( source = "_id", target = "_id" )
//    @Mapping( source = "_lastName", target = "_lastName" )
    Member memberDtoToMember( MemberDto aMemberDto );
}
