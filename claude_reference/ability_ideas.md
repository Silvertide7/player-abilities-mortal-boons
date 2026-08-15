# Ability ideas

Backlog for this mod. Built abilities move out of "Open slots" and into "Shipped".

## Ground rules

- **Target roster: 4 passive, 4 triggered, 4 active.**
- **Never set `icon` in a boon JSON.** Art goes at
  `assets/playerabilities_mortalboons/textures/ability/<ability_path>.png` (32x32), and both the
  Player Abilities UI and the Mortal Boons card derive the path from the ability id.
- **Never duplicate a Mortal Boons attribute boon.** Those 19 already cover: max_health, armor,
  armor_toughness, knockback_resistance, explosion_knockback_resistance, movement_speed,
  movement_efficiency, jump_strength, safe_fall_distance, fall_damage_multiplier, sneaking_speed,
  attack_damage, attack_speed, attack_knockback, sweeping_damage_ratio, mining_efficiency, luck,
  oxygen_bonus, burning_time, block/entity_interaction_range. A passive here has to do something
  attributes cannot, or grant an attribute only Player Abilities has.
- Tiers: no ability boons at Iron. Triggered/passive Gold+, active Diamond+ (see CLAUDE.md).

## Shipped

| Ability | Kind | Trigger / use | Sign | Levels |
| --- | --- | --- | --- | --- |
| `guardian_angel` | Triggered | `LETHAL_DAMAGE` | Bear | Diamond 1, Netherite 3 |
| `second_wind` | Triggered | `HEALTH_DROPPED` below 30% | Bear | Gold 1, Diamond 2, Netherite 3 |
| `bloodscent` | Triggered | `KILL`, 30s cooldown + 2 kills | Wolf | Gold 1, Diamond 2, Netherite 3 |
| `swift_step` | Triggered | `DAMAGE_TAKEN`, 90s cooldown + 10 damage | Hare | Gold 1, Diamond 2, Netherite 3 |
| `spider_climb` | Passive | Spider climb via `LivingEntity#onClimbable` mixin | Wyrm | Diamond+, single level |

## Open slots

3 passive, 4 active. Triggered is full.

Mixin infrastructure now exists (`playerabilities_mortalboons.mixins.json` + `[[mixins]]` in the
mods.toml template) — `skimmer`-style movement passives no longer pay the setup cost, but each mixin
still has to be ported by hand on the 1.20.1 Forge branch.

All four boons are at weight 100 for testing (Guardian Angel `[0, 0, 100, 100]`, the rest
`[0, 100, 100, 100]`). Drop them back to 10 before shipping.

## Triggered ideas

| Name | Trigger | Sign | Sketch | Cost |
| --- | --- | --- | --- | --- |
| `deathwish` | `DEATH` | Wolf | Dying detonates a burst of damage around you — a parting shot, on-theme for a mod where death takes everything. | AoE damage in `onTrigger` |
| `cinderback` | `DAMAGE_TAKEN` | Wyrm | Melee attackers catch fire; brief fire resistance for you. Context's `DamageSource.getEntity()` gives the attacker. | Small |
| `executioner` | `DEALT_DAMAGE` | Wolf | Hitting a target already under 30% health deals bonus damage. `shouldTrigger` reads `target.getHealth()`. | Small — watch re-entry, see API notes |
| `feast` | `EAT` | Bear | Eating grants Saturation/Absorption briefly. Short cooldown. | Declarative |
| `waking_vision` | `WAKE_UP` | Raven | Sleeping through the night grants Night Vision + Regeneration for several minutes. Unusual trigger, strong flavor. | Declarative |
| `counterstrike` | `SHIELD_BLOCK` | Bear | Blocking a heavy hit grants Resistance. Context is only the blocked damage — no attacker, so no riposte. | Declarative |

## Passive ideas

**Passives must do something, not just grant attributes.** Mortal Boons already owns flat stats.

Three implementation lanes:

1. **Our own event handler.** Player Abilities does not need to expose a hook — an
   `@EventBusSubscriber` in this mod can listen to any NeoForge event and gate on
   `AbilityAPI.getPassiveLevel(player, ABILITY) > 0`, which returns 0 when the player toggles the
   passive off. `ModAbilities` holds the instances as static finals so handlers can reference them.
   This is the cheapest lane and covers most ideas.
2. **Tick job.** `AbilityTickJobs.schedule` in `onActivated`, cancel the handle in `onDeactivated`.
   Safe as of Player Abilities 1.0.7, which fixed jobs stacking on death.
