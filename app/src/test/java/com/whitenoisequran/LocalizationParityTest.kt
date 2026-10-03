package com.whitenoisequran

import org.junit.Assert.assertEquals
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/** Indonesian (values/) is the source; Malay and English must have every key with the same format args. */
class LocalizationParityTest {

    // Unit tests run from the module directory
    private val res = File("src/main/res")

    private fun load(folder: String): Map<String, String> {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(File(res, "$folder/strings.xml"))
        val entries = mutableMapOf<String, String>()
        for (tag in listOf("string", "plurals", "string-array")) {
            val nodes = doc.getElementsByTagName(tag)
            for (i in 0 until nodes.length) {
                val el = nodes.item(i) as Element
                if (el.getAttribute("translatable") == "false") continue
                val items = el.getElementsByTagName("item")
                // Value = format args (plurals: the "other" form) or, for arrays, the item count
                entries["$tag/${el.getAttribute("name")}"] = when (tag) {
                    "string-array" -> "items=${items.length}"
                    "plurals" -> formatArgs(
                        (0 until items.length).map { items.item(it) as Element }
                            .first { it.getAttribute("quantity") == "other" }.textContent
                    )

                    else -> formatArgs(el.textContent)
                }
            }
        }
        return entries
    }

    private fun formatArgs(text: String) =
        Regex("%(\\d+\\$)?[sd]").findAll(text).map { it.value }.sorted().joinToString()

    @Test
    fun translationsMatchDefault() {
        val default = load("values")
        assertEquals("items=114", default["string-array/surah_meanings"])
        for (folder in listOf("values-ms", "values-en")) {
            assertEquals("$folder differs from values", default, load(folder))
        }
    }
}
