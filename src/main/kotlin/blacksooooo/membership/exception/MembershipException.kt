package blacksooooo.membership.exception

import blacksooooo.membership.common.MembershipErrorResult

class MembershipException(
    private val errorResult: MembershipErrorResult
): RuntimeException()