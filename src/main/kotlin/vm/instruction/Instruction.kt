@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory

sealed class Instruction(val insnLength: Int) {
    abstract fun execute(pc: Int, memory: Memory, environment: Environment): Int

    companion object {
        fun fromCode(pc: Int, code: CodeItem): Instruction {
            val opCode = code.insns[pc].toInt()
            val opCodeLow = opCode and 0xff
            return when (opCodeLow) {
                0x00 -> Nop
                0x01 -> Move8(pc, code)
                0x02 -> Move816(pc, code)
                0x03 -> Move1616(pc, code)
                0x04 -> MoveWide(pc, code)
                0x05 -> MoveWide816(pc, code)
                0x06 -> MoveWide1616(pc, code)
                0x07 -> MoveObject(pc, code)
                0x08 -> MoveObject816(pc, code)
                0x09 -> MoveObject1616(pc, code)
                0x0a -> MoveResult(pc, code)
                0x0b -> MoveResultWide(pc, code)
                0x0c -> MoveResultObject(pc, code)
                0x0d -> MoveException(pc, code)
                0x0e -> ReturnVoid(pc, code)
                0x0f -> Return(pc, code)
                0x10 -> ReturnWide(pc, code)
                0x11 -> ReturnObject(pc, code)
                0x12 -> Const4(pc, code)
                0x13 -> Const16(pc, code)
                0x14 -> Const32(pc, code)
                0x15 -> ConstHigh16(pc, code)
                0x16 -> ConstWide16(pc, code)
                0x17 -> ConstWide32(pc, code)
                0x18 -> ConstWide64(pc, code)
                0x19 -> ConstWideHigh16(pc, code)
                0x1a -> ConstString(pc, code)
                0x1b -> ConstStringJumbo(pc, code)
                0x1c -> ConstClass(pc, code)
                0x1d -> MonitorEnter(pc, code)
                0x1e -> MonitorExit(pc, code)
                0x1f -> CheckCast(pc, code)
                0x20 -> InstanceOf(pc, code)
                0x21 -> ArrayLength(pc, code)
                0x22 -> NewInstance(pc, code)
                0x23 -> NewArray(pc, code)
                0x24 -> FilledNewArray(pc, code)
                0x25 -> FilledNewArrayRange(pc, code)
                0x26 -> FillArrayData(pc, code)
                0x27 -> Throw(pc, code)
                0x28 -> Goto(pc, code)
                0x29 -> Goto16(pc, code)
                0x2a -> Goto32(pc, code)
                0x2b -> PackedSwitch(pc, code)
                0x2c -> SparseSwitch(pc, code)
                0x2d -> CmplFloat(pc, code)
                0x2e -> CmpgFloat(pc, code)
                0x2f -> CmplDouble(pc, code)
                0x30 -> CmpgDouble(pc, code)
                0x31 -> CmpLong(pc, code)
                0x32 -> IfEq(pc, code)
                0x33 -> IfNe(pc, code)
                0x34 -> IfLt(pc, code)
                0x35 -> IfGe(pc, code)
                0x36 -> IfGt(pc, code)
                0x37 -> IfLe(pc, code)
                0x38 -> IfEqz(pc, code)
                0x39 -> IfNez(pc, code)
                0x3a -> IfLtz(pc, code)
                0x3b -> IfGez(pc, code)
                0x3c -> IfGtz(pc, code)
                0x3d -> IfLez(pc, code)
                0x44 -> Aget(pc, code)
                0x45 -> AgetWide(pc, code)
                0x46 -> AgetObject(pc, code)
                0x47 -> AgetBoolean(pc, code)
                0x48 -> AgetByte(pc, code)
                0x49 -> AgetChar(pc, code)
                0x4a -> AgetShort(pc, code)
                0x4b -> Aput(pc, code)
                0x4c -> AputWide(pc, code)
                0x4d -> AputObject(pc, code)
                0x4e -> AputBoolean(pc, code)
                0x4f -> AputByte(pc, code)
                0x50 -> AputChar(pc, code)
                0x51 -> AputShort(pc, code)
                0x52 -> Iget(pc, code)
                0x53 -> IgetWide(pc, code)
                0x54 -> IgetObject(pc, code)
                0x55 -> IgetBoolean(pc, code)
                0x56 -> IgetByte(pc, code)
                0x57 -> IgetChar(pc, code)
                0x58 -> IgetShort(pc, code)
                0x59 -> Iput(pc, code)
                0x5a -> IputWide(pc, code)
                0x5b -> IputObject(pc, code)
                0x5c -> IputBoolean(pc, code)
                0x5d -> IputByte(pc, code)
                0x5e -> IputChar(pc, code)
                0x5f -> IputShort(pc, code)
                0x60 -> Sget(pc, code)
                0x61 -> SgetWide(pc, code)
                0x62 -> SgetObject(pc, code)
                0x63 -> SgetBoolean(pc, code)
                0x64 -> SgetByte(pc, code)
                0x65 -> SgetChar(pc, code)
                0x66 -> SgetShort(pc, code)
                0x67 -> Sput(pc, code)
                0x68 -> SputWide(pc, code)
                0x69 -> SputObject(pc, code)
                0x6a -> SputBoolean(pc, code)
                0x6b -> SputByte(pc, code)
                0x6c -> SputChar(pc, code)
                0x6d -> SputShort(pc, code)
                0x6e -> InvokeVirtual(pc, code)
                0x6f -> InvokeSuper(pc, code)
                0x70 -> InvokeDirect(pc, code)
                0x71 -> InvokeStatic(pc, code)
                0x72 -> InvokeInterface(pc, code)
                0x74 -> InvokeVirtualRange(pc, code)
                0x75 -> InvokeSuperRange(pc, code)
                0x76 -> InvokeDirectRange(pc, code)
                0x77 -> InvokeStaticRange(pc, code)
                0x78 -> InvokeInterfaceRange(pc, code)
                0x7b -> NegInt(pc, code)
                0x7c -> NotInt(pc, code)
                0x7d -> NegLong(pc, code)
                0x7e -> NotLong(pc, code)
                0x7f -> NegFloat(pc, code)
                0x80 -> NegDouble(pc, code)
                0x81 -> IntToLong(pc, code)
                0x82 -> IntToFloat(pc, code)
                0x83 -> IntToDouble(pc, code)
                0x84 -> LongToInt(pc, code)
                0x85 -> LongToFloat(pc, code)
                0x86 -> LongToDouble(pc, code)
                0x87 -> FloatToInt(pc, code)
                0x88 -> FloatToLong(pc, code)
                0x89 -> FloatToDouble(pc, code)
                0x8a -> DoubleToInt(pc, code)
                0x8b -> DoubleToLong(pc, code)
                0x8c -> DoubleToFloat(pc, code)
                0x8d -> IntToByte(pc, code)
                0x8e -> IntToChar(pc, code)
                0x8f -> IntToShort(pc, code)
                0x90 -> AddInt(pc, code)
                0x91 -> SubInt(pc, code)
                0x92 -> MulInt(pc, code)
                0x93 -> DivInt(pc, code)
                0x94 -> RemInt(pc, code)
                0x95 -> AndInt(pc, code)
                0x96 -> OrInt(pc, code)
                0x97 -> XorInt(pc, code)
                0x98 -> ShlInt(pc, code)
                0x99 -> ShrInt(pc, code)
                0x9a -> UshrInt(pc, code)
                0x9b -> AddLong(pc, code)
                0x9c -> SubLong(pc, code)
                0x9d -> MulLong(pc, code)
                0x9e -> DivLong(pc, code)
                0x9f -> RemLong(pc, code)
                0xa0 -> AndLong(pc, code)
                0xa1 -> OrLong(pc, code)
                0xa2 -> XorLong(pc, code)
                0xa3 -> ShlLong(pc, code)
                0xa4 -> ShrLong(pc, code)
                0xa5 -> UshrLong(pc, code)
                0xa6 -> AddFloat(pc, code)
                0xa7 -> SubFloat(pc, code)
                0xa8 -> MulFloat(pc, code)
                0xa9 -> DivFloat(pc, code)
                0xaa -> RemFloat(pc, code)
                0xab -> AddDouble(pc, code)
                0xac -> SubDouble(pc, code)
                0xad -> MulDouble(pc, code)
                0xae -> DivDouble(pc, code)
                0xaf -> RemDouble(pc, code)
                0xb0 -> AddInt2Addr(pc, code)
                0xb1 -> SubInt2Addr(pc, code)
                0xb2 -> MulInt2Addr(pc, code)
                0xb3 -> DivInt2Addr(pc, code)
                0xb4 -> RemInt2Addr(pc, code)
                0xb5 -> AndInt2Addr(pc, code)
                0xb6 -> OrInt2Addr(pc, code)
                0xb7 -> XorInt2Addr(pc, code)
                0xb8 -> ShlInt2Addr(pc, code)
                0xb9 -> ShrInt2Addr(pc, code)
                0xba -> UshrInt2Addr(pc, code)
                0xbb -> AddLong2Addr(pc, code)
                0xbc -> SubLong2Addr(pc, code)
                0xbd -> MulLong2Addr(pc, code)
                0xbe -> DivLong2Addr(pc, code)
                0xbf -> RemLong2Addr(pc, code)
                0xc0 -> AndLong2Addr(pc, code)
                0xc1 -> OrLong2Addr(pc, code)
                0xc2 -> XorLong2Addr(pc, code)
                0xc3 -> ShlLong2Addr(pc, code)
                0xc4 -> ShrLong2Addr(pc, code)
                0xc5 -> UshrLong2Addr(pc, code)
                0xc6 -> AddFloat2Addr(pc, code)
                0xc7 -> SubFloat2Addr(pc, code)
                0xc8 -> MulFloat2Addr(pc, code)
                0xc9 -> DivFloat2Addr(pc, code)
                0xca -> RemFloat2Addr(pc, code)
                0xcb -> AddDouble2Addr(pc, code)
                0xcc -> SubDouble2Addr(pc, code)
                0xcd -> MulDouble2Addr(pc, code)
                0xce -> DivDouble2Addr(pc, code)
                0xcf -> RemDouble2Addr(pc, code)
                0xd0 -> AddIntLit16(pc, code)
                0xd1 -> RsubInt(pc, code)
                0xd2 -> MulIntLit16(pc, code)
                0xd3 -> DivIntLit16(pc, code)
                0xd4 -> RemIntLit16(pc, code)
                0xd5 -> AndIntLit16(pc, code)
                0xd6 -> OrIntLit16(pc, code)
                0xd7 -> XorIntLit16(pc, code)
                0xd8 -> AddIntLit8(pc, code)
                0xd9 -> RsubIntLit8(pc, code)
                0xda -> MulIntLit8(pc, code)
                0xdb -> DivIntLit8(pc, code)
                0xdc -> RemIntLit8(pc, code)
                0xdd -> AndIntLit8(pc, code)
                0xde -> OrIntLit8(pc, code)
                0xdf -> XorIntLit8(pc, code)
                0xe0 -> ShlIntLit8(pc, code)
                0xe1 -> ShrIntLit8(pc, code)
                0xe2 -> UshrIntLit8(pc, code)
                0xfa -> InvokePolymorphic(pc, code)
                0xfb -> InvokePolymorphicRange(pc, code)
                0xfc -> InvokeCustom(pc, code)
                0xfd -> InvokeCustomRange(pc, code)
                0xfe -> ConstMethodHandle(pc, code)
                0xff -> ConstMethodType(pc, code)
                else -> error("Unknown opcode: $opCode")
            }
        }
    }

