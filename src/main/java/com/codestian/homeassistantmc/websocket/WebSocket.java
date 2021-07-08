package com.codestian.homeassistantmc.websocket;

import com.codestian.homeassistantmc.worldsaveddata.EntityData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class WebSocket extends WebSocketClient {

    private final Logger logger = Logger.getLogger("HomeAssistant");
    private final Gson gson = new Gson();
    private final String accessToken;
    private final MinecraftServer server;
    private final EntityData entityData;
    private int id = 0;
    private String status = "";

    public WebSocket(String url, String accessToken, MinecraftServer server) throws URISyntaxException {
        super(new URI(url + "/api/websocket"));
        this.accessToken = accessToken;
        this.server = server;
        this.entityData = EntityData.get(server.overworld());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("Opening socket to Home Assistant...");
    }

    @Override
    public void onMessage(String message) {
        final JsonObject response = gson.fromJson(message, JsonObject.class);
        switch (response.get("type").getAsString()) {
            case "auth_required":
                sendAuth();
                status = "Attempting connection";
                server.sendMessage(new TranslationTextComponent("[HomeAssistantMC] Attemping to connect to Home Assistant..."), Util.NIL_UUID);
                break;
            case "auth_invalid":
                logger.warning("Unable to authenticate with Home Assistant: " + response.get("message").getAsString());
                status = "Failed to connect: " + response.get("message").getAsString();
                server.sendMessage(new TranslationTextComponent("[HomeAssistantMC] Unable to connect: " + response.get("message").getAsString()), Util.NIL_UUID);
                break;
            case "auth_ok":
                logger.info("Successfully connected to Home Assistant");
                logger.info("Retrieving entities...");
                status = "Successfully connected";
                server.sendMessage(new TranslationTextComponent("[HomeAssistantMC] Successfully connected!"), Util.NIL_UUID);
                getEntities();
                break;
            case "result":
                if (id == 1) {
                    if (!response.get("success").getAsBoolean()) {
                        logger.warning("An error occurred with retrieving entities from Home Assistant");
                    }
                    logger.info("Successfully retrieved all entities");
                    storeEntities(response.getAsJsonArray("result"));
                    logger.info("Storing the entities...");
                    setListenEvent();
                } else if (id == 2) {
                    logger.info("Listening to state changes...");
                    setListenEvent();
                } else if (id == 3) {
                    logger.info("Successfully subscribed to state changes events. Listening...");
                    server.sendMessage(new TranslationTextComponent("[HomeAssistantMC] Listening to state changes..."), Util.NIL_UUID);
                }
                break;
            case "event":
                if (response.get("event").getAsJsonObject().get("event_type").getAsString().equals("state_changed")) {
                    JsonObject entity_new_state = response.get("event").getAsJsonObject().get("data").getAsJsonObject().get("new_state").getAsJsonObject();
                    updateEntity(entity_new_state);
                }
                break;
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        status = "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason;
        logger.warning("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        status = "Error: " + ex.getMessage();
        logger.warning(ex.getMessage());
    }

    public void sendAuth() {
        JsonObject obj = new JsonObject();
        obj.add("type", new JsonPrimitive("auth"));
        obj.add("access_token", new JsonPrimitive(accessToken));
        send(gson.toJson(obj));
    }

    public void getEntities() {
        JsonObject obj = new JsonObject();
        obj.add("id", new JsonPrimitive(++id));
        obj.add("type", new JsonPrimitive("get_states"));
        send(gson.toJson(obj));
    }

    public void storeEntities(JsonArray result) {
        entityData.putEntities(result);
    }

    public void updateEntity(JsonObject result) {
        entityData.putOneEntity(result);
    }

    public void callService(String domain, String service, JsonObject data, JsonObject target) {
        JsonObject obj = new JsonObject();
        obj.add("id", new JsonPrimitive(++id));
        obj.add("type", new JsonPrimitive("call_service"));
        obj.add("domain", new JsonPrimitive(domain));
        obj.add("service", new JsonPrimitive(service));
        obj.add("service_data", data);
        obj.add("target", target);
        send(gson.toJson(obj));
    }

    public void setListenEvent() {
        JsonObject obj = new JsonObject();
        obj.add("id", new JsonPrimitive(++id));
        obj.add("type", new JsonPrimitive("subscribe_events"));
        obj.add("event_type", new JsonPrimitive("state_changed"));
        send(gson.toJson(obj));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
