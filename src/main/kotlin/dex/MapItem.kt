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
    }
}