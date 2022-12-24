package net.starly.itemeditor.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class NBT {

    public static NBTTagCompound getTagCompound(net.minecraft.world.item.ItemStack item) {
        return item.hasTag() ? item.getTag() : new NBTTagCompound();
    }

    /**
     * @param objitem require ItemStack
     * @param key     require String
     * @return ItemStack
     */
    @NotNull
    public static ItemStack removeTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        item.setTag(ntc);
        item.removeTag(key);
        return CraftItemStack.asBukkitCopy(item);
    }

    @NotNull
    public static ItemStack removeAllTags(ItemStack objitem) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        for (String key : ntc.getKeys()) {
            item.removeTag(key);
        }
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return String
     */
    @NotNull
    public static String getStringTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getString(key).replace('"', ' ').trim();
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return byte
     */
    @NotNull
    public static byte getByteTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getByte(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return short
     */
    @NotNull
    public static short getShortTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getShort(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return int
     */
    @NotNull
    public static int getIntegerTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getInt(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return float
     */
    @NotNull
    public static float getFloatTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getFloat(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return double
     */
    @NotNull
    public static double getDoubleTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getDouble(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return boolean
     */
    public static boolean getBooleanTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getBoolean(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return long
     */
    @NotNull
    public static long getLongTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getLong(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return byte[]
     */
    @NotNull
    public static byte[] getByteArrayTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getByteArray(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return int[]
     */
    @NotNull
    public static int[] getIntArrayTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getIntArray(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return NBTTagCompound
     */
    @NotNull
    public static NBTTagCompound getCompoundTag(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.getCompound(key);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @return boolean
     */
    public static boolean hasTagKey(ItemStack objitem, String key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        return ntc.hasKey(key);
    }

    @Nullable
    public static Map<String, String> getAllStringTag(ItemStack objitem) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        if (ntc.getKeys().size() == 0) {
            return null;
        }
        Map<String, String> tags = new HashMap<>();
        for (String key : ntc.getKeys()) {
            tags.put(key, ntc.get(key).toString());
        }
        return tags;
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   String
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setStringTag(ItemStack objitem, String key, String value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setString(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   byte
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setByteTag(ItemStack objitem, String key, byte value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setByte(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   short
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setShortTag(ItemStack objitem, String key, short value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setShort(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   int
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setIntTag(ItemStack objitem, String key, int value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setInt(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   long
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setLongTag(ItemStack objitem, String key, long value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setLong(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   float
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setFloatTag(ItemStack objitem, String key, float value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setFloat(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   double
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setDoubleTag(ItemStack objitem, String key, double value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setDouble(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   byte[]
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setByteArrayTag(ItemStack objitem, String key, byte[] value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setByteArray(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }

    /**
     * @param objitem ItemStack
     * @param key     String
     * @param value   int[]
     * @return ItemStack
     */
    @NotNull
    public static ItemStack setIntArrayTag(ItemStack objitem, String key, int[] value) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(objitem);
        NBTTagCompound ntc = getTagCompound(item);
        ntc.setIntArray(key, value);
        item.setTag(ntc);
        return CraftItemStack.asBukkitCopy(item);
    }
}