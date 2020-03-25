package kr.ant.booksharing.model;

import lombok.Data;

@Data
public class ModificationPassword {
    private String email;
    private String password;
    private String postPassword;
}
