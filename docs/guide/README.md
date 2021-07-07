# Introduction

![Logo of HomeAssistantMC](./assets/images/logofull1.png)

HomeAssistantMC is a Minecraft mod that integrates Home Assistant to Minecraft, allowing retrieval of entity states and calling of services. This allows you to build and execute automations entirely ingame.

## Use cases
- Build and run automations/scripts
- Setup a 3D virtual dashboard
- Create a house floorplan
- Turn on smart devices without leaving the game
- Teach other people on home automation

## How it works

HomeAssistantMC connects to Home Assistant via the [WebSocket API](https://developers.home-assistant.io/docs/api/websocket/) with an access token. It subscribes to the event bus and listens for any entity state changes. Essentially, Minecraft becomes a websocket client of Home Asssitant.

## Why not...?

### Mine Assistant

[Mine Assistant](https://www.spigotmc.org/resources/mine-assistant.92469/) is a server plugin for Bukkit, a modification of Minecraft's server software. While its great for keeping things vanilla, it has limited functionality and can strictly only call services to turn on and off entities. HomeAssistantMC allows more freedom to configure and set entities to listen to as well as calling services even with custom yaml.

### ComputerCraft

[ComputerCraft](https://tweaked.cc/) is a Minecraft mod that adds programmable computers, turtles and more to the game. The current version of the mod is called CC Tweaked, as the original version has been discontinued. ComputerCraft does support WebSockets that can connect to Home Assistant, but it may be tedious and time consuming to set up. HomeAsssitantMC is designed for a plug and play experience as well as minimal maintanence for future Minecraft versions.