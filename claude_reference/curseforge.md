![](<insert title banner png here>)

This mod implements abilities using the [Player Abilities](https://www.curseforge.com/minecraft/mc-mods/player-abilities) mod.  
Please see the [website](https://mods.silvertide.net/player-abilities) for more details on implementations.

Player Abilities: Mortal Boons is a set of abilities built on the Player Abilities framework. They were designed as rewards for [Mortal Boons](<insert mortal boons link here>), and this mod ships the boons that grant them. You are welcome to use this mod as is, or use its source as the basis for your own ability implementations. It doubles as a working example of how to build passive and triggered abilities against the Player Abilities API, including a passive that changes player movement.

The abilities here are passive and triggered rather than cast. Triggered abilities fire on their own when the moment comes and then go on cooldown, some of them also needing kills or damage taken before they will fire again. Passive abilities are always on and can be toggled off in the ability book. Most have three levels that improve duration, strength, or how much they restore. Every value can be tuned per pack through Player Abilities datapack configs.

![](<insert ability book screenshot here>)

## About Mortal Boons

Mortal Boons turns progression into a gamble. Craft a Fatestone, pay it experience and an item offering, and tempt fate for a boon at a random tier: Iron, Gold, Diamond, or Netherite. You can hold three at once, and death takes every one of them. With this mod installed, the Fatestone can roll these abilities alongside its own stat boons. Abilities never appear at Iron, and the tier decides the level you get: Gold grants level one, Diamond level two, Netherite level three. The strongest abilities refuse to appear below Diamond at all. The boons ship as a removable datapack, so packs can disable them and write their own.

![](<insert boon card screenshot here>)

## Passive Abilities

*    **![](<insert spider_climb.png here>) Spider Climb**: Climb any wall like a spider. Keep moving to keep climbing. Works on anything you can press against, and can be toggled off in the ability book.
*    **![](<insert water_running.png here>) Water Running**: Sprint across the surface of open water. Slow down or stop and it takes you back. Leaping from a sprint-swim lands you running on the waves.

## Triggered Abilities

*    **![](<insert guardian_angel.png here>) Guardian Angel**: When a blow would kill you, the wound never lands and you are restored to a share of your maximum health. Higher levels restore more of it. Hours pass before fate will step in again.
*    **![](<insert second_wind.png here>) Second Wind**: Dropping below a third of your health steadies you with regeneration for half a minute. Higher levels mend you faster.
*    **![](<insert bloodscent.png here>) Bloodscent**: Every second kill leaves you hunting, granting strength and speed. Higher levels hit harder and hold the scent longer.
*    **![](<insert swift_step.png here>) Swift Step**: Once you have taken enough punishment, the next hit sends you moving instead of reeling. Higher levels run faster and further.

More abilities are on the way, including active abilities cast from the ability wheel.

## Requirements

Requires the Player Abilities mod and NeoForge for Minecraft 1.21.1. Mortal Boons is optional, but it is what these abilities were built for.
