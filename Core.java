package yarnwrap;

import java.util.*;
import net.fabricmc.fabric.api.loot.v2.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents.*;
import net.minecraft.server.network.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext.*;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.*;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents.*;
import net.minecraft.world.chunk.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents.*;
import net.minecraft.loot.*;
import net.fabricmc.fabric.api.lookup.v1.item.*;
import net.fabricmc.fabric.api.lookup.v1.block.*;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents.*;
import net.minecraft.component.*;
import net.fabricmc.fabric.api.networking.v1.S2CConfigurationChannelEvents.*;
import net.minecraft.server.*;
import net.fabricmc.fabric.api.loot.v3.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents.*;
import net.minecraft.text.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.*;
import net.minecraft.client.gui.hud.*;
import net.fabricmc.fabric.api.client.particle.v1.*;
import net.fabricmc.fabric.api.transfer.v1.context.*;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents.*;
import java.time.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.*;
import net.fabricmc.fabric.api.event.*;
import net.minecraft.util.math.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents.*;
import net.minecraft.client.network.*;
import net.fabricmc.fabric.api.client.message.v1.*;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.*;
import net.fabricmc.fabric.api.message.v1.*;
import net.minecraft.potion.*;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent.*;
import java.util.function.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.*;
import net.minecraft.component.type.*;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.resource.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.entity.player.*;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents.*;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.world.*;
import net.fabricmc.fabric.impl.transfer.fluid.*;
import net.fabricmc.fabric.api.util.*;
import net.minecraft.client.render.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.registry.entry.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.*;
import net.minecraft.registry.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.*;
import net.minecraft.entity.damage.*;
import org.jetbrains.annotations.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents.*;
import net.minecraft.server.world.*;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents.*;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.*;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents.*;
import net.fabricmc.fabric.api.client.networking.v1.C2SConfigurationChannelEvents.*;
import net.minecraft.fluid.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents.*;
import net.fabricmc.fabric.impl.itemgroup.*;
import net.fabricmc.fabric.mixin.transfer.*;
import net.fabricmc.fabric.api.event.client.player.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.*;
import net.minecraft.network.packet.*;
import net.fabricmc.fabric.impl.client.screen.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.hit.*;
import net.minecraft.network.message.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.*;
import net.fabricmc.fabric.api.event.client.player.ClientPlayerBlockBreakEvents.*;
import net.minecraft.entity.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.*;
import net.fabricmc.fabric.api.entity.event.v1.*;
import net.minecraft.client.gui.screen.*;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.*;
import net.fabricmc.fabric.api.itemgroup.v1.*;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents.*;
import net.minecraft.server.command.*;
import net.minecraft.entity.mob.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents.*;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents.*;
import net.minecraft.client.world.*;
import net.minecraft.item.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.*;
import net.minecraft.util.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents.*;
import net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents.*;
import com.mojang.authlib.*;
import net.fabricmc.fabric.api.client.screen.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents.*;

