package com.example.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
@Data
public class CommentDto extends Comment{
    //子评论
    private boolean flag = false;

    private String usrName;

    private String img;
}
