package com.yhs0602.dex

data class MapItem(
    val type: Int,
    val unused: Int,
    val size: Int,
    val offset: Int
) {
    companion object {
        const val TYPE_HEADER_ITEM = 0
        const val TYPE_STRING_ID_ITEM = 1
        const val TYPE_TYPE_ID_ITEM = 2
        const val TYPE_PROTO_ID_ITEM = 3
        const val TYPE_FIELD_ID_ITEM = 4
        const val TYPE_METHOD_ID_ITEM = 5
        const val TYPE_CLASS_DEF_ITEM = 6
        const val TYPE_CALL_SITE_ID_ITEM = 7
        const val TYPE_METHOD_HANDLE_ITEM = 8
        const val TYPE_MAP_LIST = 0x1000
        const val TYPE_TYPE_LIST = 0x1001
        const val TYPE_ANNOTATION_SET_REF_LIST = 0x1002
        const val TYPE_ANNOTATION_SET_ITEM = 0x1003
        const val TYPE_CLASS_DATA_ITEM = 0x2000
        const val TYPE_CODE_ITEM = 0x2001
        const val TYPE_STRING_DATA_ITEM = 0x2002
        const val TYPE_DEBUG_INFO_ITEM = 0x2003
        const val TYPE_ANNOTATION_ITEM = 0x2004
        const val TYPE_ENCODED_ARRAY_ITEM = 0x2005
        const val TYPE_ANNOTATIONS_DIRECTORY_ITEM = 0x2006
        const val TYPE_HIDDENAPI_CLASS_DATA_ITEM = 0xf000

        private val typeNameMap = mapOf(
            TYPE_HEADER_ITEM to "TYPE_HEADER_ITEM",
            TYPE_STRING_ID_ITEM to "TYPE_STRING_ID_ITEM",
            TYPE_TYPE_ID_ITEM to "TYPE_TYPE_ID_ITEM",
            TYPE_PROTO_ID_ITEM to "TYPE_PROTO_ID_ITEM",
            TYPE_FIELD_ID_ITEM to "TYPE_FIELD_ID_ITEM",
            TYPE_METHOD_ID_ITEM to "TYPE_METHOD_ID_ITEM",
            TYPE_CLASS_DEF_ITEM to "TYPE_CLASS_DEF_ITEM",
            TYPE_CALL_SITE_ID_ITEM to "TYPE_CALL_SITE_ID_ITEM",
            TYPE_METHOD_HANDLE_ITEM to "TYPE_METHOD_HANDLE_ITEM",
            TYPE_MAP_LIST to "TYPE_MAP_LIST",
            TYPE_TYPE_LIST to "TYPE_TYPE_LIST",
            TYPE_ANNOTATION_SET_REF_LIST to "TYPE_ANNOTATION_SET_REF_LIST",
            TYPE_ANNOTATION_SET_ITEM to "TYPE_ANNOTATION_SET_ITEM",
            TYPE_CLASS_DATA_ITEM to "TYPE_CLASS_DATA_ITEM",
            TYPE_CODE_ITEM to "TYPE_CODE_ITEM",
            TYPE_STRING_DATA_ITEM to "TYPE_STRING_DATA_ITEM",
            TYPE_DEBUG_INFO_ITEM to "TYPE_DEBUG_INFO_ITEM",
            TYPE_ANNOTATION_ITEM to "TYPE_ANNOTATION_ITEM",
            TYPE_ENCODED_ARRAY_ITEM to "TYPE_ENCODED_ARRAY_ITEM",
            TYPE_ANNOTATIONS_DIRECTORY_ITEM to "TYPE_ANNOTATIONS_DIRECTORY_ITEM",
            TYPE_HIDDENAPI_CLASS_DATA_ITEM to "TYPE_HIDDENAPI_CLASS_DATA_ITEM"
        )

        fun getTypeName(type: Int): String = typeNameMap[type] ?: "Unknown type"
    }

    override fun toString(): String {
        return "MapItem(type=${getTypeName(type)}, unused=$unused, size=$size, offset=$offset)"
    }
}