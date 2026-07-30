# Better with Elevators

Block elevators for [Better than Adventure](https://betterthanadventure.net/). 

Requires [Melib](https://github.com/MelonMojito/Melib).

Stand on an elevator block and **jump** to teleport up to the next elevator block, or **sneak** to drop to the one below. By default the elevator block is the steel block, but any block can be used - see `/elevator block`.

Works on dedicated servers **and in singleplayer** as a client-side mod.

## Turning elevators on and off

There are two independent switches, and **both** must be on for a player to use an elevator:

| Switch     | Where                                       | Effect                                                                          |
|------------|---------------------------------------------|---------------------------------------------------------------------------------|
| World      | `/elevator toggle enabled` (admin)          | Off means nobody uses elevators here, no matter what any client has chosen.     |
| Per-player | Options → Better with Elevators → Elevators | A player opting out stops elevators for themselves only - including on servers. |

The client's choice is sent to the server shortly after joining and again whenever it is changed, so opting out works in multiplayer and not just singleplayer. Players without the mod installed simply count as opted in, and are governed by the world switch alone.

## Commands

`/elevator` requires admin (in singleplayer, that means cheats enabled).

| Command                              | Description                                                                                                                                                        |
|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/elevator info`                     | Show whether elevators are on, plus the current block, cooldown, and obstruction setting.                                                                          |
| `/elevator toggle enabled`           | Turn elevators on or off for the whole world/server.                                                                                                               |
| `/elevator block <block>`            | Set the elevator block. Accepts a namespace id (`minecraft:block/block_gold`) or a translation key (`block.gold`); tab completion suggests every registered block. |
| `/elevator block reset`              | Reset the elevator block back to steel.                                                                                                                            |
| `/elevator cooldown <0-256>`         | Ticks to wait between elevator uses. Default `6`.                                                                                                                  |
| `/elevator toggle allowobstructions` | Whether elevators can teleport through blocks in between. Default on.                                                                                              |

Settings are stored in `config/betterwithelevators/config.json` and can also be edited there directly:

```json
{
  "enabled": true,
  "allowObstructions": true,
  "elevatorCooldown": 6,
  "elevatorBlock": "minecraft:block/block_steel"
}
```

An unknown `elevatorBlock` falls back to steel with a warning in the log, so a typo or a block from an uninstalled mod will not break elevators.

## Notes

- Elevator logic runs on the logical server: a dedicated server, or the local world in singleplayer. A client connected to a remote server leaves the work to the server, so nothing is double-applied.
- Only the server needs the mod in multiplayer; clients need it only for singleplayer use.
