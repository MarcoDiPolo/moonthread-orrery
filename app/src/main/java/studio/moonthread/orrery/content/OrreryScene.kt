package studio.moonthread.orrery.content

import com.pico.spatial.core.ecs.Entity
import com.pico.spatial.core.ecs.ModelEntity
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.ecs.resource.MeshResource
import com.pico.spatial.core.ecs.resource.PhysicallyBasedMaterial
import com.pico.spatial.core.math.Color4
import com.pico.spatial.core.math.EulerAngles
import com.pico.spatial.core.math.Vector3

/** Everything visible in the instrument is a native PICO mesh authored in metres. */
class OrreryScene {
    val root = Entity()
    private val meshes = mutableListOf<MeshResource>()
    private val paints = mutableListOf<PhysicallyBasedMaterial>()
    private val rotors = mutableListOf<Entity>()
    private var last: Alignment? = null

    private fun surface(r: Float, g: Float, b: Float, glow: Float = 0.14f) = PhysicallyBasedMaterial.create().apply {
        setBaseColor(Color4(r, g, b, 1f))
        setEmissiveColor(Color4(r * glow, g * glow, b * glow, 1f))
        setRoughness(0.31f)
        paints.add(this)
    }

    private fun part(parent: Entity, shape: MeshResource, paint: PhysicallyBasedMaterial, x: Float = 0f, y: Float = 0f, z: Float = 0f, tilt: Float = 0f): Entity {
        meshes.add(shape)
        return ModelEntity(shape, paint).also { item ->
            item.components[TransformComponent::class.java]?.apply {
                setPosition(Vector3(x, y, z)); setEulerAngles(EulerAngles(tilt, 0f, 0f))
            }
            parent.addChild(item)
        }
    }

    init {
        val stone = surface(.10f, .065f, .17f)
        val brass = surface(.70f, .45f, .20f)
        val ash = surface(.86f, .79f, .92f, .36f)
        val tide = surface(.15f, .77f, .73f, .38f)
        val ember = surface(1f, .43f, .15f, .42f)
        val moon = surface(1f, .86f, .59f, .70f)
        part(root, MeshResource.createCylinder(.028f, .132f), stone, y = -.229f, z = -.035f)
        part(root, MeshResource.createTorus(.134f, .128f), brass, y = -.215f, z = -.035f)
        part(root, MeshResource.createCylinder(.175f, .012f), brass, y = -.133f, z = -.059f)
        part(root, MeshResource.createSphere(.036f), moon, z = .003f)
        part(root, MeshResource.createSphere(.032f), stone, x = .014f, y = .009f, z = .013f)
        listOf(ash, tide, ember).forEachIndexed { index, paint ->
            val r = listOf(.182f, .136f, .085f)[index]
            val rotor = Entity()
            root.addChild(rotor)
            rotor.components[TransformComponent::class.java]?.setPosition(Vector3(0f, 0f, -.045f + index * .045f))
            part(rotor, MeshResource.createTorus(r, r - .005f), paint, tilt = 90f)
            part(rotor, MeshResource.createSphere(.012f), paint, y = r, z = .005f)
            part(rotor, MeshResource.createCone(.023f, .011f), paint, y = r + .014f, z = .005f)
            rotors.add(rotor)
        }
        // Fixed compass points remain behind the moving rings.
        part(root, MeshResource.createSphere(.009f), moon, y = .212f, z = -.053f)
        for ((x,y) in listOf(.212f to 0f, 0f to -.212f, -.212f to 0f)) {
            part(root, MeshResource.createSphere(.0055f), brass, x = x, y = y, z = -.053f)
        }
    }

    fun display(alignment: Alignment) {
        if (alignment == last) return
        Ring.entries.forEachIndexed { index, ring ->
            rotors[index].components[TransformComponent::class.java]?.setEulerAngles(EulerAngles(0f, 0f, -90f * alignment.position(ring)))
        }
        last = alignment
    }

    fun close() {
        if (root.valid) { root.removeFromParent(); root.destroy() }
        meshes.filter { it.valid }.forEach { it.close() }
        paints.filter { it.valid }.forEach { it.close() }
    }
}
