package com.sportevents.comment;

import com.sportevents.auth.AuthService;
import com.sportevents.dto.CommentDto;
import com.sportevents.event.Event;
import com.sportevents.event.EventRepository;
import com.sportevents.exception.NotFoundException;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addComment(Long eventId, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(comment.getContent());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        User user = userRepository.findById(AuthService.getCurrentUserId())
                        .orElseThrow(() -> new NotFoundException("User not found"));

        comment.setAuthor(user);
        comment.setEvent(event);
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByEvent(Long eventId) {
        return commentRepository.findByEvent_EventId(eventId);
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
        commentRepository.delete(comment);
    }
}

