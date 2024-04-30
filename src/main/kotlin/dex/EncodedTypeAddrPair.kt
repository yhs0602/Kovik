package com.yhs0602.dex

data class EncodedTypeAddrPair(
    val typeIdx: Int, // uleb128. catch할 예외 형식의 type_ids 목록 색인
    val addr: Int // uleb128. 해당 예외 형식을 처리하는 핸들러의 바이트 코드 주소
)