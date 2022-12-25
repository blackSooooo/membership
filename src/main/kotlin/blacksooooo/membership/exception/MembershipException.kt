package blacksooooo.membership.exception

import blacksooooo.membership.common.MembershipErrorResult

class MembershipException(
    val errorResult: MembershipErrorResult
): RuntimeException()