package com.ead.user.controller;


import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.user.Dto.UserDto;
import com.ead.user.enums.UserStatus;
import com.ead.user.enums.UserType;
import com.ead.user.model.UserModel;
import com.ead.user.servicies.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;



@Log4j2  // com essa anotacao não precisa criar instancia logger
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
	
	
//	Logger logger = LoggerFactory.getLogger(AuthenticationController.class); //criando a instancia para log usando o looger factory .class
//	Logger logger = LogManager.getLogger(AuthenticationController.class); // log4j2
	
	
	
	@Autowired
	UserService userService; //ha a necessidade de criare um dto exclusio para valodar os dados de entrada e depois converter em usar model.

	
	@PostMapping("/signup")
	public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
			                                          @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){ // é necessario coverter userDto em userModel para passar o daddos completos
		
		//montando um logo para mostrar a vinda do dto
		   log.debug("Post registerUser userDto received {}",userDto.toString()); // usando as chaves para trazer diferentes tipos de contextos e não somente tipo primitivo com usando %
				                                                                       // o metodo + o nome ...
		
		if(userService.existByUserName(userDto.getUserName())) {
			log.warn("Username {} is Already Taken ", userDto.getUserName());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario ja existente favor verificar campo preenchidos");
		}
		if(userService.existByEmail(userDto.getEmail())) {
			log.warn("Email {} is Already Taken ", userDto.getEmail());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email ja existente favor verificar campo preenchidos");
		}
		
	var userModel = new UserModel();
	BeanUtils.copyProperties(userDto, userModel) ;// usando o benautils para conversao 
	//preenchendo os campos faltantes no userModel;
	userModel.setUserStatus(UserStatus.ACTIVE);
	userModel.setUserType(UserType.STUDENT);
	userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
	userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
	userService.save(userModel);
	return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	
	}
	
	
	// esses strater logging é padrão no spring /para usar o godForJ2 deve excluir no pom.xml Tag: </exclusion> starter-logging 
	//criando um metodo coo ex para ver logs // no arquivo application.yaml outra maneira de definif logs
//	@RequestMapping("/") //logger é usado quando vc cria a instancia na mao sem  a anotacao do lombok.
/*	public String index() {
		 logger.trace("trace"); // é usando para um maior granularidade(muito detalhe)
		 logger.debug("debug"); // é usado para ambiente de dev (acinamento de metodos) 
		 logger.info("Info");  // é usado em prod para ter u controle e não tantod detalhes  (default) o srping ja traz
		 logger.warn("warn"); // traz avisos de processos     (default)
		 logger.error("error"); // // traz os erros boas praticas inceri-los nos blocos try catch 
		
		return " logging Spring";    
		                                //caminho do projeto                                                                       //pacote
		// mostrando o log pelo maven : C:\projetos\projetos_cursos\EAD>mvn spring-boot:run Dspring-boot.arguments=--logging.level.com.ead=trace
	}
*/	
	// com lombok
	@GetMapping("/")
	public String index() {
		 log.trace("trace"); // é usando para um maior granularidade(muito detalhe)
		 log.debug("debug"); // é usado para ambiente de dev (acinamento de metodos) 
		 log.info("Info");  // é usado em prod para ter u controle e não tantod detalhes  (default) o srping ja traz
		 log.warn("warn"); // traz avisos de processos     (default)
		 log.error("error"); // // traz os erros boas praticas inceri-los nos blocos try catch 
		
		 return "Logging Spring Boot..."; 
	}
	
}	
