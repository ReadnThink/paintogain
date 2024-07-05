package com.paintogain.controller.comment;

import com.paintogain.config.security.UserPrincipal;
import com.paintogain.controller.comment.request.CommentCreate;
import com.paintogain.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/feeds/{feedId}/comments")
    public void write(@PathVariable Long feedId, @RequestBody @Valid CommentCreate commentCreate) {
        commentService.write(feedId, commentCreate);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/comments/{commentId}")
    public void delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId) {
        commentService.delete(commentId, userPrincipal.getUsername());
    }
}
