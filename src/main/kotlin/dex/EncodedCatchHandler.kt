package com.yhs0602.dex

data class EncodedCatchHandler(
    val size: Int, // sleb128. 이 목록의 catch 형식 수입니다.
    // 양수가 아닌 경우 catch 형식 수의 음수이며 catch 뒤에 catch-all 핸들러가 옵니다.
    // 예를 들어 size가 0인 경우 catch-all은 있지만 형식이 명시된 catch는 없음을 의미합니다.
    // size가 2인 경우 형식이 명시된 catch가 2개 있지만 catch-all은 없음을 의미합니다.
    // size가 -1인 경우 형식이 있는 catch 1개와 catch-all 1개가 있음을 의미합니다.
    val handlers: List<EncodedTypeAddrPair>, // catch된 형식별로 1개씩 있는 abs(size) 인코딩된 항목의 스트림이며, 형식을 테스트해야 하는 순서를 따릅니다.
    val catchAllAddr: Int? // uleb128, optional. catch-all 핸들러의 바이트 코드 주소입니다. 이 요소는 size가 양수가 아닌 경우에만 존재합니다.
)