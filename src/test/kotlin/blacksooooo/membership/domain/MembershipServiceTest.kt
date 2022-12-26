package blacksooooo.membership.domain

import blacksooooo.membership.common.MembershipErrorResult
import blacksooooo.membership.common.MembershipType
import blacksooooo.membership.exception.MembershipException
import blacksooooo.membership.fixtures.createMembership
import blacksooooo.membership.storage.MembershipRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import java.util.*

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

    Given("userId가 주어졌을 때") {
        val userId = "userId"
        val membership1 = createMembership(userId, MembershipType.KAKAO, 100)
        val membership2 = createMembership(userId, MembershipType.NAVER, 100)
        val membership3 = createMembership(userId, MembershipType.LINE, 100)

        every { repository.findAllByUserId(any()) } returns listOf(
            membership1, membership2, membership3
        )

        When("해당 정보로 조회하면") {
            val actual = sut.getMembershipList(userId)

            Then("멤버십 리스트가 반환된다.") {
                actual shouldHaveSize 3
            }
        }
    }

    Given("멤버십이 존재하지 않을 때") {
        every { repository.findByIdOrNull(any()) } throws MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)

        When("멤버십을 조회하면") {
            Then("예외가 발생한다.") {
                shouldThrow<MembershipException> { sut.getMembership(0L, "userId") }
            }
        }
    }

    Given("멤버십이 본인의 멤버십이 아닐 때") {
        val membership = createMembership("userId", MembershipType.NAVER, 100)

        every { repository.findByIdOrNull(any()) } returns membership

        When("멤버십을 조회하면") {
            Then("예외가 발생한다.") {
                shouldThrow<MembershipException> { sut.getMembership(1L, "anotherId") }
            }
        }
    }

    Given("멤버십 id, userId가 주어졌을 때") {
        val membership = createMembership("userId", MembershipType.NAVER, 100)

        every { repository.findByIdOrNull(any()) } returns membership

        When("멤버십 조회를 하면") {
            val actual = sut.getMembership(0L, "userId")

            Then("해당 멤버십이 조회된다.") {
                actual.membershipType shouldBe MembershipType.NAVER
            }
        }
    }

    Given("멤버십이 존재하지 않을 때") {
        every { repository.findByIdOrNull(any()) } returns null

        When("멤버십 삭제를 하면") {
            Then("예외가 발생한다.") {
                shouldThrow<MembershipException> { sut.removeMembership(0L, "userId") }
            }
        }
    }

    Given("본인의 멤버십이 아닐 때") {
        val membership = createMembership("userId", MembershipType.NAVER, 1000)

        every { repository.findByIdOrNull(any()) } returns membership

        When("멤버십 삭제를 하면") {
            Then("예외가 발생한다.") {
                shouldThrow<MembershipException> { sut.removeMembership(0L,"myId") }
            }
        }
    }

    Given("id, userId가 주어졌을 때") {
        val membership = createMembership("userId", MembershipType.NAVER, 1000)

        every { repository.findByIdOrNull(any()) } returns membership
        every { repository.deleteById(any()) } just Runs

        When("멤버십 삭제를 하면") {
            sut.removeMembership(0L, "userId")
            Then("해당 멤버십이 삭제 된다.") {
                verify(exactly = 1) { repository.deleteById(any()) }
            }
        }
    }
    afterTest { clearAllMocks() }
})