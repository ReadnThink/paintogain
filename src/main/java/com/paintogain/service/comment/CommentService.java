package com.paintogain.service.comment;

import com.paintogain.controller.comment.request.CommentCreate;
import com.paintogain.domain.Comment;
import com.paintogain.exception.custom.CommentNotFound;
import com.paintogain.exception.custom.FeedNotFound;
import com.paintogain.exception.custom.Unauthorized;
import com.paintogain.exception.custom.WriterNotMatched;
import com.paintogain.repository.comment.CommentRepository;
import com.paintogain.repository.feed.FeedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    public CommentService(FeedRepository feedRepository, CommentRepository commentRepository) {
        this.feedRepository = feedRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void write(Long feedId, CommentCreate commentCreate) {
        var feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFound::new);

        Comment comment = Comment.builder()
                .author(commentCreate.getAuthor())
                .content(commentCreate.getContent())
                .build();

        feed.addComment(comment);
    }

    public void delete(Long commentId, String commentOwner) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        if (!comment.getAuthor().equals(commentOwner)) {
            throw new WriterNotMatched();
        }

        commentRepository.delete(comment);
    }
}
