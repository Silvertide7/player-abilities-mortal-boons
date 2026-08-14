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
| `bloodscent` | Triggered | `KILL`, 30s cooldown | Wolf | Gold 1, Diamond 2, Netherite 3 |
| `swift_step` | Triggered | `DAMAGE_TAKEN`, 90s cooldown | Hare | Gold 1, Diamond 2, Netherite 3 |

## Open slots

4 passive, 4 active. Triggered is full.

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

`PassiveAbility` gives `getAttributeGrants(level)` plus `onActivated` / `onDeactivated`. There is no
tick hook — ongoing behavior means starting an `AbilityTickJobs.schedule` job in `onActivated` and
cancelling its handle in `onDeactivated`.

| Name | Sign | Sketch | Cost |
| --- | --- | --- | --- |
| `attuned` | Raven | Grants `player_abilities:ability_cooldown` — every other ability comes back faster. Nothing in Mortal Boons touches this attribute. | Trivial, attribute only |
| `wellspring` | Wyrm | Grants `player_abilities:ability_power`, scaling any ability that reads `AbilityAPI.getAbilityPower`. Worth holding until some ability actually reads it. | Trivial |
| `nightsight` | Raven | Night Vision whenever the light level around you is low; drops off in daylight. | Tick job |
| `riverblood` | Wyrm | While submerged: water breathing and Dolphin's Grace. | Tick job |
| `warm_blooded` | Wyrm | Immunity to freezing, and powder snow does not slow you. | Tick job |

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
