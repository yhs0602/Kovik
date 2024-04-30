package com.yhs0602.dex

data class TryItem(
    val startAddr: UInt, // 이 항목에서 처리하는 코드 블록의 시작 주소입니다. 이 주소는 첫 번째로 처리되는 명령의 시작 부분에 관한 16비트 코드 단위의 수입니다.
    val insnCount: UShort, // 이 항목에서 처리하는 16비트 코드 단위의 수입니다. 처리되는 마지막 코드 단위(포함)는 start_addr + insn_count - 1입니다.
    val handlerOff: UShort // 관련 encoded_catch_hander_list의 시작 부분에서 이 항목의 encoded_catch_handler에 이르는 바이트 단위의 오프셋입니다. 이 오프셋은 encoded_catch_handler 시작 부분까지의 오프셋이어야 합니다.
)