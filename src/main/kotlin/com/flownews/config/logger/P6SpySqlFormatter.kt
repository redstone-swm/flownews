package com.flownews.config.logger

import com.p6spy.engine.common.P6Util
import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import java.util.Locale

class P6SpySqlFormatter : MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String,
    ): String {
        val formattedSql = formatSql(category, sql)
        return "$now | ${elapsed}ms | $category | connection$connectionId$formattedSql"
    }

    private fun formatSql(
        category: String,
        sql: String,
    ): String {
        if (sql.isBlank()) return ""

        // Only format Statement, distinguish DDL and DML
        return if (Category.STATEMENT.name == category) {
            val trimmedSql = sql.trim().lowercase(Locale.ROOT)
            val formatted =
                when {
                    trimmedSql.startsWith("create") ||
                        trimmedSql.startsWith("alter") ||
                        trimmedSql.startsWith("comment") -> {
                        FormatStyle.DDL.formatter.format(sql)
                    }
                    else -> {
                        FormatStyle.BASIC.formatter.format(sql)
                    }
                }
            "\n$formatted"
        } else {
            " | ${P6Util.singleLine(sql)}"
        }
    }
}
