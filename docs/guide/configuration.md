# Configuration

Once HomeAssistantMC has been successfully installed, you need to setup the configuration. 

To do so, navigate to the `/.minecraft/config/` folder and create a new file named `homeassistantmc-common.toml` file. Edit the variables `url` and `token` to the url of your Home Assistant instance and long lived token respectively. Examples of urls:
- `ws://homeassistant.local:8123`
- `wss://sampletextXXX.ui.nabu.casa`

Join a Minecraft world and HomeAssistantMC will be successfully connected.

:::tip
If an entity has been modified on Home Assistant's end, you need to reload the websocket connection. This can be done either by rejoining the world again (singleplayer) or restarting the server (multiplayer).
:::