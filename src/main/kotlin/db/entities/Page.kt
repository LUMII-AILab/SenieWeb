package lv.ailab.senie.db.entities

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import lv.ailab.senie.utils.urlEncode
import java.time.Instant

@Entity
@Table(name = "pages")
data class Page(
    @Id val id: Int,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "source", referencedColumnName = Book.FULL_SOURCE_CODE)
    val book: Book,

    val name: String?,
    val sortOrder: Int,
    val facsimileFilename: String?,
    val facsimileCheckedOn: Instant?,
) {

    val linkName: String?
        get() = if (sortOrder == 0 && name == null) "_" else name

    val encodedLinkName: String?
        get() = linkName?.urlEncode()

    val displayName: String?
        get() = if (linkName == "_") "Titullapa" else name
}