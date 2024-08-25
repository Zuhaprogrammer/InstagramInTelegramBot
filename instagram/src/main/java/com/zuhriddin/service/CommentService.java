package com.zuhriddin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuhriddin.model.Comment;
import com.zuhriddin.service.util.JsonUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommentService implements BaseService<Comment, UUID> {
    private static final String PATH = "resource/comments.json";

    @Override
    public Comment add(Comment comment) {
        List<Comment> comments = read();
        comments.add(comment);
        write(comments);
        return comment;
    }

    @Override
    public Comment get(UUID id) {
        List<Comment> comments = read();
        return comments.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public List<Comment> list() {
        return read();
    }

    @Override
    public void delete(UUID id) {
        List<Comment> comments = read();
        comments.removeIf(c -> c.getId().equals(id));
        write(comments);
    }

    public Comment update(Comment comment) {
        List<Comment> comments = read();
        comments.stream()
                .filter(comment1 -> comment1.getId().equals(comment.getId()))
                .findFirst()
                .ifPresent(comment1 -> {
                    comment1.setText(comment.getText());
                });
        write(comments);
        return comment;
    }

    public List<Comment> listMainComment(UUID postId) {
        List<Comment> comments = read();
        return comments.stream()
                .filter(comment1 -> comment1.getId().equals(postId) && comment1.getParentId() == null)
                .collect(Collectors.toList());
    }

    public List<Comment> listSubCommentsByCommentId(UUID commentId, UUID postId) {
        List<Comment> comments = read();
        return comments.stream()
                .filter(comment -> commentId.equals(comment.getParentId()) && comment.getPostId().equals(postId))
                .collect(Collectors.toList());
    }

    private List<Comment> read() {
        return JsonUtil.read(PATH, new TypeReference<>() {
        });
    }

    private void write(List<Comment> comments) {
        JsonUtil.write(PATH, comments);
    }
}
