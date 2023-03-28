# Bootcamp Dev-Superior
---

## Anotações CRUD - cap 01

### Arquivos de configuração

#### application.properties
```
spring.profiles.active=test

spring.jpa.open-in-view=false
```

#### application-test.properties
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

#### application-dev.properties
```
spring.jpa.properties.javax.persistence.schema-generation.create-source=metadata
spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=create.sql
spring.jpa.properties.hibernate.hbm2ddl.delimiter=;

spring.datasource.url=jdbc:postgresql://localhost:5432/dspesquisa
spring.datasource.username=postgres
spring.datasource.password=1234567

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.hibernate.ddl-auto=none
```

#### application-prod.properties
```
spring.datasource.url=${DATABASE_URL}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```


---
## Anotações Security / OAuth - cap 03
### Classe de configuração
```java
@Configuration
public class AppConfig {

	// Bean para criptografar a senha
	// Exemplo de utilização
	// user.setPassword(passwordEncoder.encode(dto.getPassword()));
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
```
### Configuração provisória para liberar todos endpoints
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// web.ignoring().antMatchers("/actuator/**");  -> Configuração para biblioteca OAuth funcionar
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**");
	}
}
```
---
### Referências sobre Bean Validation

https://beanvalidation.org/

https://docs.jboss.org/hibernate/beanvalidation/spec/2.0/api/overview-summary.html

https://docs.jboss.org/hibernate/beanvalidation/spec/2.0/api/javax/validation/constraints/package-summary.html

https://www.baeldung.com/java-bean-validation-not-null-empty-blank

https://www.baeldung.com/spring-custom-validation-message-source

https://pt.stackoverflow.com/questions/133691/formatar-campo-cpf-ou-cnpj-usando-regex

https://regexlib.com/

https://regexr.com/

---

### ConstraintValidator customizado


##### Annotation
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UserInsertValidator.class)  // aqui a classe validator que implementa a annotation
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface UserInsertValid {
	String message() default "Validation error"; // msg de erro customizada

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

```
##### Validator

```java
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> // param(annotation/class que vai usar a annotation){
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
```

---
### Spring Security

##### Interfaces que devem ser implementadas
* UserDetails
	
```java
@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
                      .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                      .toList();
	} 
```

* UserDetailsService:

    
```java
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		
		if(user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException ("Email not found");
		}
		logger.info("User found: " + username);
		return user;
	}
```

* Essas interfaces são implementadas na entidade 'User' e no serviço 'UserService'

* Classe para configuração de segurança web
	* WebSecurityConfigurerAdapter

* Bean para efetuar autenticação
	* AuthenticationManager
---

### Beans para token JWT

```java
@Bean
public JwtAccessTokenConverter accessTokenConverter() {
	JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
	tokenConverter.setSigningKey("MY-JWT-SECRET");
	return tokenConverter;
}

@Bean
public JwtTokenStore tokenStore() {
	return new JwtTokenStore(accessTokenConverter());
}
```

---
### Spring Cloud OAuth2
* Classe de configuração para Authorization Server
	* AuthorizationServerConfigurerAdapter

* Classe de configuração para Resource Server
	* ResourceServerConfigurerAdapter
		* Liberando o h2-console

```java
private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**" };
	@Autowired
	private Environment env;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//considerando que o profile do application.properties está ativo como "test"
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
			.antMatchers(PUBLIC).permitAll();
```

* Beans para implementar o padrão JWT
	* JwtAccessTokenConverter
	* JwtTokenStore

---
### Logger
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
private static Logger logger = LoggerFactory.getLogger(Exemplo.class);

// Exemplo de aplicação para aparecer no console
logger.error("User not found " + username);
logger.info("User found: " + username);
```
