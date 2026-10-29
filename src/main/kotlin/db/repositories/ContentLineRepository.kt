package lv.ailab.senie.db.repositories

import lv.ailab.senie.db.entities.ContentLine
import org.springframework.data.repository.CrudRepository

interface ContentLineRepository : CrudRepository<ContentLine, Int> {
    fun findFirstByAddressOrderByPageSortOrderAscLineSortOrderAsc(address: String): ContentLine?

    fun findAllByPageBookFullSourceCodeAndPageSortOrderIn(
        source: String,
        pageOrders: Iterable<Int>,
    ): List<ContentLine>

    fun findAllByPageBookFullSourceCodeOrderByPageSortOrderAscLineSortOrderAsc(
        source: String,
    ): List<ContentLine>
}