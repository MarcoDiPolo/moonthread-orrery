package studio.moonthread.orrery.content

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.math.Vector3
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.foundation.content.SpatialView

private val night = Color(0xFF211D32)
private val parchment = Color(0xFFFFF0D3)
private val accents = listOf(Color(0xFFDACAE7), Color(0xFF79DBCD), Color(0xFFF0A16B))

@Composable
fun HomeVolume() {
    val context = LocalContext.current
    val saved = remember { context.getSharedPreferences("moonthread", Context.MODE_PRIVATE) }
    var chapter by remember { mutableIntStateOf(saved.getInt("chapter", 0).coerceIn(0, seals.lastIndex)) }
    val seal = seals[chapter]
    var current by remember { mutableStateOf(Alignment(
        saved.getInt("ash", seal.starting.ash).coerceIn(0, 3),
        saved.getInt("tide", seal.starting.tide).coerceIn(0, 3),
        saved.getInt("ember", seal.starting.ember).coerceIn(0, 3))) }
    var turns by remember { mutableIntStateOf(saved.getInt("turns", 0).coerceAtLeast(0)) }
    var hint by remember { mutableStateOf("") }
    var scene by remember { mutableStateOf<OrreryScene?>(null) }
    val solved = current == seal.target
    LaunchedEffect(chapter, current, turns) {
        saved.edit().putInt("chapter", chapter).putInt("ash", current.ash).putInt("tide", current.tide)
            .putInt("ember", current.ember).putInt("turns", turns).apply()
    }
    DisposableEffect(Unit) { onDispose { scene?.close() } }

    SpatialView(
        initial = { content, attachments ->
            val orrery = OrreryScene()
            orrery.display(current)
            content.addEntity(orrery.root)
            scene = orrery
            for ((id, position) in listOf("clue" to Vector3(0f, .265f, 0f), "keys" to Vector3(0f, -.305f, .025f))) {
                attachments.entity(id)?.let { panel ->
                    panel.components[TransformComponent::class.java]?.setPosition(position)
                    content.addEntity(panel)
                }
            }
        },
        update = { _, _ -> scene?.display(current) },
        attachments = {
            AttachmentPanel(id = "clue") {
                Column(Modifier.width(740.dp).clip(RoundedCornerShape(28.dp)).background(night).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Moonthread Orrery", color = parchment, fontSize = 33.sp, fontWeight = FontWeight.Bold)
                    Text("Seal ${chapter + 1} of ${seals.size}  ·  ${seal.title}", color = accents[0], fontSize = 23.sp)
                    Text("Align the pointers: " + Ring.entries.joinToString("   ") { it.title + " " + directions[seal.target.position(it)] }, color = parchment, fontSize = 21.sp)
                }
            }
            AttachmentPanel(id = "keys") {
                Column(Modifier.width(790.dp).clip(RoundedCornerShape(28.dp)).background(night).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(if (solved) "Seal lit in $turns turns" else "Turn a ring clockwise. North is the gold star.", color = parchment, fontSize = 21.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Ring.entries.forEachIndexed { index, ring ->
                            Key("${ring.title} · ${directions[current.position(ring)]}", accents[index], enabled = !solved) {
                                current = current.turn(ring); turns++; hint = ""
                            }
                        }
                    }
                    Text(hint.ifEmpty { "Ash pulls Tide. Tide pulls Ember. Ember turns alone." }, color = accents[0], fontSize = 19.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (solved) Key(if (chapter == seals.lastIndex) "Play again" else "Next seal", parchment) {
                            chapter = (chapter + 1) % seals.size; current = seals[chapter].starting; turns = 0; hint = ""
                        } else Key("Hint", parchment) { hint = "Turn ${current.hint(seal.target)?.title} next." }
                        Key("Reset seal", parchment) { current = seal.starting; turns = 0; hint = "" }
                    }
                }
            }
        }
    )
}

@Composable
private fun Key(label: String, ink: Color, enabled: Boolean = true, click: () -> Unit) {
    Box(Modifier.clip(RoundedCornerShape(18.dp)).background(Color(0xFF3B344B)).clickable(enabled = enabled, onClick = click).padding(horizontal = 19.dp, vertical = 12.dp), contentAlignment = Alignment.Center) {
        Text(label, color = ink, fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}
