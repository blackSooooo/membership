package blacksooooo.membership.common

import org.springframework.http.HttpStatus

enum class MembershipErrorResult(
    val message: String,
    val status: HttpStatus
) {
    DUPLICATED_MEMBERSHIP_REGISTER("Duplicated Membership Register Request", HttpStatus.BAD_REQUEST),
    MEMBERSHIP_NOT_FOUND("Membership Not found", HttpStatus.NOT_FOUND),
    INVALID_PARAMETER("Invalid parameter", HttpStatus.BAD_REQUEST),
    NOT_MEMBERSHIP_OWNER("Not a membership owner", HttpStatus.BAD_REQUEST),
    UNKNOWN_EXCEPTION("Unknown Exception", HttpStatus.INTERNAL_SERVER_ERROR)
}