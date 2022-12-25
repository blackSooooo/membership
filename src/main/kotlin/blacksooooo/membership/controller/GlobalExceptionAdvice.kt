package blacksooooo.membership.controller

import blacksooooo.membership.common.ErrorResponse
import blacksooooo.membership.common.MembershipErrorResult
import blacksooooo.membership.exception.MembershipException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionAdvice {

    @ExceptionHandler(MembershipException::class)
    fun handleMembershipException(exception: MembershipException): ResponseEntity<ErrorResponse> {
        val errorResult = exception.errorResult
        return ResponseEntity.status(errorResult.status)
            .body(ErrorResponse(errorResult.name, errorResult.message))
    }

    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        MissingRequestHeaderException::class
    )
    fun handleInvalidParameter(exception: Exception): ResponseEntity<ErrorResponse> {
        val errorResult = MembershipErrorResult.INVALID_PARAMETER
        return ResponseEntity.status(errorResult.status)
            .body(ErrorResponse(errorResult.name, errorResult.message))
    }
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        val errorResult = MembershipErrorResult.UNKNOWN_EXCEPTION
        return ResponseEntity.status(errorResult.status)
            .body(ErrorResponse(errorResult.name, errorResult.message))
    }
}