public class Core extends ws.siri.jscore.wraps.Runnable implements
net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents.Load,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents.Generate,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents.Unload,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarting,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopped,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SyncDataPackContents,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.StartDataPackReload,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.EndDataPackReload,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.BeforeSave,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.AfterSave,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.Load,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.Unload,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.EquipmentChange,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents.Load,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents.Unload,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.StartTick,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.StartWorldTick,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndWorldTick,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents.Load,
net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents.Unload,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStarted,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents.Load,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents.Unload,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartWorldTick,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndWorldTick,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents.Load,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents.Unload,
net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents.AfterClientWorldChange,
net.fabricmc.fabric.api.message.v1.ServerMessageEvents.AllowChatMessage,
net.fabricmc.fabric.api.message.v1.ServerMessageEvents.AllowGameMessage,
net.fabricmc.fabric.api.message.v1.ServerMessageEvents.AllowCommandMessage,
net.fabricmc.fabric.api.message.v1.ServerMessageEvents.ChatMessage,
net.fabricmc.fabric.api.message.v1.ServerMessageEvents.GameMessage,
net.fabricmc.fabric.api.message.v1.ServerMessageEvents.CommandMessage,
net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.AllowChat,
net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.AllowGame,
net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.ModifyGame,
net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Chat,
net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game,
net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.ChatCanceled,
net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.GameCanceled,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowChat,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowCommand,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.ModifyChat,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.ModifyCommand,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.Chat,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.Command,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.ChatCanceled,
net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.CommandCanceled,
net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries,
net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntriesAll,
net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage.CombinedItemApiProvider,
net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents.Configure,
net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents.Disconnect,
net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Init,
net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Join,
net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Disconnect,
net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents.StartTracking,
net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents.StopTracking,
net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents.Register,
net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents.Unregister,
net.fabricmc.fabric.api.networking.v1.S2CConfigurationChannelEvents.Register,
net.fabricmc.fabric.api.networking.v1.S2CConfigurationChannelEvents.Unregister,
net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents.Init,
net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents.QueryStart,
net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents.Disconnect,
net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Init,
net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Join,
net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect,
net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents.Register,
net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents.Unregister,
net.fabricmc.fabric.api.client.networking.v1.C2SConfigurationChannelEvents.Register,
net.fabricmc.fabric.api.client.networking.v1.C2SConfigurationChannelEvents.Unregister,
net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents.Init,
net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents.QueryStart,
net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents.Disconnect,
net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents.Init,
net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents.Start,
net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents.Complete,
net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents.Disconnect,
net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents.Ready,
net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.Before,
net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.After,
net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.Canceled,
net.fabricmc.fabric.api.event.client.player.ClientPlayerBlockBreakEvents.After,
net.fabricmc.fabric.api.loot.v3.LootTableEvents.Replace,
net.fabricmc.fabric.api.loot.v3.LootTableEvents.Modify,
net.fabricmc.fabric.api.loot.v3.LootTableEvents.Loaded,
net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents.AllowBlockDustTint,
net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents.ModifyCallback,
net.fabricmc.fabric.api.item.v1.EnchantmentEvents.AllowEnchanting,
net.fabricmc.fabric.api.item.v1.EnchantmentEvents.Modify,
net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents.AllowCapeRender,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.Start,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.AfterSetup,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BeforeEntities,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.AfterEntities,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BeforeBlockOutline,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BlockOutline,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.DebugRender,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.AfterTranslucent,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.Last,
net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.End,
net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BeforeInit,
net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterInit,
net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.Remove,
net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BeforeRender,
net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterRender,
net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BeforeTick,
net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterTick,
net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents.AfterKilledOtherEntity,
net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents.AfterEntityChange,
net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents.AfterPlayerChange,
net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents.Allow,
net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents.Custom,
net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.CopyFrom,
net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn,
net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AllowDeath,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.AllowSleeping,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.StartSleeping,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.StopSleeping,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.AllowBed,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.AllowSleepTime,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.AllowNearbyMonsters,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.AllowResettingTime,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.ModifySleepingDirection,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.AllowSettingSpawn,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.SetBedOccupationState,
net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.ModifyWakeUpPosition,
net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDamage,
net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDamage,
net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDeath,
net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath,
net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.MobConversion {

private Core(String ident, String function) {
    super(ident, function);
}

public static Core runnable(String ident, String function) {
    return (Core) Core.create(ident, function);
}

public static Core getRunnable(String ident) {
    return (Core) Core.runnables.get(ident);
}

@Override
public void onChunkLoad(ServerWorld world, WorldChunk chunk) { runF(world, chunk);  }

@Override
public void onChunkGenerate(ServerWorld world, WorldChunk chunk) { runF(world, chunk);  }

@Override
public void onChunkUnload(ServerWorld world, WorldChunk chunk) { runF(world, chunk);  }

@Override
public void onServerStarting(MinecraftServer server) { runF(server);  }

@Override
public void onServerStarted(MinecraftServer server) { runF(server);  }

@Override
public void onServerStopping(MinecraftServer server) { runF(server);  }

@Override
public void onServerStopped(MinecraftServer server) { runF(server);  }

@Override
public void onSyncDataPackContents(ServerPlayerEntity player, boolean joined) { runF(player, joined);  }

@Override
public void startDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager) { runF(server, resourceManager);  }

@Override
public void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success) { runF(server, resourceManager, success);  }

@Override
public void onBeforeSave(MinecraftServer server, boolean flush, boolean force) { runF(server, flush, force);  }

@Override
public void onAfterSave(MinecraftServer server, boolean flush, boolean force) { runF(server, flush, force);  }

@Override
public void onLoad(Entity entity, ServerWorld world) { runF(entity, world);  }

@Override
public void onUnload(Entity entity, ServerWorld world) { runF(entity, world);  }

