package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.ContentLine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/**
 * Natural order for content lines is the order they in which they fallow each
 * other in text, that is, sorted first by page sort order and then by line
 * sort order.
 */
interface ContentLineRepository : CrudRepository<ContentLine, Int> {

    @Query("SELECT cl FROM ContentLine cl WHERE cl.address = :address " +
            "ORDER BY cl.page.sortOrder, cl.lineSortOrder LIMIT 1")
    fun findFirstByAddressSortByNaturalOrder(
        @Param("address") address: String
    ): ContentLine?

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
        @Param("code") source: String
    ): List<ContentLine>
}