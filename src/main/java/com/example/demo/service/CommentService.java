package com.example.demo.service;

import com.example.demo.payload.CommentDTO;

import java.util.List;

public interface CommentService {

    public CommentDTO createComment(Long id, CommentDTO commentDTO);
    public List<CommentDTO> getCommentsByPostId(Long postId);
    public CommentDTO getCommentById(Long postId, Long commentId);
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO);
    public String deleteComment(Long postId, Long commentId);
}

