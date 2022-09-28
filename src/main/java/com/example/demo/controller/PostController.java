package com.example.demo.controller;

import com.example.demo.payload.PostDTO;
import com.example.demo.payload.PostResponse;
import com.example.demo.service.PostService;
import com.example.demo.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create new blog post
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    //get all blog posts
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PostResponse posts = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //get a specific blog post
    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable long id) {
        PostDTO post = postService.getPost(id);
        return post;
    }

    //update a specific blog post
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @RequestBody PostDTO postDTO,
            @PathVariable Long id) {
        PostDTO updatedPost = postService.updatePost(postDTO, id);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    //delete a specific blog post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return  new ResponseEntity<>("The post has been deleted successfully", HttpStatus.OK);
    }
}
