package blacksooooo.membership.storage

import blacksooooo.membership.common.MembershipType
import blacksooooo.membership.fixtures.createMembership
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class MembershipRepositoryTest {
    @Autowired
    lateinit var membershipRepository: MembershipRepository

    @Test
    fun `멤버십등록`() {
        val membership = Membership(
            userId = "userId",
            membershipType = MembershipType.NAVER,
            point = 10000
        )

        val actual = membershipRepository.save(membership)

        membership.id shouldNotBe null
        actual.userId shouldBe "userId"
        actual.membershipType shouldBe MembershipType.NAVER
        actual.point shouldBe 10000
    }

    @Test
    fun `멤버십_존재_테스트`() {
        val membership = Membership(
            userId = "userId",
            membershipType = MembershipType.NAVER,
            point = 10000
        )

        membershipRepository.save(membership)
        val actual = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER)

        membership.id shouldNotBe null
        actual?.userId shouldBe "userId"
        actual?.membershipType shouldBe MembershipType.NAVER
        actual?.point shouldBe 10000
    }

    @Test
    fun `멤버십 조회_사이즈0`() {
        val result = membershipRepository.findAllByUserId("userId")

        result shouldHaveSize 0
    }

    @Test
    fun `멤버십 조회_사이즈2`() {
        val userId = "userId"
        val membership1 = createMembership(userId, MembershipType.NAVER, 100)
        val membership2 = createMembership(userId, MembershipType.KAKAO, 100)

        membershipRepository.save(membership1)
        membershipRepository.save(membership2)

        val result = membershipRepository.findAllByUserId(userId)

        result shouldHaveSize 2
    }

    @Test
    fun `멤버십 추가 후 삭제`() {
        val membership = createMembership("userId", MembershipType.NAVER, 1000)

        val savedMembership = membershipRepository.save(membership)

        membershipRepository.deleteById(savedMembership.id!!)
    }
}