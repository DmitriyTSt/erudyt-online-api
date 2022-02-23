package ru.erudyt.online.dto.model

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class OffsetBasedPageRequest(
    private val offset: Int,
    private val limit: Int,
    private val sort: Sort,
) : Pageable {
    override fun getPageNumber(): Int {
        return offset / limit
    }

    override fun getPageSize(): Int {
        return limit
    }

    override fun getOffset(): Long {
        return offset.toLong()
    }

    override fun getSort(): Sort {
        return sort
    }

    override fun next(): Pageable {
        return OffsetBasedPageRequest(offset + pageSize, pageSize, sort)
    }

    override fun previousOrFirst(): Pageable {
        return if (hasPrevious()) OffsetBasedPageRequest(offset - pageSize, pageSize, sort) else this
    }

    override fun first(): Pageable {
        return OffsetBasedPageRequest(0, pageSize, sort)
    }

    override fun withPage(pageNumber: Int): Pageable {
        return OffsetBasedPageRequest(pageSize * pageNumber, pageSize, sort)
    }

    override fun hasPrevious(): Boolean {
        return offset > limit
    }
}