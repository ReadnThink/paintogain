package com.paintogain.config.data;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSession {
    public final long id;

    @Builder
    public UserSession(long id) {
        this.id = id;
    }
}
