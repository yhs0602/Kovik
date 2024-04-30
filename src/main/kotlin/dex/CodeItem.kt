package com.yhs0602.dex

data class CodeItem(
    val registersSize: UShort, // ushort: 이 코드에서 사용하는 레지스터의 수
    val insSize: UShort, // ushort: 이 코드에 관한 메서드로 들어오는 인수의 단어 수
    val outsSize: UShort, // ushort: 메서드 호출을 위해 이 코드에서 필요로 하는 나가는 인수 공간의 단어 수
    val triesSize: UShort, // ushort: 이 인스턴스에 관한 try_item의 수 0이 아닌 경우 이 인스턴스에 있는 insns의 바로 다음에 tries 배열로 표시됩니다.
    val debugInfoOff: UInt, // uint: 파일 시작 부분에서 이 코드의 디버그 정보(줄 수, 로컬 변수 정보) 시퀀스까지의 오프셋이며 정보가 없는 경우 0입니다. 오프셋은 0이 아닌 경우 data 섹션 내 위치까지여야 합니다. 데이터 형식은 아래 'debug_info_item'에 의해 지정됩니다.
    val insnsSize: UInt, // uint: 이 코드의 명령어 수 (16비트 코드 단위)
    val insns: UShortArray,
    val padding: List<UShort>, // ushort: insns의 크기가 홀수인 경우 0으로 채워집니다.
    val tries: List<TryItem>, // try_item: 이 코드에 대한 예외 처리 핸들러 목록입니다. 이 목록은 코드 단위로 정렬되어 있으며 각 항목은 try_item 구조체로 구성됩니다.
    val handlers: List<EncodedCatchHandler> // encoded_catch_handler_list: 이 코드에 대한 예외 처리 핸들러 목록입니다. 이 목록은 코드 단위로 정렬되어 있으며 각 항목은 encoded_catch_handler_list 구조체로 구성됩니다.
) {
    override fun toString(): String {
        return "CodeItem(registersSize=$registersSize, insSize=$insSize, outsSize=$outsSize, triesSize=$triesSize, debugInfoOff=$debugInfoOff, insnsSize=$insnsSize, padding=$padding, tries=$tries, handlers=$handlers)"
    }
}