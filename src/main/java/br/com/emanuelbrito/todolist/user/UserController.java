package br.com.emanuelbrito.todolist.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private IUserRepository userRepository;

	@PostMapping("/")
	public ResponseEntity create(@RequestBody UserModel userModel) {
		var user =  this.userRepository.findByUsername(userModel.getUsername());
		
		if(user != null) {
			System.out.println("Usuário já existente.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existente.");
		}
		var passordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
		
		userModel.setPassword(passordHashred);
		
		var userCreated = this.userRepository.save(userModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
	}

	@GetMapping(" ")
	public List<UserModel> userTest() {
		var users = this.userRepository.findAll();
		return users;
	}

}
