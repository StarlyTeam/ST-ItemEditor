package net.starly.itemeditor.util;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.tank.NmsItemUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.starly.itemeditor.ItemEditorMain.msgConfig;

public class ItemEditUtil {
    private static ItemEditUtil instance;
    public static ItemEditUtil getInstance() {
        if (instance == null) instance = new ItemEditUtil(); return instance;
    }


    public ItemStack setDisplayName(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack setCustomMolelData(ItemStack itemStack, int id) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(id);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack setType(ItemStack itemStack, Material type) {
        itemStack.setType(type);
        return itemStack;
    }

    public ItemStack setLore(ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack addLore(ItemStack itemStack, String line) {
        List<String> lore = itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : new ArrayList<>();
        lore.add(line);
        return setLore(itemStack, lore);
    }

    public ItemStack editLoreAt(ItemStack itemStack, int index, String line) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lore.set(index, line);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack removeLoreAt(ItemStack itemStack, int index) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lore.remove(index);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack addEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return itemStack;
    }

    public ItemStack removeEnchantment(ItemStack itemStack, Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return itemStack;
    }

    public ItemStack clearEnchantments(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getEnchants().forEach((enchantment, level) -> itemMeta.removeEnchant(enchantment));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack addItemFlag(ItemStack itemStack, ItemFlag itemFlag) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack removeItemFlag(ItemStack itemStack, ItemFlag itemFlag) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.removeItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack clearItemFlag(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getItemFlags().forEach(itemMeta::removeItemFlags);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack setNBTTag(ItemStack itemStack, String key, String value) {
        try {
            Class<?> craftStackClass = ClassUtil.getInstance().getCraftStackClass(),
                    nbtTagCompoundClass = ClassUtil.getInstance().getNbtTagCompoundClass();
            Method asNMSCopy = ClassUtil.getInstance().getAsNMSCopy(),
                    asBukkitCopy = ClassUtil.getInstance().getAsBukkitCopy(),
                    getTag = ClassUtil.getInstance().getGetTag(),
                    setTag = ClassUtil.getInstance().getSetTag(),
                    setString = ClassUtil.getInstance().getSetString();

            Object nmsStack = asNMSCopy.invoke(craftStackClass, itemStack),
                    nbtTagCompound = getTag.invoke(nmsStack);
            if (nbtTagCompound == null) nbtTagCompound = nbtTagCompoundClass.newInstance();

            setString.invoke(nbtTagCompound, key, value);
            setTag.invoke(nmsStack, nbtTagCompound);

            return (ItemStack) asBukkitCopy.invoke(craftStackClass, nmsStack);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ItemStack removeNBTTag(ItemStack itemStack, String key) {
        try {
            Class<?> craftStackClass = ClassUtil.getInstance().getCraftStackClass(),
                    nbtTagCompoundClass = ClassUtil.getInstance().getNbtTagCompoundClass();
            Method asNMSCopy = ClassUtil.getInstance().getAsNMSCopy(),
                    asBukkitCopy = ClassUtil.getInstance().getAsBukkitCopy(),
                    getTag = ClassUtil.getInstance().getGetTag(),
                    setTag = ClassUtil.getInstance().getSetTag(),
                    remove = ClassUtil.getInstance().getRemove();

            Object nmsStack = asNMSCopy.invoke(craftStackClass, itemStack),
                    nbtTagCompound = getTag.invoke(nmsStack);
            if (nbtTagCompound == null) nbtTagCompound = nbtTagCompoundClass.newInstance();

            remove.invoke(nbtTagCompound, key);
            setTag.invoke(nmsStack, nbtTagCompound);

            return (ItemStack) asBukkitCopy.invoke(craftStackClass, nmsStack);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ItemStack clearNBTTag(ItemStack itemStack) {
        try {
            Class<?> craftStackClass = ClassUtil.getInstance().getCraftStackClass(),
                    nbtTagCompoundClass = ClassUtil.getInstance().getNbtTagCompoundClass();
            Method asNMSCopy = ClassUtil.getInstance().getAsNMSCopy(),
                    asBukkitCopy = ClassUtil.getInstance().getAsBukkitCopy(),
                    getTag = ClassUtil.getInstance().getGetTag(),
                    setTag = ClassUtil.getInstance().getSetTag(),
                    remove = ClassUtil.getInstance().getRemove(),
                    getKeys = ClassUtil.getInstance().getGetKeys();

            Object nmsStack = asNMSCopy.invoke(craftStackClass, itemStack),
                    nbtTagCompound = getTag.invoke(nmsStack);
            if (nbtTagCompound == null) nbtTagCompound = nbtTagCompoundClass.newInstance();

            Set<String> keys = (Set<String>) getKeys.invoke(nbtTagCompound);
            Object finalNbtTagCompound = nbtTagCompound;
            keys.forEach(key -> {
                try {
                    remove.invoke(finalNbtTagCompound, key);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            setTag.invoke(nmsStack, nbtTagCompound);

            return (ItemStack) asBukkitCopy.invoke(craftStackClass, nmsStack);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
