# FAQ

Answers to most frequent questions or questions that were never asked but good to know.

- [What versions of Minecraft are supported?](##does-this-mod-work-on-singleplayer)
- [Does this mod work on Singleplayer?](#does-this-mod-work-on-singleplayer)
- [How do I backup the stuff I built with this mod?](#how-do-i-backup-the-stuff-i-built-with-this-mod)
- [The mod fails to connect to Home Assistant when entering the world!](#the-mod-fails-to-connect-to-home-assistant-when-entering-the-world)
- [Minecraft crashes when I install the mod!](#minecraft-crashes-when-i-install-the-mod)
- [Do I need to install anything on Home Assistant?](#do-i-need-to-install-anything-on-home-assistant)
- [Can I use other mods with this?](#can-i-use-other-mods-with-this)
- [How can I contribute to this project?](#how-can-i-contribute-to-this-project)
- [Is there a fabric version available?](#is-there-a-fabric-version-available)
- [Can I control Minecraft from Home Assistant?](#can-i-control-minecraft-from-home-assistant)

## What versions of Minecraft are supported?

Currently supported Minecraft versions:

|        | Forge | Fabric |
|--------|-------|--------|
| 1.16.x | Yes   | TBD    |
| 1.12.x | No    | No     |

HomeAssistantMC will only be developed for 1.16+. There are no plans to develop for 1.12.

## Does this mod work on Singleplayer?

HomeAssistantMC works on both singleplayer and multiplayer. Do note that for singleplayer, it is recommended to enable `Open to LAN` to prevent the game from pausing and not receiving updates from Home Asssitant.

## How do I backup the stuff I built with this mod?

You can back up the Minecraft world folder located in `/.minecraft/saves/` if you are on singleplayer. It is always recommended to backup Minecraft worlds before using or updating mods.

## The mod fails to connect to Home Assistant when entering the world!

To check if the Minecraft world is connected to Home Assistant, run the command `/hass status`. If it is not connected, rejoin the game in singleplayer or restart the server in multiplayer. If it still does not work, ensure the variables in the `homeassistantmc-common.config` file are correct and retry again.

## Minecraft crashes when I install the mod!

Ensure that it is HomeAssistantMC that is causing the crash. If it does, submit an issue on the GitHub repository.

## Do I need to install anything on Home Assistant?

HomeAssistantMC works out of the box on its own, as long as you have the url and access token. No integration or custom components are needed as the mod only connects to Home Assistant as a websocket client. The access token can be retrieved from the profile page.

## How do I keep redstone running with nobody in the server?

The `/forceload` command was introduced in Minecraft 1.14, allowing players up to permission level 2 to set chunks to load constantly. Automation contraptions on these chunks will be able run continuously even without players.

## Can I use other mods with this?

HomeAssistantMC is built with the Forge API, allowing compatibility with other Minecraft forge mods. You can try and install redstone related mods such as [Create](https://www.curseforge.com/minecraft/mc-mods/create) and [More Red](https://www.curseforge.com/minecraft/mc-mods/more-red) to create even more complex automation contraptions.

## How can I contribute to this project?

The source code for HomeAssistantMC is on GitHub. Feel free to submit pull requests to improve code or add new functionalities. 

## Is there a fabric version available?

A fabric port will be developed in the near future. 

## Can I control Minecraft from Home Assistant?

Controlling Minecraft from Home Assistant is possible, but access to command blocks is required. Create helper entities in Home Assistant and listen to them with the state block. You can then connect the redstone signal output of the state block ingame to a command block. 

If you would like to dynamically trigger automations based on the data from the Minecraft world (current biome, player health), check out the MqttMC mod (In development). 