package aplicacao.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import aplicacao.exception.ValidationException;


public abstract class ValidatorHelper {
	
    public static void validar(Object obj) throws ValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<Object>> violations = factory.getValidator().validate(obj);
        String erro = "";
        List<String> mensagens = new ArrayList<String>();
        for (ConstraintViolation<Object> violation : violations) {
            erro = erro.concat(violation.getMessage()).concat("\n");
            mensagens.add(violation.getMessage());
        }
       
        if(!erro.isEmpty())
            throw new ValidationException(erro, mensagens);
    }
    
}