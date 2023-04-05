package xyz.spoonmap.server.relation.service;

import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;
import xyz.spoonmap.server.relation.dto.response.FollowResponse;
import xyz.spoonmap.server.relation.dto.response.FollowerResponse;

public interface RelationService {

    FollowResponse retrieveFollows(UserDetails userDetails);

    FollowerResponse retrieveFollowers(UserDetails userDetails);

    FollowAddResponse requestFollow(UserDetails userDetails, Long receiverId);

    FollowAddResponse acceptFollow(Long senderId, UserDetails userDetails);

}
