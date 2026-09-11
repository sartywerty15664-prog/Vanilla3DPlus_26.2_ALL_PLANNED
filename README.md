# Vanilla 3D+ — Minecraft 26.2

Client-side Fabric visual enhancement project.

## Included in this rebuild

- Stronger multi-layer atmospheric haze for rain, thunder, night and dawn.
- Real multi-color rainbow particle arc after rain.
- Layered volumetric-looking cloud volumes built from vanilla particles.
- Water impact splashes and high-impact mist.
- Ground dust, snow and hard-landing effects.
- Material-colored held-item aura and weapon trails.
- Visible armor material effects.
- Custom Fabric 26.2 armor rendering for copper, iron, gold, diamond, netherite, chainmail and turtle armor.
- Shader-safe terrain relief pass using Fabric's 26.2 submit-rendering API; no raw OpenGL.
- O opens the in-game Vanilla 3+ settings screen.
- LOW / MEDIUM / HIGH quality modes.

## Build

The repository includes `.github/workflows/build.yml`.
GitHub Actions builds the JAR with Java 25 and Gradle 9.5.1.

Expected artifact:

`build/libs/vanilla_3d_plus-1.0.0.jar`

## Important

This archive is the complete project source. It is not a locally precompiled JAR because the current build environment does not contain the Minecraft/Fabric Gradle toolchain.