@Override
public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) { runF(livingEntity, equipmentSlot, previousStack, currentStack);  }

@Override
public void onLoad(BlockEntity blockEntity, ServerWorld world) { runF(blockEntity, world);  }

@Override
public void onUnload(BlockEntity blockEntity, ServerWorld world) { runF(blockEntity, world);  }

@Override
public void onStartTick(MinecraftServer server) { runF(server);  }

@Override
public void onEndTick(MinecraftServer server) { runF(server);  }

@Override
public void onStartTick(ServerWorld world) { runF(world);  }

@Override
public void onEndTick(ServerWorld world) { runF(world);  }

@Override
public void onWorldLoad(MinecraftServer server, ServerWorld world) { runF(server, world);  }

@Override
public void onWorldUnload(MinecraftServer server, ServerWorld world) { runF(server, world);  }

@Override
public void onChunkLoad(ClientWorld world, WorldChunk chunk) { runF(world, chunk);  }

@Override
public void onChunkUnload(ClientWorld world, WorldChunk chunk) { runF(world, chunk);  }

@Override
public void onClientStarted(MinecraftClient client) { runF(client);  }

@Override
public void onClientStopping(MinecraftClient client) { runF(client);  }

@Override
public void onLoad(BlockEntity blockEntity, ClientWorld world) { runF(blockEntity, world);  }

@Override
public void onUnload(BlockEntity blockEntity, ClientWorld world) { runF(blockEntity, world);  }

@Override
public void onStartTick(MinecraftClient client) { runF(client);  }

@Override
public void onEndTick(MinecraftClient client) { runF(client);  }

@Override
public void onStartTick(ClientWorld world) { runF(world);  }

@Override
public void onEndTick(ClientWorld world) { runF(world);  }

@Override
public void onLoad(Entity entity, ClientWorld world) { runF(entity, world);  }

@Override
public void onUnload(Entity entity, ClientWorld world) { runF(entity, world);  }

@Override
public void afterWorldChange(MinecraftClient client, ClientWorld world) { runF(client, world);  }

@Override
public boolean allowChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) { runF(message, sender, params); return true;  }

@Override
public boolean allowGameMessage(MinecraftServer server, Text message, boolean overlay) { runF(server, message, overlay); return true;  }

@Override
public boolean allowCommandMessage(SignedMessage message, ServerCommandSource source, MessageType.Parameters params) { runF(message, source, params); return true;  }

@Override
public void onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) { runF(message, sender, params);  }

@Override
public void onGameMessage(MinecraftServer server, Text message, boolean overlay) { runF(server, message, overlay);  }

@Override
public void onCommandMessage(SignedMessage message, ServerCommandSource source, MessageType.Parameters params) { runF(message, source, params);  }

@Override
public boolean allowReceiveChatMessage(Text message, SignedMessage signedMessage, GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) { runF(message, signedMessage, sender, params, receptionTimestamp); return true;  }

@Override
public boolean allowReceiveGameMessage(Text message, boolean overlay) { runF(message, overlay); return true;  }

@Override
public Text modifyReceivedGameMessage(Text message, boolean overlay) { runF(message, overlay); return null;  }

@Override
public void onReceiveChatMessage(Text message, SignedMessage signedMessage, GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) { runF(message, signedMessage, sender, params, receptionTimestamp);  }

@Override
public void onReceiveGameMessage(Text message, boolean overlay) { runF(message, overlay);  }

@Override
public void onReceiveChatMessageCanceled(Text message, SignedMessage signedMessage, GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) { runF(message, signedMessage, sender, params, receptionTimestamp);  }

@Override
public void onReceiveGameMessageCanceled(Text message, boolean overlay) { runF(message, overlay);  }

@Override
public boolean allowSendChatMessage(String message) { runF(message); return true;  }

@Override
public boolean allowSendCommandMessage(String command) { runF(command); return true;  }

@Override
public String modifySendChatMessage(String message) { runF(message); return null;  }

@Override
public String modifySendCommandMessage(String command) { runF(command); return null;  }

@Override
public void onSendChatMessage(String message) { runF(message);  }

@Override
public void onSendCommandMessage(String command) { runF(command);  }

@Override
public void onSendChatMessageCanceled(String message) { runF(message);  }

@Override
public void onSendCommandMessageCanceled(String command) { runF(command);  }

@Override
public void modifyEntries(FabricItemGroupEntries entries) { runF(entries);  }

@Override
public void modifyEntries(ItemGroup group, FabricItemGroupEntries entries) { runF(group, entries);  }

