package com.paintogain.config.security;

import com.paintogain.exception.custom.FeedNotFound;
import com.paintogain.repository.FeedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final FeedRepository feedRepository;

    public CustomPermissionEvaluator(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();

        var feed = feedRepository.findById((Long) targetId)
                .orElseThrow(FeedNotFound::new);

        if (!feed.getUserId().equals(userPrincipal.getUserId())) {
            log.info("[인가실패] 해당 사용자가 작성한 글이 아닙니다. targetId={} userId={}", targetId, feed.getUserId());
            return false;
        }
        return true;
    }
}
