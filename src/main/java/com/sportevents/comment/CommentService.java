package com.sportevents.comment;

import com.sportevents.auth.AuthService;
import com.sportevents.dto.CommentDto;
import com.sportevents.event.Event;
import com.sportevents.event.EventRepository;
import com.sportevents.exception.NotFoundException;
import com.sportevents.exception.RequestException;
import com.sportevents.notification.NotificationService;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final NotificationService notificationService;

    @Autowired
    public CommentService(CommentRepository commentRepository, EventRepository eventRepository,
                          UserRepository userRepository, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void addComment(Long eventId, CommentDto commentDto) {
        if(commentDto.getContent().length() > 300) {
            throw new RequestException("Comment is too long");
        }

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        User user = userRepository.findById(AuthService.getCurrentUserId())
                        .orElseThrow(() -> new NotFoundException("User not found"));

        comment.setAuthor(user);
        comment.setEvent(event);
        commentRepository.save(comment);
    }

    public List<CommentDto> getCommentsByEvent(Long eventId) {
        return commentRepository.findByEvent_EventId(eventId).stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setCommentId(comment.getCommentId());
                    commentDto.setContent(comment.getContent());
                    commentDto.setAuthorUsername(comment.getAuthor().getUsername());
                    commentDto.setEventId(eventId);
                    commentDto.setAuthorId(comment.getAuthor().getUserId());
                    return commentDto;
                })
                .toList();
    }

    @Transactional
    public void updateComment(Long eventId, Long commentId, CommentDto updatedCommentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getEvent().getEventId().equals(eventId)) {
            throw new RuntimeException("Comment does not belong to the event");
        }
        comment.setContent(updatedCommentDto.getContent());
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long eventId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getEvent().getEventId().equals(eventId)) {
            throw new RuntimeException("Comment does not belong to the event");
        }

        if(comment.getAuthor().getUserId() != AuthService.getCurrentUserId() ) {
            if(AuthService.isAdmin()) {
                commentRepository.delete(comment);
                notificationService.notifyUserOfDeletedComment(comment.getAuthor().getUserId(), eventId);
                return;
            }
            throw new RequestException("You are not the author of this comment");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void deleteAllCommentsByEventId(Long eventId) {
        commentRepository.deleteAllByEvent_EventId(eventId);
    }

}

