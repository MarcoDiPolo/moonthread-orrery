package studio.moonthread.orrery

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import studio.moonthread.orrery.content.Alignment
import studio.moonthread.orrery.content.Ring
import studio.moonthread.orrery.content.seals

class MoonPuzzleTest {
    @Test fun eachButtonPullsOnlyItsConnectedRings() {
        val north = Alignment(0, 0, 0)
        assertEquals(Alignment(1, 1, 0), north.turn(Ring.ASH))
        assertEquals(Alignment(0, 1, 1), north.turn(Ring.TIDE))
        assertEquals(Alignment(0, 0, 1), north.turn(Ring.EMBER))
        assertEquals(Alignment(0, 0, 0), Alignment(3, 3, 0).turn(Ring.ASH))
    }

    @Test fun everyStartingPositionCanReachEachSealUsingTheHint() {
        for (seal in seals) for (a in 0..3) for (t in 0..3) for (e in 0..3) {
            var actual = Alignment(a, t, e)
            var steps = 0
            while (actual != seal.target && steps < 10) {
                actual = actual.turn(requireNotNull(actual.hint(seal.target)))
                steps++
            }
            assertEquals(seal.target, actual)
            assertTrue(steps <= 9)
        }
    }
}
