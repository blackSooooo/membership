package blacksooooo.membership.controller

import blacksooooo.membership.common.MembershipConstants.Companion.USER_ID_HEADER
import blacksooooo.membership.domain.MembershipService
import blacksooooo.membership.request.MembershipRequestDto
import blacksooooo.membership.response.MembershipResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class MembershipController(
    private val membershipService: MembershipService
) {

    @PostMapping("/api/v1/memberships")
    fun addMembership(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @RequestBody @Valid body: MembershipRequestDto
    ): ResponseEntity<MembershipResponseDto> {
        val result = membershipService.addMembership(userId, body.membershipType, body.point)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(result)
    }

    @GetMapping("/api/v1/memberships")
    fun getMemberships(
        @RequestHeader(USER_ID_HEADER) userId: String
    ): ResponseEntity<List<MembershipResponseDto>> {
        return ResponseEntity.ok(membershipService.getMembershipList(userId))
    }
}