@Override
public 		Storage<FluidVariant> find(ContainerItemContext context) { runF(context); return null;  }

@Override
public void onSendConfiguration(ServerConfigurationNetworkHandler handler, MinecraftServer server) { runF(handler, server);  }

@Override
public void onConfigureDisconnect(ServerConfigurationNetworkHandler handler, MinecraftServer server) { runF(handler, server);  }

@Override
public void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server) { runF(handler, server);  }

@Override
public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) { runF(handler, sender, server);  }

@Override
public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) { runF(handler, server);  }

@Override
public void onStartTracking(Entity trackedEntity, ServerPlayerEntity player) { runF(trackedEntity, player);  }

@Override
public void onStopTracking(Entity trackedEntity, ServerPlayerEntity player) { runF(trackedEntity, player);  }

@Override
public void onChannelRegister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels) { runF(handler, sender, server, channels);  }

@Override
public void onChannelUnregister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels) { runF(handler, sender, server, channels);  }

@Override
public void onChannelRegister(ServerConfigurationNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels) { runF(handler, sender, server, channels);  }

@Override
public void onChannelUnregister(ServerConfigurationNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels) { runF(handler, sender, server, channels);  }

@Override
public void onLoginInit(ServerLoginNetworkHandler handler, MinecraftServer server) { runF(handler, server);  }

@Override
public void onLoginStart(ServerLoginNetworkHandler handler, MinecraftServer server, LoginPacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) { runF(handler, server, sender, synchronizer);  }

@Override
public void onLoginDisconnect(ServerLoginNetworkHandler handler, MinecraftServer server) { runF(handler, server);  }

@Override
public void onPlayInit(ClientPlayNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) { runF(handler, sender, client);  }

@Override
public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onChannelRegister(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels) { runF(handler, sender, client, channels);  }

@Override
public void onChannelUnregister(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels) { runF(handler, sender, client, channels);  }

@Override
public void onChannelRegister(ClientConfigurationNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels) { runF(handler, sender, client, channels);  }

@Override
public void onChannelUnregister(ClientConfigurationNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels) { runF(handler, sender, client, channels);  }

@Override
public void onLoginStart(ClientLoginNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onLoginQueryStart(ClientLoginNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onLoginDisconnect(ClientLoginNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onConfigurationInit(ClientConfigurationNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onConfigurationStart(ClientConfigurationNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onConfigurationComplete(ClientConfigurationNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onConfigurationDisconnect(ClientConfigurationNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public void onConfigurationReady(ClientConfigurationNetworkHandler handler, MinecraftClient client) { runF(handler, client);  }

@Override
public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) { runF(world, player, pos, state, blockEntity); return true;  }

@Override
public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) { runF(world, player, pos, state, blockEntity);  }

@Override
public void onBlockBreakCanceled(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) { runF(world, player, pos, state, blockEntity);  }

@Override
public void afterBlockBreak(ClientWorld world, ClientPlayerEntity player, BlockPos pos, BlockState state) { runF(world, player, pos, state);  }

@Override
public 		LootTable replaceLootTable(RegistryKey<LootTable> key, LootTable original, LootTableSource source, RegistryWrapper.WrapperLookup registries) { runF(key, original, source, registries); return null;  }

@Override
public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) { runF(key, tableBuilder, source, registries);  }

@Override
public void onLootTablesLoaded(ResourceManager resourceManager, Registry<LootTable> lootRegistry) { runF(resourceManager, lootRegistry);  }

@Override
public boolean allowBlockDustTint(BlockState state, ClientWorld world, BlockPos pos) { runF(state, world, pos); return true;  }

@Override
public void modify(ModifyContext context) { runF(context);  }

@Override
public TriState allowEnchanting(RegistryEntry<Enchantment> enchantment, ItemStack target, EnchantingContext enchantingContext) { runF(enchantment, target, enchantingContext); return null;  }

@Override
public void modify(RegistryKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) { runF(key, builder, source);  }

@Override
public boolean allowCapeRender(AbstractClientPlayerEntity player) { runF(player); return true;  }

@Override
public void onStart(WorldRenderContext context) { runF(context);  }

@Override
public void afterSetup(WorldRenderContext context) { runF(context);  }

@Override
public void beforeEntities(WorldRenderContext context) { runF(context);  }

@Override
public void afterEntities(WorldRenderContext context) { runF(context);  }

@Override
public boolean beforeBlockOutline(WorldRenderContext context, HitResult hitResult) { runF(context, hitResult); return true;  }

@Override
public boolean onBlockOutline(WorldRenderContext worldRenderContext, BlockOutlineContext blockOutlineContext) { runF(worldRenderContext, blockOutlineContext); return true;  }

@Override
public void beforeDebugRender(WorldRenderContext context) { runF(context);  }

@Override
public void afterTranslucent(WorldRenderContext context) { runF(context);  }

@Override
public void onLast(WorldRenderContext context) { runF(context);  }

@Override
public void onEnd(WorldRenderContext context) { runF(context);  }

@Override
public void beforeInit(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) { runF(client, screen, scaledWidth, scaledHeight);  }

@Override
public void afterInit(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) { runF(client, screen, scaledWidth, scaledHeight);  }

@Override
public void onRemove(Screen screen) { runF(screen);  }

@Override
public void beforeRender(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float tickDelta) { runF(screen, drawContext, mouseX, mouseY, tickDelta);  }

@Override
public void afterRender(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float tickDelta) { runF(screen, drawContext, mouseX, mouseY, tickDelta);  }

@Override
public void beforeTick(Screen screen) { runF(screen);  }

@Override
public void afterTick(Screen screen) { runF(screen);  }

@Override
public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) { runF(world, entity, killedEntity);  }

@Override
public void afterChangeWorld(Entity originalEntity, Entity newEntity, ServerWorld origin, ServerWorld destination) { runF(originalEntity, newEntity, origin, destination);  }

@Override
public void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) { runF(player, origin, destination);  }

@Override
public boolean allowElytraFlight(LivingEntity entity) { runF(entity); return true;  }

@Override
public boolean useCustomElytra(LivingEntity entity, boolean tickElytra) { runF(entity, tickElytra); return true;  }

@Override
public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) { runF(oldPlayer, newPlayer, alive);  }

@Override
public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) { runF(oldPlayer, newPlayer, alive);  }

