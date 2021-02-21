package me.neznamy.tab.platforms.bukkit.nms.datawatcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import me.neznamy.tab.platforms.bukkit.nms.PacketPlayOut;
import me.neznamy.tab.shared.ProtocolVersion;

public class DataWatcherItem {

	//required NMS constructor
	private static Constructor<?> newDataWatcherItem;
	
	//type of value (position + data type)
	public DataWatcherObject type;
	
	//actual data value
	public Object value;

	/**
	 * Initializes required NMS classes and fields
	 * @throws Exception - if something fails
	 */
	public static void initializeClass() throws Exception {
		Class<?> DataWatcherItem;
		DataWatcherItem = PacketPlayOut.getNMSClass("WatchableObject");
		newDataWatcherItem = DataWatcherItem.getConstructors()[0];
	}
	
	/**
	 * Constructs new instance of the object with given parameters
	 * @param type - value type
	 * @param value - value
	 */
	public DataWatcherItem(DataWatcherObject type, Object value){
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns NMS version of this class
	 * @return NMS version of this class
	 * @throws Exception - if something fails
	 */
	public Object toNMS() throws Exception {
		if (ProtocolVersion.SERVER_VERSION.getMinorVersion() >= 9) {
			return newDataWatcherItem.newInstance(type.toNMS(), value);
		} else {
			return newDataWatcherItem.newInstance(type.classType, type.position, value);
		}
	}
	
	/**
	 * Returns and instance of this class from given NMS item
	 * @param nmsItem - NMS item
	 * @return instance of this class with same data
	 * @throws Exception - if something fails
	 */
	public static DataWatcherItem fromNMS(Object nmsItem) throws Exception {
		Object classType = getValue(nmsItem, "field_75678_a");
		int position = (int) getValue(nmsItem, "field_75676_b");
		Object value = getValue(nmsItem, "field_75677_c");
		return new DataWatcherItem(new DataWatcherObject(position, classType), value);
	}
	
	/**
	 * Returns value of a field
	 * @param obj - object to get value from
	 * @param field - name of field to get
	 * @return value of field
	 * @throws Exception - if something fails
	 */
	public static Object getValue(Object obj, String field) throws Exception {
		Field f = obj.getClass().getDeclaredField(field);
		f.setAccessible(true);
		return f.get(obj);
	}
}