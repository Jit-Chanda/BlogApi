package com.example.demo.service;

import com.example.demo.payload.PostDTO;
import com.example.demo.payload.PostResponse;

import java.util.List;

public interface PostService {

    public PostDTO createPost(PostDTO postDTO);
    public PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir);
    public PostDTO getPost(Long id);
    public PostDTO updatePost(PostDTO postDTO, Long id);
    public void deletePost(Long id);
}
