import calculator.VisitorBasedCalculator
import com.github.pambrose.common.script.KotlinExprEvaluator
import kotlin.math.pow
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

val Char.isNonNum: Boolean
    get() = this in listOf('+', '-', '*', '/', '=')

val Char.isAnOpOr0: Boolean
    get() = isNonNum || this == '0'

val nonZeroNums = ('1'..'9').toList()
val numsOnly = ('0'..'9').toList()
val opsOnly = listOf('+', '-', '*', '/')
val numOps = numsOnly + opsOnly
val numsWithEquals = numsOnly + listOf('=')
val numOpsWithEquals = numOps + listOf('=')

fun main(args: Array<String>) {
    val evaluator = VisitorBasedCalculator()

    listOf(fourDigitExpr(), fiveDigitExpr(), sixDigitExpr())
        .toSequence()
        .map {
            evaluator.eval(it).let { result ->
                result to "$it=${result.toInt()}"
            }
        }
        .filter { it.first.isWhole() && it.second.length == 8 }
        .onEach { println(it.second) }
        .count()
        .also { println(it) }
}

fun <A> Collection<Sequence<A>>.toSequence(): Sequence<A> =
    sequence {
        this@toSequence.forEach { seq ->
            seq.forEach { yield(it) }
        }
    }

fun Double.isWhole(): Boolean = this % 1.0 == 0.0

@OptIn(ExperimentalTime::class)
fun timeSaverIndicator() {
    //Initial count = 2,562,890,625
    //Count with positional optimization = 926,100,000
    //Count with looking left optimization = 359,752,000

    val four = fourDigitExprCount()
    val fp4 = 14.0.pow(4)
    val fourp = String.format("%.3f", 100 * four / fp4)
    val fourpc = String.format("%.3f", 100 - 100 * four / fp4)
    val five = fiveDigitExprCount()
    val fp5 = 14.0.pow(5)
    val fivep = String.format("%.3f", 100 * five / fp5)
    val fivepc = String.format("%.3f", 100 - 100 * five / fp5)
    val six = sixDigitExprCount()
    val fp6 = 14.0.pow(6)
    val sixp = String.format("%.3f", 100 * six / fp6)
    val sixpc = String.format("%.3f", 100 - 100 * six / fp6)

    val time = measureTime {
        val evaluator = KotlinExprEvaluator()
        var count = 0

        val k1 = sequenceOf(1, 2, 3, 4, 5)

        println(k1.toList())
        println(k1.toList())
        println(k1.take(3).toList())

//        eightDigitExpr()
//            .mapIndexed { i, s ->
//                if (i % 100 == 0)
//                    println(i)
//                s
//            }
//            .take(1000)
//            .filter {
//                try {
//                    evaluator.eval(it)
//                } catch (e: java.lang.Exception) {
//                    false
//                }
//            }
//            .count().also { println("The value is $it") }
//
//        val fp8 = 15.0.pow(8)
//        val eight = eightDigitExpr()
//        val eightp = String.format("%.3f", 100 * (eight / fp8))
//        val eightpc = String.format("%.3f", 100 - 100 * (eight / fp8))


        println("$four out of $fp4 is $fourp%, so we saved $fourpc%")
        println("$five out of $fp5 is $fivep%, so we saved $fivepc%")
        println("$six out of $fp6 is $sixp%, so we saved $sixpc%")
//        println("$eight out of $fp8 is $eightp%, so we saved $eightpc%")
    }
    println(time)
}

fun String.containsOp(ops: List<Char>): Boolean = ops.any { it in this }

fun fourDigitExpr(): Sequence<String> =
    sequence {
        for (p0 in nonZeroNums) {
            for (p1 in numOps) {
                for (p2 in numOps) {
                    if (p1.isNonNum && p2.isAnOpOr0)
                        continue
                    for (p3 in numsOnly) {
                        if (p2.isNonNum && p3 == '0')
                            continue
                        val text = "$p0$p1$p2$p3"
                        if (text.containsOp(opsOnly))
                            yield(text)
                    }
                }
            }
        }
    }

fun fourDigitExprCount(): Int = fourDigitExpr().count()

fun fiveDigitExpr(): Sequence<String> =
    sequence {
        for (p0 in nonZeroNums) {
            for (p1 in numOps) {
                for (p2 in numOps) {
                    if (p1.isNonNum && p2.isAnOpOr0)
                        continue
                    for (p3 in numOps) {
                        if (p2.isNonNum && p3.isAnOpOr0)
                            continue
                        for (p4 in numsOnly) {
                            if (p3.isNonNum && p4 == '0')
                                continue
                            val text = "$p0$p1$p2$p3$p4"
                            if (text.containsOp(opsOnly))
                                yield(text)
                        }
                    }
                }
            }
        }
    }


fun fiveDigitExprCount(): Int = fiveDigitExpr().count()

fun sixDigitExpr(): Sequence<String> =
    sequence {
        for (p0 in nonZeroNums) {
            for (p1 in numOps) {
                for (p2 in numOps) {
                    if (p1.isNonNum && p2.isAnOpOr0)
                        continue
                    for (p3 in numOps) {
                        if (p2.isNonNum && p3.isAnOpOr0)
                            continue
                        for (p4 in numOps) {
                            if (p3.isNonNum && p4.isAnOpOr0)
                                continue
                            for (p5 in numsOnly) {
                                if (p4.isNonNum && p5 == '0')
                                    continue
                                val text = "$p0$p1$p2$p3$p4$p5"
                                if (text.containsOp(opsOnly))
                                    yield(text)
                            }
                        }
                    }
                }
            }
        }

    }

fun sixDigitExprCount(): Int = sixDigitExpr().count()

fun eightDigitExpr(): Sequence<String> =
    sequence {
        for (p0 in nonZeroNums) {
            for (p1 in numOps) {
                for (p2 in numOps) {
                    if (p1.isNonNum && p2.isAnOpOr0)
                        continue
                    for (p3 in numOps) {
                        if (p2.isNonNum && p3.isAnOpOr0)
                            continue
                        for (p4 in numOpsWithEquals) {
                            if (p3.isNonNum && p4.isAnOpOr0)
                                continue
                            for (p5 in numsWithEquals) {
                                if (p4.isNonNum && p5.isNonNum)
                                    continue
                                for (p6 in numsWithEquals) {
                                    if (p5.isNonNum && p6.isNonNum)
                                        continue
                                    if (p5.isNonNum && p6 == '0')
                                        continue
                                    for (p7 in numsOnly) {
                                        if (p4 != '=' && p5 != '=' && p6 != '=')
                                            continue
                                        val textEquals = "$p0$p1$p2$p3$p4$p5$p6$p7".replace("=", "==")
                                        yield(textEquals)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }