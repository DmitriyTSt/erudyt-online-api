/*
Copyright (c) 2007 Zsolt Sz�sz <zsolt at lorecraft dot com>

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package ru.erudyt.online.mapper

import java.util.regex.Pattern

/**
 * Deserializes a serialized PHP data structure into corresponding Java objects. It supports
 * the integer, float, boolean, string primitives that are mapped to their Java
 * equivalent, plus arrays that are parsed into `Map` instances and objects
 * that are represented by [SerializedPhpParser.PhpObject] instances.
 *
 *
 * Example of use:
 * <pre>
 * String input = "O:8:"TypeName":1:{s:3:"foo";s:3:"bar";}";
 * SerializedPhpParser serializedPhpParser = new SerializedPhpParser(input);
 * Object result = serializedPhpParser.parse();
</pre> *
 *
 * The `result` object will be a `PhpObject` with the name "TypeName" and
 * the attribute "foo" = "bar".
 */
class SerializedPhpParser {
    private val input: String
    private var index = 0
    private var assumeUTF8 = true
    private var acceptedAttributeNameRegex: Pattern? = null

    constructor(input: String) {
        this.input = input
    }

    constructor(input: String, assumeUTF8: Boolean) {
        this.input = input
        this.assumeUTF8 = assumeUTF8
    }

    fun parse(): Any? {
        val type = input[index]
        return when (type) {
            'i' -> {
                index += 2
                // Patch Integer/Double for the PHP x64.
                var tmp: Any?
                tmp = parseInt()
                if (tmp == null) {
                    tmp = parseFloat()
                }
                tmp
            }
            'd' -> {
                index += 2
                parseFloat()
            }
            'b' -> {
                index += 2
                parseBoolean()
            }
            's' -> {
                index += 2
                parseString()
            }
            'a' -> {
                index += 2
                parseArray()
            }
            'O' -> {
                index += 2
                parseObject()
            }
            'N' -> {
                index += 2
                NULL
            }
            else -> throw IllegalStateException(
                "Encountered unknown type [" + type
                    + "]"
            )
        }
    }

    private fun parseObject(): Any {
        val phpObject = PhpObject()
        val strLen = readLength()
        phpObject.name = input.substring(index, index + strLen)
        index = index + strLen + 2
        val attrLen = readLength()
        for (i in 0 until attrLen) {
            val key = parse()
            val value = parse()
            if (isAcceptedAttribute(key)) {
                phpObject.attributes[key] = value
            }
        }
        index++
        return phpObject
    }

    private fun parseArray(): Map<Any?, Any?> {
        val arrayLen = readLength()
        val result: MutableMap<Any?, Any?> = LinkedHashMap()
        for (i in 0 until arrayLen) {
            val key = parse()
            val value = parse()
            if (isAcceptedAttribute(key)) {
                result[key] = value
            }
        }
        index++
        return result
    }

    private fun isAcceptedAttribute(key: Any?): Boolean {
        if (acceptedAttributeNameRegex == null) {
            return true
        }
        return if (key !is String) {
            true
        } else acceptedAttributeNameRegex!!.matcher(key as String?).matches()
    }

    private fun readLength(): Int {
        val delimiter = input.indexOf(':', index)
        val arrayLen = Integer.valueOf(input.substring(index, delimiter))
        index = delimiter + 2
        return arrayLen
    }

    /**
     * Assumes strings are utf8 encoded
     *
     * @return
     */
    private fun parseString(): String {
        val strLen = readLength()
        var utfStrLen = 0
        var byteCount = 0
        while (byteCount != strLen) {
            val ch = input[index + utfStrLen++]
            if (assumeUTF8) {
                if (ch.code >= 0x0001 && ch.code <= 0x007F) {
                    byteCount++
                } else if (ch.code > 0x07FF) {
                    byteCount += 3
                } else {
                    byteCount += 2
                }
            } else {
                byteCount++
            }
        }
        val value = input.substring(index, index + utfStrLen)
        index = index + utfStrLen + 2
        return value
    }

    private fun parseBoolean(): Boolean {
        val delimiter = input.indexOf(';', index)
        var value = input.substring(index, delimiter)
        if (value == "1") {
            value = "true"
        } else if (value == "0") {
            value = "false"
        }
        index = delimiter + 1
        return java.lang.Boolean.valueOf(value)
    }

    private fun parseFloat(): Double {
        val delimiter = input.indexOf(';', index)
        val value = input.substring(index, delimiter)
        index = delimiter + 1
        return java.lang.Double.valueOf(value)
    }

    private fun parseInt(): Int? {
        val delimiter = input.indexOf(';', index)
        // Let's store old value of the index for the patch Integer/Double for the PHP x64.
        val index_old = index
        val value = input.substring(index, delimiter)
        index = delimiter + 1
        // Patch Integer/Double for the PHP x64.
        index = try {
            return Integer.valueOf(value)
        } catch (ex: Exception) {
            index_old
        }
        return null
        // End of Patch Integer/Double for the PHP x64.
    }

    fun setAcceptedAttributeNameRegex(acceptedAttributeNameRegex: String?) {
        this.acceptedAttributeNameRegex = Pattern.compile(acceptedAttributeNameRegex)
    }

    /**
     * Represents an object that has a name and a map of attributes
     */
    class PhpObject {
        var name: String? = null
        var attributes: MutableMap<Any?, Any?> = HashMap()
        override fun toString(): String {
            return "\"$name\" : $attributes"
        }
    }

    companion object {
        val NULL: Any = object : Any() {
            override fun toString(): String {
                return "NULL"
            }
        }
    }
}