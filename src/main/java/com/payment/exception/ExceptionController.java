package com.payment.exception;

import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GeneralService generalService;

    public ExceptionController(GeneralService generalService) {
        this.generalService = generalService;
    }

    @ExceptionHandler({GeneralException.class})
    public final ResponseEntity<?> handleException(Exception ex) {
        logger.info("Error occurred, error message is {}", ex.getMessage());

        if (ex instanceof GeneralException) {
            String responseCode = ex.getMessage();
            String message = ex.getCause().getMessage();
            logger.info("Specific error message is: Response code => {} and message => {}", responseCode, message);

            Response response = generalService.prepareResponse(ex.getMessage(), ex.getCause().getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Response response = generalService.prepareResponse(ResponseCodeAndMessage.AN_ERROR_OCCURRED_96, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        logger.info("Error occurred during request body validation, error message is {}", ex.getMessage());

        //Get all errors
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(". "));

        Response response;
        response = generalService.prepareResponse(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, errors, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
