package github.com.brunomeloesilva.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.brunomeloesilva.config.security.TokenService;
import github.com.brunomeloesilva.controller.dto.TokenDto;
import github.com.brunomeloesilva.controller.form.LoginForm;

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod", "test"})
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private TokenService TokenService;
	
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm loginForm) {
		
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getSenha());
		
		try {
			
			Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
			String token = TokenService.gerarToken(authenticate);
			
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
			
		} catch(AuthenticationException ex) {
			return ResponseEntity.badRequest().build();
		}
		
	}
}
