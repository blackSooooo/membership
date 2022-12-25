package blacksooooo.membership.domain

import blacksooooo.membership.common.MembershipErrorResult
import blacksooooo.membership.common.MembershipType
import blacksooooo.membership.exception.MembershipException
import blacksooooo.membership.response.MembershipResponseDto
import blacksooooo.membership.storage.Membership
import blacksooooo.membership.storage.MembershipRepository
import org.springframework.stereotype.Service

@Service
class MembershipService(
    private val membershipRepository: MembershipRepository
) {
    fun addMembership(
        userId: String,
        membershipType: MembershipType,
        point: Int
    ): MembershipResponseDto {
        val result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType)
        if (result != null) {
            throw MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER)
        }
        val savedMembership = membershipRepository.save(Membership(
            userId = userId,
            membershipType = membershipType,
            point = point
        ))
        return MembershipResponseDto(
            id = savedMembership.id!!,
            membershipType = savedMembership.membershipType
        )
    }

    fun getMembershipList(userId: String): List<MembershipResponseDto> {
        return membershipRepository.findAllByUserId(userId).map {
            MembershipResponseDto(
                id = it.id!!,
                membershipType = it.membershipType
            )
        }
    }
}