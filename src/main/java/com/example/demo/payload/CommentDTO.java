package com.example.demo.payload;

import lombok.*;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private long id;
    private String name;
    private String email;
    private String msg;
}
