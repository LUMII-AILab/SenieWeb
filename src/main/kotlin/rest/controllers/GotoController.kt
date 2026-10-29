package lv.ailab.senie.rest.controllers

import lv.ailab.senie.db.repositories.BookRepository
import lv.ailab.senie.db.repositories.ContentLineRepository
import lv.ailab.senie.rest.CommonFailures.bookNotFound
import lv.ailab.senie.utils.urlEncode
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Controller class GotoController(
    private val bookRepo: BookRepository,
    private val contentRepo: ContentLineRepository,
) {
    @GetMapping("/goto")
    fun goto(
        @RequestParam("source") source: String?,
        @RequestParam("address") address: String?,
        @RequestParam("page") inputPage: String?,
        @RequestParam("fullText") fullText: Boolean?,
    ): RedirectView {
        val (book, page, line) = when {
            address != null -> {
                // Removes trailing dots because bible verse addresses ending
                // with dots confuse users and they do variants.
                val normalizedAdress = address.trimEnd('.')
                val line = contentRepo.findFirstByAddressOrderByPageSortOrderAscLineSortOrderAsc(normalizedAdress)
                    ?: error("Sistēmā nav rindiņas ar adresi [$normalizedAdress].")
                Triple(line.page.book, line.page.name, line)
            }
            source != null -> {
                val book = bookRepo.findByFullSourceCode(source) ?: throw bookNotFound(source)
                Triple(book, inputPage, null)
            }
            else -> throw Exception("`/goto` adresei nepieciešams `address` vai `source` parametrs.")
        }

        val pathStub =
            if (fullText == true && book.isCollectionItem)
                "/books_fulltext/" + book.fullSourceCode.urlEncode() else book.urlPath

        val target = UriComponentsBuilder
            .fromPath(pathStub)
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .let { if (line != null) it.fragment(line.address) else it }
            .build()
            .toUriString()
        return RedirectView(target)
    }
}