package blacksooooo.membership.response

import blacksooooo.membership.common.MembershipType

data class MembershipResponseDto(
    val id: Long,
    val membershipType: MembershipType
)
