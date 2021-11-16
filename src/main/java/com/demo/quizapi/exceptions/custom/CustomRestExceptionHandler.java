package com.demo.quizapi.exceptions.custom; //package com.alaajo.projectalaajo.client.exception;

import com.demo.quizapi.exceptions.response.ApiError;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
   *
   * @param ex      MissingServletRequestParameterException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
    MissingServletRequestParameterException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    String error = ex.getParameterName() + " parameter is missing";
    return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
  }


  /**
   * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
   *
   * @param ex      HttpMediaTypeNotSupportedException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
    HttpMediaTypeNotSupportedException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
    return buildResponseEntity(
      new ApiError(
        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        builder.substring(0, builder.length() - 2),
        ex
      )
    );
  }

  /**
   * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   *
   * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    ApiError apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
    apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
   *
   * @param ex the ConstraintViolationException
   * @return the ApiError object
   */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolation(
    ConstraintViolationException ex
  ) {
    ApiError apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getConstraintViolations());
    return buildResponseEntity(apiError);
  }
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> handleAuthenticationException(
          AuthenticationException ex,
          HttpServletResponse response
  ) {
    ApiError apiError = new ApiError(BAD_REQUEST, "Bad Credentials", ex);
    apiError.setStatusCode(400);

    return buildResponseEntity(apiError);
  }
  /**
   * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
   *
   * @param ex the EntityNotFoundException
   * @return the ApiError object
   */
  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(
    EntityNotFoundException ex,
    WebRequest requset,
    HttpServletResponse res
  ) {
    ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY);
    apiError.setMessage(ex.getLocalizedMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
   *
   * @param ex the EntityNotFoundException
   * @return the ApiError object
   */
  @ExceptionHandler(EntityAlreadyExistException.class)
  protected ResponseEntity<Object> handleEntityEntityAlreadyExistException(
    EntityAlreadyExistException ex,
    WebRequest requset,
    HttpServletResponse res
  ) {
    ApiError apiError = new ApiError(CONFLICT);
    apiError.setMessage(ex.getLocalizedMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
   *
   * @param ex      HttpMessageNotReadableException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
    HttpMessageNotReadableException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    log.info(
      "{} to {}",
      servletWebRequest.getHttpMethod(),
      servletWebRequest.getRequest().getServletPath()
    );
    String error = "Malformed JSON request";
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
  }

  /**
   * Handle HttpMessageNotWritableException.
   *
   * @param ex      HttpMessageNotWritableException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(
    HttpMessageNotWritableException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    String error = "Error writing JSON output";
    return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
  }

  /**
   * Handle NoHandlerFoundException.
   *
   * @param ex
   * @param headers
   * @param status
   * @param request
   * @return
   */
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
    NoHandlerFoundException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
    ApiError apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(
      String.format(
        "Could not find the %s method for URL %s",
        ex.getHttpMethod(),
        ex.getRequestURL()
      )
    );
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handle javax.persistence.EntityNotFoundException
   */
  @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(
    javax.persistence.EntityNotFoundException ex
  ) {
    return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
  }

  /**
   * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
   *
   * @param ex the DataIntegrityViolationException
   * @return the ApiError object
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  protected ResponseEntity<Object> handleDataIntegrityViolation(
    DataIntegrityViolationException ex,
    WebRequest request
  ) {
    String message;
    if (ex.getCause() instanceof ConstraintViolationException) {
      message = "Database error";
      return buildResponseEntity(
        new ApiError(HttpStatus.CONFLICT, message, ex.getCause())
      );
    }
    if (ex.getCause().getCause() instanceof SQLException) {
      SQLException e = (SQLException) ex.getCause().getCause();

      if (e.getMessage().contains("Key")) {
        message = e.getMessage().substring(e.getMessage().indexOf("Key"));
        return buildResponseEntity(
          new ApiError(HttpStatus.CONFLICT, message, ex.getCause())
        );
      }
    }
    return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
  }

  /**
   * Handle Exception, handle generic Exception.class
   *
   * @param ex the Exception
   * @return the ApiError object
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
    MethodArgumentTypeMismatchException ex,
    WebRequest request
  ) {
    ApiError apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(
      String.format(
        "The parameter '%s' of value '%s' could not be converted to type '%s'",
        ex.getName(),
        ex.getValue(),
        ex.getRequiredType().getSimpleName()
      )
    );
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handle Exception, handle generic Exception.class
   *
   * @param ex the Exception
   * @return the ApiError object
   */
  @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
  protected ResponseEntity<Object> handleMethodArgumentConversionNotSupportedException(
    MethodArgumentTypeMismatchException ex
  ) {
    ApiError apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage("Type conversion error");
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  //  @ExceptionHandler(ResourceNotFoundException.class)
  //  ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
  //    ApiError apiError = new ApiError(NOT_FOUND, "Resource was not found", ex);
  //
  //    return buildResponseEntity(apiError);
  //  }



  @ExceptionHandler(MultipartException.class)
  ResponseEntity<?> handleMultipartExceptions(MultipartException ex) {
    ApiError apiError = new ApiError(BAD_REQUEST, ex);

    return buildResponseEntity(apiError);
  }



  @ExceptionHandler(value = { UnrecognizedPropertyException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleUnrecognizedPropertyException(
    UnrecognizedPropertyException ex
  ) {
    final String error =
      "JSON parse error: Unrecognized field " + "[ " + ex.getPropertyName() + " ]";

    ApiError apiError = new ApiError(BAD_REQUEST, error, ex);

    return buildResponseEntity(apiError);
  }



  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    apiError.setStatusCode(apiError.getStatus().value());
    log.error(
      "message: {}  <==> timestamp: {} <==>  statusCode: {} <==> status: {}",
      apiError.getMessage(),
      apiError.getTimestamp(),
      apiError.getStatusCode(),
      apiError.getStatus()
    );
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
