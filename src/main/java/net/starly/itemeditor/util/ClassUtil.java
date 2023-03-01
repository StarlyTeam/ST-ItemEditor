package net.starly.itemeditor.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class ClassUtil {
    private static ClassUtil instance;
    private Class<?> nmsStackClass, craftStackClass, nbtTagCompoundClass;
    private Method asNMSCopy, asBukkitCopy, getTag, setTag, setString, getString, remove, getKeys;

    private ClassUtil() {
        String nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
        nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);

        try {
            try {
                this.nmsStackClass = Class.forName("net.minecraft.server." + nmsVersion + ".ItemStack");
            } catch (ClassNotFoundException ex) {
                this.nmsStackClass = Class.forName("net.minecraft.world.item.ItemStack");
            }

            try {
                this.craftStackClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".inventory.CraftItemStack");
            } catch (ClassNotFoundException ex) {
                this.craftStackClass = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
            }

            try {
                this.nbtTagCompoundClass = Class.forName("net.minecraft.server." + nmsVersion + ".NBTTagCompound");
            } catch (ClassNotFoundException ex) {
                this.nbtTagCompoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
            }


            asNMSCopy = craftStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
            asBukkitCopy = craftStackClass.getDeclaredMethod("asBukkitCopy", nmsStackClass);

            try {
                getTag = nmsStackClass.getDeclaredMethod("getTag");
            } catch (NoSuchMethodException ex) {
                getTag = nmsStackClass.getDeclaredMethod("u");
            }

            try {
                setTag = nmsStackClass.getDeclaredMethod("setTag", nbtTagCompoundClass);
            } catch (NoSuchMethodException ex) {
                setTag = nmsStackClass.getDeclaredMethod("c", nbtTagCompoundClass);
            }

            try {
                setString = nbtTagCompoundClass.getDeclaredMethod("setString", String.class, String.class);
            } catch (NoSuchMethodException ex) {
                setString = nbtTagCompoundClass.getDeclaredMethod("a", String.class, String.class);
            }

            try {
                getString = nbtTagCompoundClass.getDeclaredMethod("getString", String.class);
            } catch (NoSuchMethodException ex) {
                getString = nbtTagCompoundClass.getDeclaredMethod("l", String.class);
            }

            try {
                remove = nbtTagCompoundClass.getDeclaredMethod("remove", String.class);
            } catch (NoSuchMethodException ex) {
                remove = nbtTagCompoundClass.getDeclaredMethod("r", String.class);
            }

            try {
                getKeys = nbtTagCompoundClass.getDeclaredMethod("getAllKeys");
            } catch (NoSuchMethodException ex) {
                getKeys = nbtTagCompoundClass.getDeclaredMethod("e");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ClassUtil getInstance() {
        if (instance == null) {
            synchronized (ClassUtil.class) {
                instance = new ClassUtil();
            }
        }

        return instance;
    }

    public Class<?> getNmsStackClass() {
        return nmsStackClass;
    }

    public Class<?> getCraftStackClass() {
        return craftStackClass;
    }

    public Class<?> getNbtTagCompoundClass() {
        return nbtTagCompoundClass;
    }

    public Method getAsNMSCopy() {
        return asNMSCopy;
    }

    public Method getAsBukkitCopy() {
        return asBukkitCopy;
    }

    public Method getGetTag() {
        return getTag;
    }

    public Method getSetTag() {
        return setTag;
    }

    public Method getSetString() {
        return setString;
    }

    public Method getGetString() {
        return getString;
    }

    public Method getRemove() {
        return remove;
    }

    public Method getGetKeys() {
        return getKeys;
    }
}
