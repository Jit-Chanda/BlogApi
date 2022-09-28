package com.example.demo.service.impl;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.exception.BlogAPIException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.payload.CommentDTO;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper mapper;

    //Constructor injection
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDTO createComment(Long id, CommentDTO commentDTO) {
        //calling method to convert DTO to entity
        Comment comment = mapToEntity(commentDTO);
        //retrieve post entity by id
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("post", "id", id));
        //set the retrieved post in comment entity
        comment.setPost(post);
        //save the comment entity in the database
        Comment savedComment = commentRepository.save(comment);

        //returning the comment DTO
        return mapToDTO(savedComment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        //retrieve comments for a particular post
        List<Comment> comments = commentRepository.findByPostId(postId);
        //converting list of comments entity into list of comments DTO then, returning it
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        //retrieve post entity by post id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));
        //retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", commentId));
        //checking if the comment belongs to the particular post or not
        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }
        //converting the comment entity into comment DTO and returning it
        return mapToDTO(comment);
    }

    @Override
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO) {
        //retrieve post entity by post id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));
        //retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", commentId));
        //checking if the comment belongs to the particular post or not
        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }
        //update the fields inside comment entity
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setMsg(commentDTO.getMsg());
        //saving the comment entity into database
        Comment updatedComment = commentRepository.save(comment);
        //converting the saved comment entity into comment DTO and, returning it
        return mapToDTO(updatedComment);
    }

    @Override
    public String deleteComment(Long postId, Long commentId) {
        //retrieve post entity by post id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));
        //retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", commentId));
        //checking if the comment belongs to the particular post or not
        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }
        //delete the comment from the database
        commentRepository.delete(comment);
        return "Comment is deleted successfully";
    }

    //convert entity to DTO
    private CommentDTO mapToDTO(Comment comment) {
        //entity to DTO mapping using model mapper
        CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
        //manually doing the entity to DTO mapping
//        CommentDTO commentDTO = new CommentDTO();
//        commentDTO.setId(comment.getId());
//        commentDTO.setName(comment.getName());
//        commentDTO.setEmail(comment.getEmail());
//        commentDTO.setMsg(comment.getMsg());

        return commentDTO;
    }

    //convert DTO to entity
    private Comment mapToEntity(CommentDTO commentDTO) {
        //entity to DTO mapping using model mapper
        Comment comment = mapper.map(commentDTO, Comment.class);
        //manually doing the DTO to entity mapping
//        Comment comment = new Comment();
//        comment.setId(commentDTO.getId());
//        comment.setName(commentDTO.getName());
//        comment.setEmail(commentDTO.getEmail());
//        comment.setMsg(commentDTO.getMsg());

        return comment;
    }
}
