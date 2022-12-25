package blacksooooo.membership.fixtures

import blacksooooo.membership.common.MembershipType
import blacksooooo.membership.storage.Membership

fun createMembership(
    userId: String,
    membershipType: MembershipType,
    point: Int
): Membership {
    return Membership(
        userId = userId,
        membershipType = membershipType,
        point = point
    )
}