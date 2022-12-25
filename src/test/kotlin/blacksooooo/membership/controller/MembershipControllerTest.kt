package blacksooooo.membership.controller

import blacksooooo.membership.common.MembershipConstants.Companion.USER_ID_HEADER
import blacksooooo.membership.common.MembershipErrorResult
import blacksooooo.membership.common.MembershipType
import blacksooooo.membership.domain.MembershipService
import blacksooooo.membership.exception.MembershipException
import blacksooooo.membership.request.MembershipRequestDto
import blacksooooo.membership.response.MembershipResponseDto
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.stream.Stream

@TestInstance(PER_CLASS)
internal class MembershipControllerTest {
    lateinit var gson: Gson
    lateinit var mockMvc: MockMvc
    private val service = mockk<MembershipService>()
    private val sut = MembershipController(service)

    @BeforeEach
    fun setup() {
        gson = Gson()
        mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setControllerAdvice(GlobalExceptionAdvice::class.java)
            .build()
    }

    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    fun `멤버십 등록 실패_잘못된 파라미터`(point: Int, membershipType: MembershipType) {
        val path = "/api/v1/memberships"

        val request = mockMvc.perform(
            MockMvcRequestBuilders.post(path)
                .header(USER_ID_HEADER, "1234")
                .content(gson.toJson(MembershipRequestDto(point, membershipType)))
                .contentType(MediaType.APPLICATION_JSON)
        )

        request.andExpect(status().isBadRequest)
    }

    @Test
    fun `멤버십 등록 실패_사용자 식별값이 헤더에 없음`() {
        val path = "/api/v1/memberships"

        val request = mockMvc.perform(
            MockMvcRequestBuilders.post(path)
                .content(gson.toJson(MembershipRequestDto(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )

        request.andExpect(status().isBadRequest)
    }

    @Test
    fun `멤버십 등록 실패_service에서 예외 throw`() {
        val path = "/api/v1/memberships"

        every { service.addMembership(any(), any(), any()) } throws MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER)

        val request = mockMvc.perform(
            MockMvcRequestBuilders.post(path)
                .header(USER_ID_HEADER, "1234")
                .content(gson.toJson(MembershipRequestDto(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )

        request.andExpect(status().isBadRequest)
    }

    @Test
    fun `멤버십 등록 성공`() {
        val path = "/api/v1/memberships"

        every { service.addMembership(any(), any(), any()) } returns MembershipResponseDto(0L, MembershipType.NAVER)

        val request = mockMvc.perform(
            MockMvcRequestBuilders.post(path)
                .header(USER_ID_HEADER, "1234")
                .content(gson.toJson(MembershipRequestDto(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )

        request.andExpect(status().isCreated)

        val response = gson.fromJson(request.andReturn()
            .response
            .contentAsString, MembershipResponseDto::class.java)

        assertThat(response.membershipType).isEqualTo(MembershipType.NAVER)
        assertThat(response.id).isNotNull
    }

    @Test
    fun `멤버십 목록 조회 실패_사용자 식별값이 헤더에 없음`() {
        val path = "/api/v1/memberships"

        val request = mockMvc.perform(
            MockMvcRequestBuilders.get(path)
        )

        request.andExpect(status().isBadRequest)
    }

    @Test
    fun `멤버십 목록 조회 성공`() {
        val path = "/api/v1/memberships"

        every { service.getMembershipList(any()) } returns listOf(
            MembershipResponseDto(0L, MembershipType.LINE)
        )

        val request = mockMvc.perform(
            MockMvcRequestBuilders.get(path)
                .header(USER_ID_HEADER, "1234")
        )

        request.andExpect(status().isOk)
    }

    @Test
    fun `멤버십 조회 실패_사용자 식별값이 헤더에 없음`() {
        val path = "/api/v1/memberships/1"

        val request = mockMvc.perform(
            MockMvcRequestBuilders.get(path)
        )

        request.andExpect(status().isBadRequest)
    }

    @Test
    fun `멤버십 조회 실패_멤버십 존재하지 않음`() {
        val path = "/api/v1/memberships/1"

        every { service.getMembership(any(), any()) } throws MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)

        val request = mockMvc.perform(
            MockMvcRequestBuilders.get(path)
                .header(USER_ID_HEADER, "1234")
        )

        request.andExpect(status().isNotFound)
    }

    @Test
    fun `멤버십 조회 성공`() {
        val path = "/api/v1/memberships/1"

        every { service.getMembership(any(), any()) } returns MembershipResponseDto(1, MembershipType.NAVER)

        val request = mockMvc.perform(
            MockMvcRequestBuilders.get(path)
                .header(USER_ID_HEADER, "1234")
        )

        val response = gson.fromJson(request.andReturn()
            .response
            .contentAsString, MembershipResponseDto::class.java)

        request.andExpect(status().isOk)

        assertThat(response.membershipType).isEqualTo(MembershipType.NAVER)
    }

    private fun invalidMembershipAddParameter(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(-1, MembershipType.NAVER),
        )
    }
}
