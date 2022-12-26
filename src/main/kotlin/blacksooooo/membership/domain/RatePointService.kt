package blacksooooo.membership.domain

import org.springframework.stereotype.Service

@Service
class RatePointService: PointService {
    override fun calculateAmount(amount: Int): Int {
        return amount * POINT_RATE / 100
    }

    companion object {
        const val POINT_RATE = 1
    }
}