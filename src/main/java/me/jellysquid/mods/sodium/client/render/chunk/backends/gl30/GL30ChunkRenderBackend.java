package me.jellysquid.mods.sodium.client.render.chunk.backends.gl30;

import me.jellysquid.mods.sodium.client.gl.SodiumVertexFormats;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.gl.func.GlFunctions;
import me.jellysquid.mods.sodium.client.render.chunk.oneshot.ChunkRenderBackendOneshot;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Shader-based render backend for chunks which uses VAOs to avoid the overhead in setting up vertex attribute pointers
 * before every draw call. This approach has significantly less CPU overhead as we only need to cross the native code
 * barrier once in order to setup all the necessary vertex attribute states and buffer bindings. Additionally, it might
 * allow the driver to skip validation logic that would otherwise be performed.
 */
public class GL30ChunkRenderBackend extends ChunkRenderBackendOneshot<VAOGraphicsState> {
    public GL30ChunkRenderBackend(GlVertexFormat<SodiumVertexFormats.ChunkMeshAttribute> format) {
        super(format);
    }

    @Override
    public void end(MatrixStack matrixStack) {
        GlFunctions.VERTEX_ARRAY.glBindVertexArray(0);

        super.end(matrixStack);
    }

    @Override
    public Class<VAOGraphicsState> getGraphicsStateType() {
        return VAOGraphicsState.class;
    }

    @Override
    protected VAOGraphicsState createGraphicsState() {
        return new VAOGraphicsState();
    }

    public static boolean isSupported() {
        return GlFunctions.isVertexArraySupported();
    }
}
