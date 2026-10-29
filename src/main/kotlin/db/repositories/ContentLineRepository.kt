package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.ContentLine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ContentLineRepository : CrudRepository<ContentLine, Int> {
    fun findFirstByAddressOrderByPageSortOrderAscLineSortOrderAsc(address: String): ContentLine?

    fun findAllByPageBookFullSourceCodeAndPageSortOrderIn(
        source: String,
        pageOrders: Iterable<Int>,
    ): List<ContentLine>

    // The same as JPA's provided
    // "findAllByPageBookFullSourceCodeOrderByPageSortOrderAscLineSortOrderAsc"
    // but less awkward in use.
    @Query("SELECT cl FROM ContentLine cl WHERE cl.page.book.fullSourceCode = :code " +
            "ORDER BY cl.page.sortOrder, cl.lineSortOrder")
    fun findAllBySourceSortByNaturalOrder(
        @Param("code") source: String,
    ): List<ContentLine>
}