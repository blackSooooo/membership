package blacksooooo.membership.domain

import blacksooooo.membership.common.MembershipType
import blacksooooo.membership.exception.MembershipException
import blacksooooo.membership.fixtures.createMembership
import blacksooooo.membership.storage.MembershipRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class MembershipServiceTest: BehaviorSpec ({
    val repository = mockk<MembershipRepository>()
    val sut = MembershipService(repository)

    Given("멤버십이 이미 존재할 때") {
        val userId = "user_id"
        val membershipType = MembershipType.NAVER
        val point = 1000
        val membership = createMembership(userId, membershipType, point)

        every { repository.findByUserIdAndMembershipType(any(), any()) } returns membership

        When("해당 멤버십을 저장하게 되면") {
            Then("예외가 발생한다.") {
                shouldThrow<MembershipException> {
                    sut.addMembership(userId, membershipType, point)
                }
            }
        }
    }

    Given("멤버십 정보가 주어졌을 때") {
        val userId = "user_id"
        val membershipType = MembershipType.NAVER
        val point = 1000
        val membership = createMembership(userId, membershipType, point)

        every { repository.findByUserIdAndMembershipType(any(), any()) } returns null
        every { repository.save(any()) } returns membership

        When("해당 멤버십을 저장하게 되면") {
            val actual = sut.addMembership(userId, membershipType, point)

            Then("엔티티가 저장된다.") {
                actual.id shouldNotBe null
                actual.membershipType shouldBe membershipType
                verify(exactly = 1) { repository.findByUserIdAndMembershipType(any(), any()) }
                verify { repository.save(any()) }
            }
        }
    }

    afterTest { clearAllMocks() }
})