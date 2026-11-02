package lv.ailab.senie.db.entities

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import lv.ailab.senie.utils.urlEncode
import java.time.Year

@Entity
@Table(name = "books")
data class Book(
    @Id val fullSourceCode: String,
    val collectionCode: String?,
    val itemCode: String?,
    val name: String,
    val year1: Year,
    val year2: Year,
    val pubCentury: Int,
    @Enumerated(EnumType.STRING)
    val indexType: IndexType?,
    val manuscript: Boolean?,
    val orderInCollection: Float?,
    @ManyToMany(fetch = LAZY)
    @JoinTable(name="books_genres",
            joinColumns= [JoinColumn(name="source", referencedColumnName=FULL_SOURCE_CODE)],
            inverseJoinColumns= [JoinColumn(name="genre_id", referencedColumnName="id")]
        )
    @OrderBy("subgenre, name")
    val genres: List<Genre>
) {
    val isCollection: Boolean
        get() = collectionCode != null && itemCode == null

    val isCollectionItem: Boolean
        get() = collectionCode != null && itemCode != null

    val urlPath: String
        get() = "/${if (isCollection) "collections" else "books"}/" + fullSourceCode.urlEncode()

    val displayCode: String
        get() = itemCode ?: collectionCode ?: throw Exception(
            "Book [$name] has neither a collection nor item code. " +
                "This should not be possible, data corruption likely.",
        )

    val displayTitle: String
        get() = "$name ($displayCode)"

    enum class IndexType { GNP, GLR, LR, P }

    companion object {
        const val FULL_SOURCE_CODE = "fullSourceCode"
    }
}