3. **Mixin.** Only for movement and physics, where vanilla offers no hook. This mod has no mixin
   setup yet, and whatever we add has to be redone on the 1.20.1 Forge branch.

| Name | Sign | What it does | Lane |
| --- | --- | --- | --- |
| `skimmer` | Hare | Sprint across water without sinking; sneak to drop in. | Mixin — `LivingEntity#canStandOnFluid` returning true for water, the way `Strider` does for lava. Verify collision in dev |
| `unseen` | Raven | Hostile mobs will not target you until you strike first. | Event — cancel `LivingChangeTargetEvent` when the new target is a holder who has not hurt that mob |
| `cinderheart` | Wyrm | Fire and lava barely hurt, and you never catch alight. | Event — `LivingDamageEvent.Pre`, reduce when `source.is(DamageTypeTags.IS_FIRE)`, plus `clearFire` |
| `soulbound` | Raven | Keep a share of your experience through death. | Event — `PlayerEvent.Clone` on `wasDeath`, restore a fraction of the original's XP. Passive is still granted at this point; Mortal Boons revokes at `PlayerRespawnEvent` |
| `carrion_sense` | Wolf | Everything you kill drops more. | Event — `LivingDropsEvent`, duplicate a share of the drops |
| `quarryhand` | Wyrm | Ore you break sometimes yields a second drop, no enchantment involved. | Event — `BlockEvent.BreakEvent` or `HarvestDrops` |
| `ironstomach` | Bear | Sprinting and jumping cost you no hunger, and rotten flesh never sickens you. | Tick job zeroing exhaustion, plus an event to strip the hunger effect |
| `warm_blooded` | Bear | Freezing cannot hurt you and powder snow does not slow you. | Tick job — `setTicksFrozen(0)` |
| `riverblood` | Wyrm | Never drown, swim like a dolphin, and mine at full speed underwater. | Tick job |
| `momentum` | Hare | Sustained sprinting keeps building speed; it resets when you stop. | Tick job counting continuous sprint ticks |
| `nightsight` | Raven | See in the dark wherever the light level is low. | Tick job refreshing Night Vision well before it flickers |

## Active ideas

Diamond+ only. `INSTANT` needs no cast bar; `CHARGED` / `CHANNELED` take `getUseTicks` and can set
`requiresStationary`.

| Name | Use type | Sign | Sketch | Cost |
| --- | --- | --- | --- | --- |
| `warcry` | INSTANT | Wolf | Nearby hostiles take Weakness + Slowness; you take Strength. | Entity search + effects |
| `dash` | INSTANT | Hare | Lunge the way you are facing via `setDeltaMovement`, with brief fall protection on the landing. | Small |
| `veil` | INSTANT | Raven | Invisibility, and nearby mobs drop you as their target. | Small |
| `molten_step` | INSTANT | Wyrm | Erupt a ring of flame, igniting everything in it. | Block/entity loop |
| `meditate` | CHANNELED | Bear | Stationary channel that restores health and hunger over its duration; moving breaks it. Player Abilities' dev `RestfulMeditationAbility` is a working reference. | Medium |
| `recall` | CHARGED | Raven | Long stationary cast that teleports you to your respawn point. Netherite-tier power. | Medium |

## API notes and limits

- **Fall damage cannot be cancelled from the `FALL` trigger.** Player Abilities only auto-cancels for
  `LETHAL_DAMAGE`, where a firing ability sets the damage to 0. Fall mitigation belongs in a passive
  using `safe_fall_distance` / `fall_damage_multiplier` — and Mortal Boons already ships both.
- **`HEALTH_DROPPED` requires actual damage.** It fires from `LivingDamageEvent.Post`, so losing max
  health (a boon being revoked, absorption ending) never fires it.
- **`droppedBelow(fraction)` fires only on the crossing hit.** For "any hit while under the
  threshold," compare `healthAfter()` against `maxHealth()` directly in `shouldTrigger`.
- **Re-entry is guarded per (player, ability).** An ability that deals damage inside `onTrigger` on a
  damage trigger will not recurse into itself, but it can still fire *other* damage-triggered
  abilities.
- **Exclusive triggers stop at the first ability that fires.** Only `LETHAL_DAMAGE` is exclusive —
  two lethal-damage abilities on one player means one of them never fires.
- **Cooldowns scale with `player_abilities:ability_cooldown`**, and creative players bypass cooldowns
  and requirements entirely — worth remembering when testing.
