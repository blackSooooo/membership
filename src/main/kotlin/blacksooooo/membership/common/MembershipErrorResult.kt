package blacksooooo.membership.common

import org.springframework.http.HttpStatus

enum class MembershipErrorResult(
    message: String,
    status: HttpStatus
) {
    DUPLICATED_MEMBERSHIP_REGISTER("Duplicated Membership Register Request", HttpStatus.BAD_REQUEST)
}