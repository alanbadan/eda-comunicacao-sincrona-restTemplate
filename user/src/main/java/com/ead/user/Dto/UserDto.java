package com.ead.user.Dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ead.user.Dto.UserDto.UserView.ImagePut;
import com.ead.user.Dto.UserDto.UserView.PasswordPut;
import com.ead.user.Dto.UserDto.UserView.RegistrationPost;
import com.ead.user.validation.UserNameConstraints;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto { //dto para recebimento de dados do cliente por post
	
	//interface jsonView
	public interface UserView{
		public static interface RegistrationPost{}
		public static interface UserPut{}
		public static interface PasswordPut{}
		public static interface ImagePut{}
	}
	
	@NotBlank(groups = RegistrationPost.class)
	@JsonView(UserView.RegistrationPost.class)
	@Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
	@UserNameConstraints(group = RegistrationPost.class)//naotacao criada no pacote validTION
	private String userName;
	
	@NotBlank(groups = RegistrationPost.class)
	@JsonView(UserView.RegistrationPost.class)
	@Email(groups = RegistrationPost.class)
	private String email;
	
	@NotBlank
	@Size(min = 6, max = 20,  groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
	@JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
	private String password;
	
	@NotBlank(groups = PasswordPut.class)
	@JsonView(UserView.PasswordPut.class)
	@Size(min = 4, max = 50, groups = PasswordPut.class)
	private String oldPassword;
	
	@NotBlank
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String fullName;
	
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String phoneNumber;
	
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String cpf;
	
	@NotBlank(groups = ImagePut.class)
	@JsonView(UserView.ImagePut.class)
	private String imagemUrl;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getImagemUrl() {
		return imagemUrl;
	}
	public void setImagemUrl(String imagemUrl) {
		this.imagemUrl = imagemUrl;
	}
	
}