@Override
public boolean allowDeath(ServerPlayerEntity player, DamageSource damageSource, float damageAmount) { runF(player, damageSource, damageAmount); return true;  }

@Override
public 		PlayerEntity.SleepFailureReason allowSleep(PlayerEntity player, BlockPos sleepingPos) { runF(player, sleepingPos); return null;  }

@Override
public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) { runF(entity, sleepingPos);  }

@Override
public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos) { runF(entity, sleepingPos);  }

@Override
public ActionResult allowBed(LivingEntity entity, BlockPos sleepingPos, BlockState state, boolean vanillaResult) { runF(entity, sleepingPos, state, vanillaResult); return null;  }

@Override
public ActionResult allowSleepTime(PlayerEntity player, BlockPos sleepingPos, boolean vanillaResult) { runF(player, sleepingPos, vanillaResult); return null;  }

@Override
public ActionResult allowNearbyMonsters(PlayerEntity player, BlockPos sleepingPos, boolean vanillaResult) { runF(player, sleepingPos, vanillaResult); return null;  }

@Override
public boolean allowResettingTime(PlayerEntity player) { runF(player); return true;  }

@Override
public 		Direction modifySleepDirection(LivingEntity entity, BlockPos sleepingPos, Direction sleepingDirection) { runF(entity, sleepingPos, sleepingDirection); return null;  }

@Override
public boolean allowSettingSpawn(PlayerEntity player, BlockPos sleepingPos) { runF(player, sleepingPos); return true;  }

@Override
public boolean setBedOccupationState(LivingEntity entity, BlockPos sleepingPos, BlockState bedState, boolean occupied) { runF(entity, sleepingPos, bedState, occupied); return true;  }

@Override
public 		Vec3d modifyWakeUpPosition(LivingEntity entity, BlockPos sleepingPos, BlockState bedState, Vec3d wakeUpPos) { runF(entity, sleepingPos, bedState, wakeUpPos); return null;  }

@Override
public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) { runF(entity, source, amount); return true;  }

@Override
public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) { runF(entity, source, baseDamageTaken, damageTaken, blocked);  }

@Override
public boolean allowDeath(LivingEntity entity, DamageSource damageSource, float damageAmount) { runF(entity, damageSource, damageAmount); return true;  }

@Override
public void afterDeath(LivingEntity entity, DamageSource damageSource) { runF(entity, damageSource);  }

@Override
public void onConversion(MobEntity previous, MobEntity converted, boolean keepEquipment) { runF(previous, converted, keepEquipment);  }
}
