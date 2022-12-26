package blacksooooo.membership.domain

import blacksooooo.membership.common.MembershipErrorResult
import blacksooooo.membership.common.MembershipType
import blacksooooo.membership.exception.MembershipException
import blacksooooo.membership.response.MembershipResponseDto
import blacksooooo.membership.storage.Membership
import blacksooooo.membership.storage.MembershipRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MembershipService(
    private val membershipRepository: MembershipRepository,
    private val ratePointService: PointService
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

    fun getMembership(id: Long, userId: String): MembershipResponseDto {
        return membershipRepository.findByIdOrNull(id)?.let {
            if (it.userId != userId) {
                throw MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)
            }
            MembershipResponseDto(
                id = it.id!!,
                membershipType = it.membershipType
            )
        } ?: throw MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)
    }

    fun removeMembership(id: Long, userId: String) {
        membershipRepository.findByIdOrNull(id)?.let {
            if(it.userId != userId) {
                throw MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER)
            }
            membershipRepository.deleteById(id)
        }?: throw MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)
    }

    @Transactional
    fun accumulateMembershipPoint(id: Long, userId: String, amount: Int) {
        membershipRepository.findByIdOrNull(id)?.apply {
            if(this.userId != userId) {
                throw MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER)
            }
            this.point += ratePointService.calculateAmount(amount)
        }?: throw MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)
    }
}