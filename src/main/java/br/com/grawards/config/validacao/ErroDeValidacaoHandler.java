package br.com.grawards.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //indico pro Sprint que eh um controller sendo usado numa API Rest 
public class ErroDeValidacaoHandler {

	//para internacionalização
	@Autowired
	private MessageSource messageSource; 
	
	//@ExceptionHandler indico pro Spring que esse metodo deve ser chamado quando houver uma exceção dentro de um Controller
	//indico pro string que é uma validação de erro de formulário
	//exceções de validação de formulário o Spring lança exceções MethodArgumentNotValidException
	// toda vez que ocorrer uma exception desse tipo em qualquer RestController do projeto, o Spring vai chamar esse meu método passando
	//como parâmetro a exception que ocorreu, assim ele não retorna o Erro 400 pro cliente, ele me permite tratar esse erro e assim
	//ele retorna por padrão 200 pro cliente, então eu faço ele retornar o Erro 400 com a mensagem que eu quiser
	
	//@ResponseStatus assim eu digo pra ele que o retorno desse método  é 400 (BAD_REQUEST), e não 200 que seria o default
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDto> handle (MethodArgumentNotValidException exception) {
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		//para cada erro da lista fieldErros, preciso criar um dto para retornar pro cliente
		//java 8: cada objeto da lista ele atribuí para a minha variável "e"
		fieldErrors.forEach(e -> {
			//assim ele pega a mensagem no idioma do locale que o cliente (rest) enviou no request pra pegar a mensagem no idioma correto
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		return dto;		
	}
}
