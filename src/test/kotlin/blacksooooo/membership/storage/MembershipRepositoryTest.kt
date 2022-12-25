package blacksooooo.membership.storage

import blacksooooo.membership.common.MembershipType
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
}