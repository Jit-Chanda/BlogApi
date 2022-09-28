package com.example.demo.service.impl;

import com.example.demo.payload.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.payload.PostResponse;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;

    //Constructor injection
    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper)
    {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        //calling method to convert DTO to entity
        Post post = mapToEntity(postDTO);
        //calling method from repository layer to save the entity into database
        Post createdPost = postRepository.save(post);
        //calling method to convert entity to DTO
        PostDTO response = mapToDTO(createdPost);
        //returning the response into controller layer
        return response;
    }

    @Override
    public PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        //checking the sortDir value and setting the sort direction accordingly
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        //create pageable instance
        //if we want only pagination
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        //create pageable instance
        //if we want pagination as well as sorting functionality
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        //there are multiple findAll() implementation, I'm using the one which takes pageable instance as input
        //this findAll(pageable) will return a page of posts, so, we need to store the result in Page<Post> variable
        Page<Post> posts = postRepository.findAll(pageable);
        //get content from page object and store into list
        List<Post> listOfPost = posts.getContent();
        //converting the post entity to post DTO
        List<PostDTO> content =  listOfPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        //creating actual post response
        PostResponse response = new PostResponse();
        response.setContent(content);
        response.setPageNo(posts.getNumber());
        response.setPageSize(posts.getSize());
        response.setTotalElements(posts.getTotalElements());
        response.setTotalPages(posts.getTotalPages());
        response.setLast(posts.isLast());

        return response;
    }

    @Override
    public PostDTO getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post", "id", id));
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        Post updatedPost = postRepository.save(post);
        PostDTO response = mapToDTO(updatedPost);
        return response;
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
        postRepository.deleteById(id);
    }

    //convert entity to DTO
    private PostDTO mapToDTO(Post post) {
        //entity to DTO mapping using model mapper
        PostDTO response = mapper.map(post, PostDTO.class);
        //manually doing the entity to DTO mapping
//        PostDTO response = new PostDTO();
//        response.setId(post.getId());
//        response.setTitle(post.getTitle());
//        response.setDescription(post.getDescription());
//        response.setContent(post.getContent());
        return response;
    }

    //convert DTO to entity
    private Post mapToEntity(PostDTO postDTO) {
        //entity to DTO mapping using model mapper
        Post response = mapper.map(postDTO, Post.class);
        //manually doing the DTO to entity mapping
//        Post response = new Post();
//        response.setContent(postDTO.getContent());
//        response.setDescription(postDTO.getDescription());
//        response.setTitle(postDTO.getTitle());
        return response;
    }
}
