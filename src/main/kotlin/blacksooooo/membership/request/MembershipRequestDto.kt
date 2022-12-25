package blacksooooo.membership.request

import blacksooooo.membership.common.MembershipType
import javax.validation.constraints.Min

data class MembershipRequestDto(
    @field:Min(0)
    val point: Int,
    val membershipType: MembershipType
)
