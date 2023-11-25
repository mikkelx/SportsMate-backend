package com.sportevents.comment;

import com.sportevents.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event/{eventId}/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<?> addComment(@PathVariable Long eventId, @RequestBody CommentDto commentDto) {
        System.out.println(commentDto.getContent());
        commentService.addComment(eventId, commentDto);
        return new ResponseEntity<>("Comment added successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByEvent(@PathVariable Long eventId) {
        List<Comment> comments = commentService.getCommentsByEvent(eventId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long eventId,
                                           @PathVariable Long commentId,
                                           @RequestBody CommentDto updatedCommentDto) {
        commentService.updateComment(eventId, commentId, updatedCommentDto);
        return new ResponseEntity<>("Comment updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long eventId, @PathVariable Long commentId) {
        commentService.deleteComment(eventId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
