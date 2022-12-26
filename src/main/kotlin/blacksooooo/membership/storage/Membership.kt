package blacksooooo.membership.storage

import blacksooooo.membership.common.MembershipType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "membership")
class Membership (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = 0L,

    @Column(name = "membership_type", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    val membershipType: MembershipType,

    @Column(name = "user_id", nullable = false)
    val userId: String,

    @Column(name = "point", nullable = false)
    var point: Int = 0,

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "updated_at")
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = LocalDateTime.now()
)