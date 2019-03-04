package mx.com.admoninmuebles.config;

import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class SpringExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(SpringExceptionHandler.class);

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes, Locale locale) {
		
        return "error/error-tamanio-archivo";

	}
	
	@ExceptionHandler({ TransactionSystemException.class })
	public void handleConstraintViolation(Exception ex, WebRequest request) {
	    Throwable cause = ((TransactionSystemException) ex).getRootCause();
	    if (cause instanceof ConstraintViolationException) {
	        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
	        logger.error("Error de contraint violations ", cause);
	    }
	}

}
