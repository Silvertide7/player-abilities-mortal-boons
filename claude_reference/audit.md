# Full mod audit — 2026-08-23 (third pass)

Self-audit + 2 reviewer agents (full src/main/java correctness; resources/docs/build/jar).
Everything in the mod was in scope: 8 abilities, 3 event handlers, 2 mixins, datapack, lang, docs, jar.

## Fixed this audit

- [x] Second Wind no longer fires on the killing blow itself (healthAfter > 0 guard) — previously it would regen a corpse and burn the 15-minute cooldown across a death when granted outside Mortal Boons
- [x] Second Wind retuned: fires on any hit landing at or below 30% health (was crossing-only); Guardian Angel save -> next hit now procs it
- [x] CurseForge copy updated for the new Second Wind behavior

- [x] WaterRunningSplashHandler splashed while wading chest-deep over waterlogged slabs/stairs — now bails on `isInWater()` (surface-running feet never touch fluid, so true positives are unaffected)
- [x] Swift Step description said "the next hit" — the hit that completes the 10 damage is the one that procs; text now says so
- [x] Echoing Blow description now mentions the proper-tool requirement
- [x] CurseForge copy: "open water" → "still water", Guardian Angel "hours" → "an hour"
- [x] CLAUDE.md tier design: spider_climb and water_running documented as Diamond+ passives; stale `getMaxLevel() == 4` line corrected to 3
- [x] ability_ideas.md: stale "skimmer" reference renamed; water_running row now records the splash handler for the 1.20.1 port

## Verified clean (with evidence)

- [x] Framework enforcement traced for every declared value: cooldowns, kill/damage requirements, effect grants, Guardian Angel setHealth-before-damage-zeroing, Second Wind edge-crossing semantics, Bloodscent record-then-fire kill ordering
- [x] Cross-ability interactions: Water Running + Spider Climb, Swift Step, Slipstream, Echoing Blow at LOWEST priority — no conflicts
- [x] Splash handler: cast safe, wading-on-solid-floor is a true negative, toggled-off emits nothing, spectator/creative never pass onGround, sound modulo nests correctly
- [x] All lang numbers match code constants; all 8 abilities fully wired (registry ↔ boon ↔ lang ↔ 32x32 icon); no orphans, no stale keys
- [x] Deps satisfied by installed jars (Player Abilities 1.0.8, Mortal Boons 1.2.0); mixins config exact; jar contents complete, no .DS_Store
- [x] No comments or dead code in src/main/java; no icon fields in boon JSONs

## Known and accepted

- [ ] Guardian Angel + Second Wind: the saving hit itself cannot proc Second Wind (framework zeroes lethal damage before HEALTH_DROPPED fires), but since Second Wind now fires on any hit landing at/below 30%, the next hit after an angel save procs it
- [ ] Bloodscent's first kill after grant procs immediately ("every second kill" is steady-state); Player Abilities seeds requirement progress only after the first fire — pleasant, framework-wide, left as is
- [ ] Splashes still show when sprinting across lily pads over water (feet above pad, water below) — reads fine visually
- [ ] Slipped arrows stutter a frame client-side; claim mods hooking only BreakEvent don't see echoed breaks (mayInteract covers spawn protection/world border)
- [ ] Water Running: attack mid-run drops you in; still-water only; no engagement from partial-height ground; muffled underwater explosions
- [ ] .gitignore excludes CLAUDE.md/.claude — kindred-convention, deliberate

## Pre-ship checklist

- [x] Drop all 8 boon weights 100 → 10 — done 2026-08-23
- [ ] In-game pass: Slipstream (slip + stop-sprint hit + slipped arrow stays slipped), splash feel at volume 0.12 / 10-tick interval, Echoing Blow echo
- [ ] CurseForge page: fill screenshot placeholders; README.md is a one-liner placeholder if you want more there
- [ ] Then: 4 active abilities remain to build
