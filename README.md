<div align="center" style="text-align:center;"><img style="width: 100%" src="branding/assets/logo/PNG/citadelPNG1.png"></div>

<div align="center" style="text-align:center;">
    <a href="https://patreon.com/restonic4"><img src="https://img.shields.io/badge/Patreon-restonic4-orange?logo=patreon&style=for-the-badge"></a>
    <a href="https://github.com/restonic4"><img src="https://img.shields.io/badge/GitHub-restonic4-black?logo=github&style=for-the-badge"></a>
    <a href="https://discord.gg/GkAwCVhemV"><img src="https://img.shields.io/discord/1287697784572743790?label=Discord&logo=discord&style=for-the-badge"></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache-blue?style=for-the-badge"></a>
</div>

---

## Welcome

This is my first time programming a game engine. I am doing this because I don't quite like other engines. Don't get com wrong, those engines are really cool, but I prefer something else, something that I can 100% understand and modify without blowing my mind.

I never thought I would be able to do something like this, but here I am, creating a 3D game engine in java, that's crazy.

## Citadel engine

This engine is going to be used for my future games and projects, but if people really like it, I could add support for using it outside my own projects.

## Development Status

<ul>
    <li>✅ = Confirmed.</li>
    <li>❌ = Rejected / Canceled.</li>
    <li>❔ = Unknown / We don't know.</li>
    <li>❓ = We could accept help from the community/contributors, but we won't do it without help.</li>
</ul>

<table align="center" style="border-collapse: collapse; border: none;">
<td style="border: none;">

### Always In Development (🔄)
| Feature       | Status   |
|---------------|----------|
| 3D Rendering  | ✅        |
| Optimization  | ✅        |
| Modding API   | ✅        |
| Documentation | ✅        |

</td><td style="border: none;">

### Currently In Progress (⌛)
| Feature         | Status |
|-----------------|--------|
| Lighting        | ✅      |
| Shadows         | ✅      |
| Profiler        | ❔      |
| Frustum Culling | ✅      |
| Textures        | ✅      |
| Networking      | ✅      |
| Physics         | ❔      |
| Scene editor    | ✅      |

</td></table>

<table align="center" style="border-collapse: collapse; border: none;">
<td style="border: none;">

### Planned Features (🗓️)
| Feature                                        | Status |
|------------------------------------------------|--------|
| Object Culling / Occlusion culling             | ✅      |
| Steamworks Compatibility                       | ❔      |
| GUI System                                     | ❔      |
| Particles                                      | ✅      |
| Switchable render system (For older computers) | ❔      |
| Vulkan support                                 | ❓      |

</td><td style="border: none;">

### Recently Completed (✅)
| Feature              | Status |
|----------------------|--------|
| Save System          | ✅      |
| Batching             | ✅      |
| Sound                | ✅      |
| Registry System      | ✅      |
| Engine Configuration | ✅      |
| ImGui                | ✅      |
| Event System         | ✅      |
| Log System           | ✅      |

</td></table>

## Credits

Coded with ❤🧡💛💚💙💙💜🤎🖤🤍 by <a href="https://github.com/restonic4">restonic4</a>.

📚 = Library.
🔮 = App / Tool.

<table style="border: none; width: 100%;">
  <tr>
    <td style="vertical-align: top; border: none;">
      <h3>Libraries and tools used:</h3>
      <ul>
        <li><a href="https://www.lwjgl.org">📚 LWJGL 3</a></li>
        <li><a href="https://www.glfw.org">📚 GLFW</a></li>
        <li><a href="https://www.khronos.org/about/">📚 OpenGL</a></li>
        <li><a href="https://www.openal.org">📚 OpenAL</a></li>
        <li><a href="https://github.com/nothings/stb">📚 STB</a></li>
        <li><a href="https://github.com/ocornut/imgui">📚 Original ImGui</a> ---> <a href="https://github.com/SpaiR/imgui-java">📚 Java ImGui</a></li>
        <li><a href="https://github.com/netty/netty/">📚 Netty</a></li>
        <li><a href="https://gource.io">🔮 Gource</a></li>
        <li><a href="https://visualvm.github.io">🔮 VisualVM</a></li>
      </ul>
    </td>
    <td style="vertical-align: top; border: none;">
      <h3>Knowledge:</h3>
      <ul>
        <li><a href="https://www.youtube.com/@GamesWithGabe">Youtube: @GamesWithGabe</a></li>
        <li><a href="https://www.youtube.com/@ThinMatrix">Youtube: @ThinMatrix</a></li>
        <li><a href="https://www.youtube.com/watch?v=f05PwswO7qc">Youtube video by @KazeN64</a></li>
        <li><a href="https://www.youtube.com/watch?v=1LCEiVDHJmc">Youtube video by @standupmaths</a></li>
        <li><a href="https://www.youtube.com/watch?v=YTfdBSjitd8">Youtube video by @Aurailus</a></li>
        <li><a href="https://ahbejarano.gitbook.io/lwjglgamedev">LWJGL book by lwjglgamedev</a></li>
        <li><a href="https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content">Another LWJGL book by lwjglgamedev</a></li>
      </ul>
    </td>
  </tr>
</table>

For more information, check out the [docs](./resources/docs) in the `resources/docs` folder.

## How to install / use the engine

I will provide a better guide on the future, as well as documentation. But for now you can go to jitpack and implement as a gradle dependency the commit you desire.

### Using gradle groovy
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.restonic4:CitadelEngine:98c27d066b' // 98c27d066b is an example, choose the one you need.
}
```

If your IDE can't find the commit/version it could be that jitpack does not have it jet compiled, you can go to https://jitpack.io/#restonic4/CitadelEngine and check if that commit is available, if not, you can ask jitpack to build it.

## Projects using Citadel
<ul>
    <li>...</li>
</ul>

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Gource

100 commits:

[![100 commits](https://img.youtube.com/vi/ai7zSsb1ELU/0.jpg)](https://www.youtube.com/watch?v=ai7zSsb1ELU)

Next gources:
- 500 commits.
- 1k commits.
- 1 year of development.

## Stats

![Alt](https://repobeats.axiom.co/api/embed/d25d2b27e779d0f83b2dd83417d267881d2e99bc.svg "Repobeats analytics image")