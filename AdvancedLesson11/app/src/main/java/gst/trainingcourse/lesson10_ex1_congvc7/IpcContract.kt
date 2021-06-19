package gst.trainingcourse.lesson10_ex1_congvc7

object IpcContract {
    const val ZERO = 0
    const val ONE = 1
    const val TWO = 2
    const val FOUR = 4
    const val FIVE = 5

    val keys = intArrayOf(ZERO, ONE, TWO, FOUR, FIVE)

    // TODO change this to 5 seconds later
    const val DELAY_INTERVAL = 1_000L

    const val RECEIVE_MESSAGE_KEY_ANSWER = "ANSWER"
    const val RECEIVE_MESSAGE_CODE: Int = 0xb1
}
