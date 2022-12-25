package blacksooooo.membership.storage

import blacksooooo.membership.common.MembershipType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MembershipRepository: JpaRepository<Membership, Long> {
    fun findByUserIdAndMembershipType(userId: String, membershipType: MembershipType): Membership?
}
