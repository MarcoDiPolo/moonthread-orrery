package studio.moonthread.orrery.content

enum class Ring(val title: String) { ASH("Ash"), TIDE("Tide"), EMBER("Ember") }

data class Alignment(val ash: Int, val tide: Int, val ember: Int) {
    init { require(listOf(ash, tide, ember).all { it in 0..3 }) }

    fun position(ring: Ring) = when (ring) { Ring.ASH -> ash; Ring.TIDE -> tide; Ring.EMBER -> ember }

    fun turn(ring: Ring): Alignment = when (ring) {
        Ring.ASH -> copy(ash = (ash + 1) % 4, tide = (tide + 1) % 4)
        Ring.TIDE -> copy(tide = (tide + 1) % 4, ember = (ember + 1) % 4)
        Ring.EMBER -> copy(ember = (ember + 1) % 4)
    }

    fun hint(target: Alignment) = Ring.entries.firstOrNull { position(it) != target.position(it) }
}

data class Seal(val title: String, val starting: Alignment, val target: Alignment)

val directions = listOf("North", "East", "South", "West")
val seals = listOf(
    Seal("The moth at dusk", Alignment(0, 0, 0), Alignment(1, 2, 1)),
    Seal("The well of stars", Alignment(3, 1, 2), Alignment(1, 0, 3)),
    Seal("The moon's return", Alignment(0, 3, 1), Alignment(3, 0, 2)),
)