    data object _00x : Instruction(0) { // N/A
        override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
            TODO("Not yet implemented")
        }
    }

    // TODO: implement constructors for each instruction
    abstract class _10x(val op: Int) : Instruction(1) // 00|op
    {
        constructor(pc: Int, code: CodeItem) : this(code.insns[pc].toInt() and 0xff)
    }

    abstract class _12x(val op: Int, val vA: Int, val vB: Int) : Instruction(1) // B|A|op
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0x0f,
            (code.insns[pc].toInt() shr 12) and 0x0f
        )
    }

    abstract class _11n(val op: Int, val vA: Int, val LB: Int) : Instruction(1) // B|A|op, B is a literal
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0x0f,
            (code.insns[pc].toInt() shr 12) and 0x0f
        )
    }

    abstract class _11x(val op: Int, val vAA: Int) : Instruction(1) // AA|op
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff
        )
    }

    abstract class _10t(val op: Int, val offset: Int) : Instruction(1) // AA|op, offset
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            ((code.insns[pc].toInt() shr 8) and 0xff).toByte().toInt()
        )
    }

    abstract class _20t(val op: Int, val offset: Int) : Instruction(2) // 00|op AAAA
    {
        constructor(pc: Int, code: CodeItem) : this(code.insns[pc].toInt() and 0xff, code.insns[pc + 1].toInt())
    }

    abstract class _20bc(val op: Int, val AA: Int, val KindBBBB: Int) : Instruction(2) // AA|op BBBB
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _22x(val op: Int, val vAA: Int, val vBBBB: Int) : Instruction(2) // AA|op BBBB
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[1].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _21t(val op: Int, val vAA: Int, val offset: Int) : Instruction(2) // AA|op, offset
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _21s(val op: Int, val vAA: Int, val LBBBB: Int) :
        Instruction(2) // AA|op BBBB, B is a literal
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _21h(val op: Int, val vAA: Int, val LBBBB: Int) : Instruction(2) // AA|op BBBB
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _21c(val op: Int, val vAA: Int, val KindBBBB: Int) : Instruction(2) // AA|op BBBB
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _23x(val op: Int, val vAA: Int, val vBB: Int, val vCC: Int) : Instruction(2) // AA|op CC|BB
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt() and 0xff,
            (code.insns[pc + 1].toInt() shr 8) and 0xff
        )
    }

    abstract class _22b(val op: Int, val vAA: Int, val vBB: Int, val LCC: Int) :
        Instruction(2) // AA|op CC|BB, C is a literal
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt() and 0xff,
            (code.insns[pc + 1].toInt() shr 8) and 0xff
        )
    }

    abstract class _22t(val op: Int, val vA: Int, val vB: Int, val offset: Int) : Instruction(2) // B|A|op CCCC
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xf,
            (code.insns[pc].toInt() shr 12) and 0xf,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _22s(val op: Int, val vA: Int, val vB: Int, val LCCCC: Int) :
        Instruction(2) // B|A|op CCCC, C is a literal
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xf,
            (code.insns[pc].toInt() shr 12) and 0xf,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _22c(val op: Int, val vA: Int, val vB: Int, val KindCCCC: Int) : Instruction(2) // B|A|op CCCC
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xf,
            (code.insns[pc].toInt() shr 12) and 0xf,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _22cs(val op: Int, val vA: Int, val vB: Int, val fieldoffCCCC: Int) :
        Instruction(2) // B|A|op CCCC, C is a literal
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xf,
            (code.insns[pc].toInt() shr 12) and 0xf,
            code.insns[pc + 1].toInt()
        )
    }

    abstract class _30t(val op: Int, val AAAAlo: Int, val AAAAhi: Int) : Instruction(3) // 00|op AAAAlo AAAAhi, goto/32
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _32x(val op: Int, val vAAAA: Int, val vBBBB: Int) : Instruction(3) //00|op AAAA BBBB
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _31i(val op: Int, val vAA: Int, val BBBBlo: Int, val BBBBhi: Int) :
        Instruction(3) // AA|op BBBBlo BBBBhi, B is a literal
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _31t(val op: Int, val vAA: Int, val BBBBlo: Int, val BBBBhi: Int) :
        Instruction(3) // AA|op BBBBlo BBBBhi, offset
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _31c(val op: Int, val vAA: Int, val KindBBBBlo: Int, val KindBBBBhi: Int) :
        Instruction(3) // AA|op BBBBlo BBBBhi, const-string/jumbo
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _35c(
        val op: Int,
        val A: Int,
        val G: Int,
        val BBBB: Int,
        val F: Int,
        val E: Int,
        val D: Int,
        val C: Int
    ) :
        Instruction(3) // A|G|op BBBB F|E|D|C
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 12) and 0xf,
            (code.insns[pc].toInt() shr 8) and 0xf,
            code.insns[pc + 1].toInt(),
            (code.insns[pc + 2].toInt() shr 12) and 0xf,
            (code.insns[pc + 2].toInt() shr 8) and 0xf,
            (code.insns[pc + 2].toInt() shr 4) and 0xf,
            code.insns[pc + 2].toInt() and 0xf
        )
    }

    abstract class _35ms(
        val op: Int,
        val A: Int,
        val G: Int,
        val BBBB: Int,
        val F: Int,
        val E: Int,
        val D: Int,
        val C: Int
    ) :
        Instruction(3) // A|G|op BBBB F|E|D|C
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 12) and 0xf,
            (code.insns[pc].toInt() shr 8) and 0xf,
            code.insns[pc + 1].toInt(),
            (code.insns[pc + 2].toInt() shr 12) and 0xf,
            (code.insns[pc + 2].toInt() shr 8) and 0xf,
            (code.insns[pc + 2].toInt() shr 4) and 0xf,
            code.insns[pc + 2].toInt() and 0xf
        )
    }

    abstract class _35mi(
        val op: Int,
        val A: Int,
        val G: Int,
        val BBBB: Int,
        val F: Int,
        val E: Int,
        val D: Int,
        val C: Int
    ) :
        Instruction(3) // A|G|op BBBB F|E|D|C
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 12) and 0xf,
            (code.insns[pc].toInt() shr 8) and 0xf,
            code.insns[pc + 1].toInt(),
            (code.insns[pc + 2].toInt() shr 12) and 0xf,
            (code.insns[pc + 2].toInt() shr 8) and 0xf,
            (code.insns[pc + 2].toInt() shr 4) and 0xf,
            code.insns[pc + 2].toInt() and 0xf
        )
    }

    abstract class _3rc(
        val op: Int,
        val count: Int, // 0..255
        val BBBB: Int,
        val CCCC: Int
    ) :
        Instruction(3) // AA|op BBBB CCCC
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _3rms(
        val op: Int,
        val count: Int, // 0..255
        val BBBB: Int,
        val CCCC: Int
    ) :
        Instruction(3) // AA|op BBBB CCCC
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _3rmi(
        val op: Int,
        val count: Int, // 0..255
        val BBBB: Int,
        val CCCC: Int
    ) :
        Instruction(3) // AA|op BBBB CCCC
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt()
        )
    }


    abstract class _45cc(
        val op: Int,
        val A: Int,
        val G: Int,
        val BBBB: Int,
        val F: Int,
        val E: Int,
        val D: Int,
        val C: Int,
        val HHHH: Int,
    ) :
        Instruction(4) // A|G|op BBBB F|E|D|C HHHH
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 12) and 0xf,
            (code.insns[pc].toInt() shr 8) and 0xf,
            code.insns[pc + 1].toInt(),
            (code.insns[pc + 2].toInt() shr 12) and 0xf,
            (code.insns[pc + 2].toInt() shr 8) and 0xf,
            (code.insns[pc + 2].toInt() shr 4) and 0xf,
            code.insns[pc + 2].toInt() and 0xf,
            code.insns[pc + 2].toInt()
        )
    }

    abstract class _4rcc(
        val op: Int,
        val count: Int, // 0..65535
        val BBBB: Int,
        val CCCC: Int,
        val HHHH: Int,
    ) :
        Instruction(4) // AA|op BBBB CCCC HHHH
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt(),
            code.insns[pc + 3].toInt()
        )
    }

    abstract class _51l(
        val op: Int,
        val vAA: Int,
        val BBBBlo: Int,
        val BBBBhiii: Int,
        val BBBBhii: Int,
        val BBBBhi: Int
    ) :
        Instruction(5) // AA|op BBBBlo BBBB BBBB BBBBhi // const-wide
    {
        constructor(pc: Int, code: CodeItem) : this(
            code.insns[pc].toInt() and 0xff,
            (code.insns[pc].toInt() shr 8) and 0xff,
            code.insns[pc + 1].toInt(),
            code.insns[pc + 2].toInt(),
            code.insns[pc + 3].toInt(),
            code.insns[pc + 4].toInt()
        )
    }
}