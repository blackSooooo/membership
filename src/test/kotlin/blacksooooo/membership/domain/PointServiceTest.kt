package blacksooooo.membership.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class PointServiceTest: BehaviorSpec ({
    val sut = RatePointService()

    Given("amount가 주어졌을 때") {
        When("1%를 기준으로 포인트 계산을 하게 되면") {
            Then("기준에 맞게 계산된다.") {
                val amount = 20000
                val actual = sut.calculateAmount(amount)
                actual shouldBe 200
            }
        }
    }
})