package me.neznamy.tab.platforms.bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.neznamy.tab.platforms.bukkit.nms.NMSHook;
import me.neznamy.tab.platforms.bukkit.nms.PacketPlayOutEntityDestroy;
import me.neznamy.tab.platforms.bukkit.nms.PacketPlayOutEntityMetadata;
import me.neznamy.tab.platforms.bukkit.nms.PacketPlayOutSpawnEntityLiving;
import me.neznamy.tab.platforms.bukkit.nms.datawatcher.DataWatcher;
import me.neznamy.tab.shared.ProtocolVersion;
import me.neznamy.tab.shared.Shared;
import me.neznamy.tab.shared.features.bossbar.BossBar;
import me.neznamy.tab.shared.packets.EnumChatFormat;
import me.neznamy.tab.shared.packets.IChatBaseComponent;
import me.neznamy.tab.shared.packets.PacketBuilder;
import me.neznamy.tab.shared.packets.PacketPlayOutBoss;
import me.neznamy.tab.shared.packets.PacketPlayOutChat;
import me.neznamy.tab.shared.packets.PacketPlayOutPlayerInfo;
import me.neznamy.tab.shared.packets.PacketPlayOutPlayerListHeaderFooter;
import me.neznamy.tab.shared.packets.PacketPlayOutScoreboardDisplayObjective;
import me.neznamy.tab.shared.packets.PacketPlayOutScoreboardObjective;
import me.neznamy.tab.shared.packets.PacketPlayOutScoreboardObjective.EnumScoreboardHealthDisplay;
import me.neznamy.tab.shared.packets.PacketPlayOutScoreboardScore;
import me.neznamy.tab.shared.packets.PacketPlayOutScoreboardTeam;
import me.neznamy.tab.shared.packets.PacketPlayOutTitle;
import us.myles.ViaVersion.api.type.Type;
import us.myles.viaversion.libs.gson.JsonParser;
import me.neznamy.tab.shared.packets.PacketPlayOutBoss.Action;
import me.neznamy.tab.shared.packets.PacketPlayOutPlayerInfo.EnumGamemode;
import me.neznamy.tab.shared.packets.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import me.neznamy.tab.shared.packets.PacketPlayOutPlayerInfo.PlayerInfoData;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BukkitPacketBuilder implements PacketBuilder {

	private static String serverPackage;
	private static int minorVersion;

	private static Class<Enum> EnumChatFormat_;

	//PacketPlayOutBoss
	private static Class<?> PacketPlayOutBoss;
	private static Class<?> BarColor;
	private static Class<?> BarStyle;
	private static Class<Enum> PacketPlayOutBoss_Action;
	private static Constructor<?> newPacketPlayOutBoss;
	private static Field PacketPlayOutBoss_UUID;
	private static Field PacketPlayOutBoss_ACTION;
	private static Field PacketPlayOutBoss_NAME;
	private static Field PacketPlayOutBoss_PROGRESS;
	private static Field PacketPlayOutBoss_COLOR;
	private static Field PacketPlayOutBoss_STYLE;
	private static Field PacketPlayOutBoss_DARKEN_SKY;
	private static Field PacketPlayOutBoss_PLAY_MUSIC;
	private static Field PacketPlayOutBoss_CREATE_FOG;

	//PacketPlayOutChat
	private static Class<?> PacketPlayOutChat;
	private static Class<?> ChatMessageType;
	private static Constructor<?> newPacketPlayOutChat;
	private static Field PacketPlayOutChat_MESSAGE;
	private static Field PacketPlayOutChat_POSITION;
	private static Field PacketPlayOutChat_SENDER;

	//PacketPlayOutPlayerListHeaderFooter
	private static Class<?> PacketPlayOutPlayerListHeaderFooter;
	private static Constructor<?> newPacketPlayOutPlayerListHeaderFooter;
	private static Field PacketPlayOutPlayerListHeaderFooter_HEADER;
	private static Field PacketPlayOutPlayerListHeaderFooter_FOOTER;

	//PacketPlayOutScoreboardDisplayObjective
	private static Class<?> PacketPlayOutScoreboardDisplayObjective;
	private static Constructor<?> newPacketPlayOutScoreboardDisplayObjective;
	private static Field PacketPlayOutScoreboardDisplayObjective_POSITION;
	private static Field PacketPlayOutScoreboardDisplayObjective_OBJECTIVENAME;

	//PacketPlayOutScoreboardObjective
	private static Class<?> PacketPlayOutScoreboardObjective_;
	private static Class<Enum> EnumScoreboardHealthDisplay_;
	private static Constructor<?> newPacketPlayOutScoreboardObjective;
	private static Field PacketPlayOutScoreboardObjective_OBJECTIVENAME;
	private static Field PacketPlayOutScoreboardObjective_DISPLAYNAME;
	private static Field PacketPlayOutScoreboardObjective_RENDERTYPE;
	private static Field PacketPlayOutScoreboardObjective_METHOD;

	//PacketPlayOutScoreboardScore
	private static Class<?> PacketPlayOutScoreboardScore_;
	private static Class<Enum> EnumScoreboardAction;
	private static Constructor<?> newPacketPlayOutScoreboardScore0;
	private static Constructor<?> newPacketPlayOutScoreboardScore_String;
	private static Constructor<?> newPacketPlayOutScoreboardScore_1_13;
	private static Field PacketPlayOutScoreboardScore_PLAYER;
	private static Field PacketPlayOutScoreboardScore_OBJECTIVENAME;
	private static Field PacketPlayOutScoreboardScore_SCORE;
	private static Field PacketPlayOutScoreboardScore_ACTION;

	//PacketPlayOutScoreboardTeam
	public static Class<?> PacketPlayOutScoreboardTeam;
	private static Constructor<?> newPacketPlayOutScoreboardTeam;
	private static Field PacketPlayOutScoreboardTeam_NAME;
	private static Field PacketPlayOutScoreboardTeam_DISPLAYNAME;
	private static Field PacketPlayOutScoreboardTeam_PREFIX;
	private static Field PacketPlayOutScoreboardTeam_SUFFIX;
	private static Field PacketPlayOutScoreboardTeam_VISIBILITY; //1.8+
	private static Field PacketPlayOutScoreboardTeam_CHATFORMAT; //1.13+
	private static Field PacketPlayOutScoreboardTeam_COLLISION; //1.9+
	public static Field PacketPlayOutScoreboardTeam_PLAYERS;
	private static Field PacketPlayOutScoreboardTeam_ACTION;
	public static Field PacketPlayOutScoreboardTeam_SIGNATURE;

	//PacketPlayOutPlayerInfo
	public static Class<?> PacketPlayOutPlayerInfo;
	private static Class<Enum> EnumGamemode_;
	private static Class<Enum> EnumPlayerInfoAction_;
	private static Class<?> PlayerInfoData;
	private static Constructor<?> newPacketPlayOutPlayerInfo2;
	private static Constructor<?> newPlayerInfoData;

	private static Class<?> GameProfile;
	private static Constructor<?> newGameProfile;
	private static Field GameProfile_ID;
	private static Field GameProfile_NAME;
	private static Field GameProfile_PROPERTIES;
	private static Class<?> PropertyMap;
	private static Method PropertyMap_putAll;

	private static Field PacketPlayOutPlayerInfo_ACTION;
	private static Field PacketPlayOutPlayerInfo_PLAYERS;

	private static Field PlayerInfoData_PING;
	private static Field PlayerInfoData_GAMEMODE;
	private static Field PlayerInfoData_PROFILE;
	private static Field PlayerInfoData_LISTNAME;

	//PacketPlayOutTitle
	private static Class<?> PacketPlayOutTitle;
	private static Constructor<?> newPacketPlayOutTitle;
	private static Class<Enum> EnumTitleAction;


	/**
	 * Initializes required NMS classes and fields
	 * @throws Exception - if something fails
	 */
	public static void initializeClass() throws Exception {
		serverPackage = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		minorVersion = Integer.parseInt(serverPackage.split("_")[1]);
		initializeClasses();
		initializeConstructors();
		initializeFields();
		if (minorVersion >= 8) {
			for (Method m : PropertyMap.getMethods()) {
				if (m.getName().equals("putAll") && m.getParameterCount() == 1) PropertyMap_putAll = m;
			}
			if (PropertyMap_putAll == null) throw new IllegalStateException("putAll method not found");
		}
	}

	/**
	 * Initializes required NMS classes
	 * @throws Exception - if something fails
	 */
	private static void initializeClasses() throws Exception {
		EnumChatFormat_ = (Class<Enum>) getNMSClass("EnumChatFormat");
		if (minorVersion >= 7) {
			//1.7+
			PacketPlayOutChat = getNMSClass("PacketPlayOutChat");
			PacketPlayOutScoreboardDisplayObjective = getNMSClass("PacketPlayOutScoreboardDisplayObjective");
			PacketPlayOutScoreboardObjective_ = getNMSClass("PacketPlayOutScoreboardObjective");
			PacketPlayOutScoreboardScore_ = getNMSClass("PacketPlayOutScoreboardScore");
			PacketPlayOutScoreboardTeam = getNMSClass("PacketPlayOutScoreboardTeam");
		}
	}

	/**
	 * Initializes required NMS constructors
	 * @throws Exception - if something fails
	 */
	private static void initializeConstructors() throws Exception {
		newPacketPlayOutChat = PacketPlayOutChat.getConstructor();
		newPacketPlayOutScoreboardDisplayObjective = PacketPlayOutScoreboardDisplayObjective.getConstructor();
		newPacketPlayOutScoreboardObjective = PacketPlayOutScoreboardObjective_.getConstructor();
		newPacketPlayOutScoreboardTeam = PacketPlayOutScoreboardTeam.getConstructor();
		newPacketPlayOutScoreboardScore0 = PacketPlayOutScoreboardScore_.getConstructor();
		newPacketPlayOutScoreboardScore_String = PacketPlayOutScoreboardScore_.getConstructor(String.class);
	}


	/**
	 * Initializes required NMS fields
	 * @throws Exception - if something fails
	 */
	private static void initializeFields() throws Exception {
		(PacketPlayOutScoreboardDisplayObjective_POSITION = PacketPlayOutScoreboardDisplayObjective.getDeclaredField("field_149374_a")).setAccessible(true);
		(PacketPlayOutScoreboardDisplayObjective_OBJECTIVENAME = PacketPlayOutScoreboardDisplayObjective.getDeclaredField("field_149373_b")).setAccessible(true);

		(PacketPlayOutScoreboardObjective_OBJECTIVENAME = PacketPlayOutScoreboardObjective_.getDeclaredField("field_149343_a")).setAccessible(true);
		(PacketPlayOutScoreboardObjective_DISPLAYNAME = PacketPlayOutScoreboardObjective_.getDeclaredField("field_149341_b")).setAccessible(true);
		(PacketPlayOutScoreboardObjective_METHOD = PacketPlayOutScoreboardObjective_.getDeclaredField("field_149342_c")).setAccessible(true);

		(PacketPlayOutScoreboardScore_PLAYER = PacketPlayOutScoreboardScore_.getDeclaredField("field_149329_a")).setAccessible(true);
		(PacketPlayOutScoreboardScore_OBJECTIVENAME = PacketPlayOutScoreboardScore_.getDeclaredField("field_149327_b")).setAccessible(true);
		(PacketPlayOutScoreboardScore_SCORE = PacketPlayOutScoreboardScore_.getDeclaredField("field_149328_c")).setAccessible(true);
		(PacketPlayOutScoreboardScore_ACTION = PacketPlayOutScoreboardScore_.getDeclaredField("field_149326_d")).setAccessible(true);

		(PacketPlayOutScoreboardTeam_NAME = PacketPlayOutScoreboardTeam.getDeclaredField("field_149320_a")).setAccessible(true);
		(PacketPlayOutScoreboardTeam_DISPLAYNAME = PacketPlayOutScoreboardTeam.getDeclaredField("field_149318_b")).setAccessible(true);
		(PacketPlayOutScoreboardTeam_PREFIX = PacketPlayOutScoreboardTeam.getDeclaredField("field_149319_c")).setAccessible(true);
		(PacketPlayOutScoreboardTeam_SUFFIX = PacketPlayOutScoreboardTeam.getDeclaredField("field_149316_d")).setAccessible(true);

		(PacketPlayOutChat_MESSAGE = PacketPlayOutChat.getDeclaredField("field_148919_a")).setAccessible(true);
		
		
		(PacketPlayOutScoreboardTeam_PLAYERS = PacketPlayOutScoreboardTeam.getDeclaredField("field_149317_e")).setAccessible(true);
		(PacketPlayOutScoreboardTeam_ACTION = PacketPlayOutScoreboardTeam.getDeclaredField("field_149314_f")).setAccessible(true);
		(PacketPlayOutScoreboardTeam_SIGNATURE = PacketPlayOutScoreboardTeam.getDeclaredField("field_149315_g")).setAccessible(true);
	}

	@Override
	public Object build(PacketPlayOutBoss packet, ProtocolVersion clientVersion) throws Exception {
		//<1.9 client and server
		return buildBossPacketEntity(packet, clientVersion);
	}

	public Object buildBossPacket19(PacketPlayOutBoss packet, ProtocolVersion clientVersion) throws Exception {
		Object nmsPacket = newPacketPlayOutBoss.newInstance();
		PacketPlayOutBoss_UUID.set(nmsPacket, packet.id);
		PacketPlayOutBoss_ACTION.set(nmsPacket, Enum.valueOf((Class<Enum>)PacketPlayOutBoss_Action, packet.operation.toString()));
		PacketPlayOutBoss_PROGRESS.set(nmsPacket, packet.pct);
		PacketPlayOutBoss_NAME.set(nmsPacket, packet.name == null ? null :NMSHook.stringToComponent(IChatBaseComponent.optimizedComponent(packet.name).toString(clientVersion)));
		PacketPlayOutBoss_COLOR.set(nmsPacket, packet.color == null ? null : Enum.valueOf((Class<Enum>)BarColor, packet.color.toString()));
		PacketPlayOutBoss_STYLE.set(nmsPacket, packet.overlay == null ? null : Enum.valueOf((Class<Enum>)BarStyle, packet.overlay.toString()));
		PacketPlayOutBoss_DARKEN_SKY.set(nmsPacket, packet.darkenScreen);
		PacketPlayOutBoss_PLAY_MUSIC.set(nmsPacket, packet.playMusic);
		PacketPlayOutBoss_CREATE_FOG.set(nmsPacket, packet.createWorldFog);
		return nmsPacket;
	}

	public Object buildBossPacketVia(PacketPlayOutBoss packet, ProtocolVersion clientVersion) throws Exception {
		if (clientVersion == ProtocolVersion.UNKNOWN) return null; //preventing disconnect if packet ID changes and users do not update
		try {
			ByteBuf buf = Unpooled.buffer();
			Type.VAR_INT.writePrimitive(buf, clientVersion.getMinorVersion() == 15 ? 0x0D : 0x0C);
			Type.UUID.write(buf, packet.id);
			Type.VAR_INT.writePrimitive(buf, packet.operation.ordinal());
			switch (packet.operation) {
			case ADD:
				Type.COMPONENT.write(buf, JsonParser.parseString(IChatBaseComponent.optimizedComponent(packet.name).toString(clientVersion)));
				Type.FLOAT.writePrimitive(buf, packet.pct);
				Type.VAR_INT.writePrimitive(buf, packet.color.ordinal());
				Type.VAR_INT.writePrimitive(buf, packet.overlay.ordinal());
				Type.BYTE.write(buf, packet.getFlags());
				break;
			case REMOVE:
				break;
			case UPDATE_PCT:
				Type.FLOAT.writePrimitive(buf, packet.pct);
				break;
			case UPDATE_NAME:
				Type.COMPONENT.write(buf, JsonParser.parseString(IChatBaseComponent.optimizedComponent(packet.name).toString(clientVersion)));
				break;
			case UPDATE_STYLE:
				Type.VAR_INT.writePrimitive(buf, packet.color.ordinal());
				Type.VAR_INT.writePrimitive(buf, packet.overlay.ordinal());
				break;
			case UPDATE_PROPERTIES:
				Type.BYTE.write(buf, packet.getFlags());
				break;
			default:
				break;
			}
			return buf;
		} catch (Throwable t) {
			return Shared.errorManager.printError(null, "Failed to create 1.9 bossbar packet using ViaVersion v" + Bukkit.getPluginManager().getPlugin("ViaVersion").getDescription().getVersion() + ". Is it the latest version?", t);
		}
	}

	public Object buildBossPacketEntity(PacketPlayOutBoss packet, ProtocolVersion clientVersion) throws Exception {
		if (packet.operation == Action.UPDATE_STYLE) return null; //nothing to do here

		int entityId = ((BossBar)Shared.featureManager.getFeature("bossbar")).getLine(packet.id).entityId;
		if (packet.operation == Action.REMOVE) {
			return new PacketPlayOutEntityDestroy(entityId).toNMS(clientVersion);
		}
		DataWatcher w = new DataWatcher();
		if (packet.operation == Action.UPDATE_PCT || packet.operation == Action.ADD) {
			float health = (float)300*packet.pct;
			if (health == 0) health = 1;
			w.helper().setHealth(health);
		}
		if (packet.operation == Action.UPDATE_NAME || packet.operation == Action.ADD) {
			w.helper().setCustomName(packet.name, clientVersion);
		}
		if (packet.operation == Action.ADD) {
			w.helper().setEntityFlags((byte) 32);
			return new PacketPlayOutSpawnEntityLiving(entityId, null, EntityType.WITHER, new Location(null, 0,0,0), w).toNMS(clientVersion);
		} else {
			return new PacketPlayOutEntityMetadata(entityId, w).toNMS(clientVersion);
		}
	}

	@Override
	public Object build(PacketPlayOutChat packet, ProtocolVersion clientVersion) throws Exception {
		Object nmsPacket = newPacketPlayOutChat.newInstance();
		if (minorVersion >= 7) {
			PacketPlayOutChat_MESSAGE.set(nmsPacket, NMSHook.stringToComponent(packet.message.toString(clientVersion)));
		}
		return nmsPacket;
	}

	@Override
	public Object build(PacketPlayOutPlayerInfo packet, ProtocolVersion clientVersion) throws Exception {
		if (minorVersion < 8) return null;
		Object nmsPacket = newPacketPlayOutPlayerInfo2.newInstance(Enum.valueOf(EnumPlayerInfoAction_, packet.action.toString()), Collections.EMPTY_LIST);
		List<Object> items = new ArrayList<Object>();
		for (PlayerInfoData data : packet.entries) {
			Object profile = newGameProfile.newInstance(data.uniqueId, data.name);
			if (data.skin != null) PropertyMap_putAll.invoke(GameProfile_PROPERTIES.get(profile), data.skin);
			if (newPlayerInfoData.getParameterCount() == 5) {
				items.add(newPlayerInfoData.newInstance(newPacketPlayOutPlayerInfo2.newInstance(null, Collections.EMPTY_LIST), profile, data.latency, data.gameMode == null ? null : Enum.valueOf(EnumGamemode_, data.gameMode.toString()), 
						data.displayName == null ? null : NMSHook.stringToComponent(data.displayName.toString(clientVersion))));
			} else {
				//1.8.8 paper
				items.add(newPlayerInfoData.newInstance(profile, data.latency, data.gameMode == null ? null : Enum.valueOf(EnumGamemode_, data.gameMode.toString()), 
						data.displayName == null ? null : NMSHook.stringToComponent(data.displayName.toString(clientVersion))));
			}
		}
		PacketPlayOutPlayerInfo_PLAYERS.set(nmsPacket, items);
		return nmsPacket;
	}

	@Override
	public Object build(PacketPlayOutPlayerListHeaderFooter packet, ProtocolVersion clientVersion) throws Exception {
		Object nmsPacket = newPacketPlayOutPlayerListHeaderFooter.newInstance();
		PacketPlayOutPlayerListHeaderFooter_HEADER.set(nmsPacket, NMSHook.stringToComponent(packet.header.toString(clientVersion, true)));
		PacketPlayOutPlayerListHeaderFooter_FOOTER.set(nmsPacket, NMSHook.stringToComponent(packet.footer.toString(clientVersion, true)));
		return nmsPacket;
	}

	@Override
	public Object build(PacketPlayOutScoreboardDisplayObjective packet, ProtocolVersion clientVersion) throws Exception {
		Object nmsPacket = newPacketPlayOutScoreboardDisplayObjective.newInstance();
		PacketPlayOutScoreboardDisplayObjective_POSITION.set(nmsPacket, packet.slot);
		PacketPlayOutScoreboardDisplayObjective_OBJECTIVENAME.set(nmsPacket, packet.objectiveName);
		return nmsPacket;
	}

	@Override
	public Object build(PacketPlayOutScoreboardObjective packet, ProtocolVersion clientVersion) throws Exception {
		String displayName = packet.displayName;
		if (clientVersion.getMinorVersion() < 13) {
			displayName = cutTo(displayName, 32);
		}
		Object nmsPacket = newPacketPlayOutScoreboardObjective.newInstance();
		PacketPlayOutScoreboardObjective_OBJECTIVENAME.set(nmsPacket, packet.objectiveName);
		PacketPlayOutScoreboardObjective_DISPLAYNAME.set(nmsPacket, displayName);
		if (PacketPlayOutScoreboardObjective_RENDERTYPE != null && packet.renderType != null) {
			PacketPlayOutScoreboardObjective_RENDERTYPE.set(nmsPacket, packet.renderType.ordinal());
		}
		PacketPlayOutScoreboardObjective_METHOD.set(nmsPacket, packet.method);
		return nmsPacket;
	}

	@Override
	public Object build(PacketPlayOutScoreboardScore packet, ProtocolVersion clientVersion) throws Exception {
		if (packet.action == PacketPlayOutScoreboardScore.Action.REMOVE) {
			return newPacketPlayOutScoreboardScore_String.newInstance(packet.player);
		}
		Object nmsPacket = newPacketPlayOutScoreboardScore0.newInstance();
		PacketPlayOutScoreboardScore_PLAYER.set(nmsPacket, packet.player);
		PacketPlayOutScoreboardScore_OBJECTIVENAME.set(nmsPacket, packet.objectiveName);
		PacketPlayOutScoreboardScore_SCORE.set(nmsPacket, packet.score);
		PacketPlayOutScoreboardScore_ACTION.set(nmsPacket, packet.action.ordinal());
		return nmsPacket;
	}

	@Override
	public Object build(PacketPlayOutScoreboardTeam packet, ProtocolVersion clientVersion) throws Exception {
		String prefix = packet.playerPrefix;
		String suffix = packet.playerSuffix;
		prefix = cutTo(prefix, 16);
		suffix = cutTo(suffix, 16);
		Object nmsPacket = newPacketPlayOutScoreboardTeam.newInstance();
		PacketPlayOutScoreboardTeam_NAME.set(nmsPacket, packet.name);
		PacketPlayOutScoreboardTeam_DISPLAYNAME.set(nmsPacket, packet.name);
		PacketPlayOutScoreboardTeam_PREFIX.set(nmsPacket, prefix);
		PacketPlayOutScoreboardTeam_SUFFIX.set(nmsPacket, suffix);
		if (PacketPlayOutScoreboardTeam_VISIBILITY != null) PacketPlayOutScoreboardTeam_VISIBILITY.set(nmsPacket, packet.nametagVisibility);
		if (PacketPlayOutScoreboardTeam_COLLISION != null) PacketPlayOutScoreboardTeam_COLLISION.set(nmsPacket, packet.collisionRule);
		PacketPlayOutScoreboardTeam_PLAYERS.set(nmsPacket, packet.players);
		PacketPlayOutScoreboardTeam_ACTION.set(nmsPacket, packet.method);
		PacketPlayOutScoreboardTeam_SIGNATURE.set(nmsPacket, packet.options);
		return nmsPacket;
	}

	@Override
	public Object build(PacketPlayOutTitle packet, ProtocolVersion clientVersion) throws Exception {
		if (ProtocolVersion.SERVER_VERSION.getMinorVersion() < 8) return null;
		return newPacketPlayOutTitle.newInstance(Enum.valueOf(EnumTitleAction, packet.action.toString()), 
				packet.text == null ? null : NMSHook.stringToComponent(IChatBaseComponent.optimizedComponent(packet.text).toString(clientVersion)), packet.fadeIn, packet.stay, packet.fadeOut);
	}

	private static Class<?> getNMSClass(String name) throws ClassNotFoundException {
		return Main.class.getClassLoader().loadClass("net.minecraft.server." + serverPackage + "." + name);
	}

	private static List<Field> getFields(Class<?> clazz, Class<?> type){
		List<Field> list = new ArrayList<Field>();
		if (clazz == null) return list;
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getType() == type) list.add(field);
		}
		return list;
	}

	@Override
	public PacketPlayOutPlayerInfo readPlayerInfo(Object nmsPacket, ProtocolVersion clientVersion) throws Exception {
		if (minorVersion < 8) return null;
		EnumPlayerInfoAction action = EnumPlayerInfoAction.valueOf(PacketPlayOutPlayerInfo_ACTION.get(nmsPacket).toString());
		List<PlayerInfoData> listData = new ArrayList<PlayerInfoData>();
		for (Object nmsData : (List) PacketPlayOutPlayerInfo_PLAYERS.get(nmsPacket)) {
			Object nmsGamemode = PlayerInfoData_GAMEMODE.get(nmsData);
			EnumGamemode gamemode = nmsGamemode == null ? null : EnumGamemode.valueOf(nmsGamemode.toString());
			Object profile = PlayerInfoData_PROFILE.get(nmsData);
			Object nmsComponent = PlayerInfoData_LISTNAME.get(nmsData);
			IChatBaseComponent listName = IChatBaseComponent.fromString(NMSHook.componentToString(nmsComponent));
			listData.add(new PlayerInfoData((String) GameProfile_NAME.get(profile), (UUID) GameProfile_ID.get(profile), GameProfile_PROPERTIES.get(profile), PlayerInfoData_PING.getInt(nmsData), gamemode, listName));
		}
		return new PacketPlayOutPlayerInfo(action, listData);
	}

	@Override
	public PacketPlayOutScoreboardObjective readObjective(Object nmsPacket, ProtocolVersion clientVersion) throws Exception {
		String objective = (String) PacketPlayOutScoreboardObjective_OBJECTIVENAME.get(nmsPacket);
		String displayName;
		displayName = (String) PacketPlayOutScoreboardObjective_DISPLAYNAME.get(nmsPacket);
		EnumScoreboardHealthDisplay renderType = null;
		if (PacketPlayOutScoreboardObjective_RENDERTYPE != null) {
			Object nmsRender = PacketPlayOutScoreboardObjective_RENDERTYPE.get(nmsPacket);
			if (nmsRender != null) {
				renderType = EnumScoreboardHealthDisplay.values()[(int)nmsRender];
			}
		}
		int method = PacketPlayOutScoreboardObjective_METHOD.getInt(nmsPacket);
		PacketPlayOutScoreboardObjective packet = PacketPlayOutScoreboardObjective.REGISTER(objective, displayName, renderType);
		packet.method = method;
		return packet;
	}

	@Override
	public PacketPlayOutScoreboardDisplayObjective readDisplayObjective(Object nmsPacket, ProtocolVersion clientVersion) throws Exception {
		return new PacketPlayOutScoreboardDisplayObjective(
				PacketPlayOutScoreboardDisplayObjective_POSITION.getInt(nmsPacket),
				(String) PacketPlayOutScoreboardDisplayObjective_OBJECTIVENAME.get(nmsPacket)
				);
	}
}