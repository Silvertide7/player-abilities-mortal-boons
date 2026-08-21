# Water Running audit

Self-audit + 4 independent reviewer agents (line-by-line, removed-behavior, cross-file, cleanup/conventions), 2026-08-20.

## Fixed

- [x] Fall damage: sprinting players landing on water from height took full fall damage — `fallDistance > 3` now splashes through instead of solidifying (LiquidBlockWaterRunMixin)
- [x] Elytra gliders slammed into the solidified surface — `isFallFlying()` now excluded
- [x] Dead `isSpectator()` guards removed from both mixins (spectators always have `abilities.flying = true` and are noPhysics)
- [x] `LEVEL != 0` property lookup collapsed into `fluidState.isSource()` — same semantics, one concept, cheaper
- [x] Tooltip now says "still water" so flowing-edge dunks read as the rule, not a bug
- [x] Design decisions recorded in ability_ideas.md (why LiquidBlock not canStandOnFluid, why full cube, why stateless)
- [x] Legacy check: deleted LivingEntityWaterRunMixin fully gone, no stale grace/underwater references, mixins.json exact

## Verified clean (reviewers)

- [x] Registration, instance identity, mixins.json, mods.toml [[mixins]], boon JSON level defaults, lang keys
- [x] Guard ordering cheapest-first; only allocation (`pos.above()`) reached solely by actual runners; `Shapes.block()` is a cached singleton
- [x] Mob pathfinding, suffocation, projectiles, block placement, boats/vehicles: all use non-player or wrong-entity contexts, unaffected
- [x] Client/server agree (sprint flag and passive level both synced) — no rubber-banding
- [x] No invisible ceiling for swimmers (isAbove + water-above checks), no NPE paths

## Open — user decision / user task

- [ ] Add `water_running.png` (32x32) — boon card blits unconditionally, shows checkerboard until then
- [ ] Attacking mid-run drops you in (vanilla cancels sprint on melee). Accepted for now; re-add a 2-tick grace if it feels bad in combat
- [ ] Entry from partial-height blocks (farmland, path, slabs) doesn't engage — jump as you cross. Accepted; no cheap fix without reintroducing the waist-deep bug
- [ ] Flowing water / bubble columns / waterlogged blocks are holes in the surface. Accepted as "still water only"
- [ ] Underwater explosions barely hurt a sprinting runner (exposure rays hit the solid surface). Accepted quirk
- [ ] Drop all four test weights (100 → 10) before shipping
