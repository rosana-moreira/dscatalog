package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Favor entrar um e-mail válido")
    private String email;
    private List<RoleDTO> roles = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email, String password, List<RoleDTO> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
