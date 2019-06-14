package io.obadiah.lb.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private static final String LINE_SEPARATOR = "\n";

    private Map<Enchantment, Integer> enchants = Maps.newHashMap();
    private int amount;
    private short data;
    private List<String> lore = Lists.newArrayList();
    private Material mat;
    private String title;
    private boolean unbreakable;
    private Color color;

    private List<PotionEffect> potionEffects;

    public ItemBuilder(ItemBuilder builder) {
        this.enchants = Maps.newHashMap(builder.enchants);
        this.amount = builder.amount;
        this.data = builder.data;
        this.lore = Lists.newArrayList(builder.lore);
        this.mat = builder.mat;
        this.title = builder.title;
        this.unbreakable = builder.unbreakable;
        this.color = builder.color;
    }

    public ItemBuilder(ItemStack stack) {
        this.mat = stack.getType();
        this.amount = stack.getAmount();
        this.enchants.putAll(stack.getEnchantments());
        this.data = stack.getDurability();
        this.potionEffects = Lists.newArrayList();

        if (!stack.hasItemMeta()) {
            return;
        }

        this.title = stack.getItemMeta().getDisplayName();

        if (stack.getItemMeta() instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) stack.getItemMeta();
            this.potionEffects = Lists.newArrayList(meta.getCustomEffects());
        }

        if (stack.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
        }

        if (stack.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            this.color = meta.getColor();
        }

        if (!stack.getItemMeta().hasLore()) {
            return;
        }

        this.lore = stack.getItemMeta().getLore();
    }

    public ItemBuilder(Material mat) {
        this(mat, 1, (short) 0);
    }

    public ItemBuilder(Material mat, int amount, short data) {
        this.mat = mat;
        this.amount = amount;
        this.data = data;
        this.potionEffects = Lists.newArrayList();
    }

    public ItemBuilder setType(Material mat) {
        this.mat = mat;
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        if (lore.length == 0) {
            this.lore = null;
            return this;
        }

        this.lore.clear();
        List<String> loreList = Lists.newArrayList();
        for (String loreLine : lore) {
            String[] lines = loreLine.split(LINE_SEPARATOR);
            if (lines.length == 1) {
                loreList.add(lines[0]);
                continue;
            }

            String formatting = ChatColor.getLastColors(lines[0]);
            for (int i = 0; i < lines.length; i++) {
                if (i == 0) {
                    loreList.add(lines[i]);
                    continue;
                }

                loreList.add(formatting + lines[i]);
            }
        }

        this.lore.addAll(loreList);
        return this;
    }

    public ItemBuilder addLoreLines(String... strings) {
        return this.addLoreLines(Lists.newArrayList(strings));
    }

    public ItemBuilder addColoredLoreLines(String color, String... strings) {
        return this.addLoreLines(color, Lists.newArrayList(strings));
    }

    public ItemBuilder addLoreLines(String color, List<String> list) {
        if (list == null) {
            return this;
        }

        List<String> loreList = Lists.newArrayList();
        for (String loreLine : list) {
            String[] lines = loreLine.split(LINE_SEPARATOR);
            if (lines.length == 1) {
                loreList.add(color == null ? lines[0] : color + lines[0]);
                continue;
            }

            String formatting = color == null ? ChatColor.getLastColors(lines[0]) : color;
            for (int i = 0; i < lines.length; i++) {
                if (i == 0) {
                    loreList.add(color == null ? lines[i] : color + lines[i]);
                    continue;
                }

                loreList.add(formatting + lines[i]);
            }
        }

        this.lore.addAll(loreList);
        return this;
    }

    public ItemBuilder addLoreLines(List<String> list) {
        return this.addLoreLines(null, list);
    }

    public ItemBuilder addTopLore(List<String> list) {
        if (list == null) {
            return this;
        }

        if (this.lore == null) {
            this.lore = list;
            return this;
        }

        list.addAll(this.lore);
        this.lore = list;
        return this;
    }

    public ItemBuilder setWoolColor(DyeColor color) {
        return this.setData(color.getWoolData());
    }

    public ItemBuilder setPaneColor(DyeColor color) {
        return this.setData(color.getWoolData());
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = item.getItemMeta();

        if (meta instanceof PotionMeta && !this.potionEffects.isEmpty()) {
            PotionMeta potionMeta = (PotionMeta) meta;
            for (PotionEffect effect : this.potionEffects) {
                potionMeta.addCustomEffect(effect, false);
            }
        }

        if (this.title != null) {
            meta.setDisplayName(this.title);
        }

        if (this.lore != null && !this.lore.isEmpty()) {
            meta.setLore(this.lore);
        }

        if (color != null && meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(this.color);
        }

        meta.spigot().setUnbreakable(this.unbreakable);
        item.setItemMeta(meta);
        item.addUnsafeEnchantments(this.enchants);
        return item;
    }

    public Material getType() {
        return this.mat;
    }

    public ItemBuilder addLore(String string) {
        this.addLoreLines(Lists.newArrayList(string));
        return this;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return this.enchants;
    }

    public int getAmount() {
        return this.amount;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public short getData() {
        return this.data;
    }

    public ItemBuilder setData(short newData) {
        this.data = newData;
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore == null) {
            return this;
        }

        List<String> loreList = Lists.newArrayList();
        for (String loreLine : lore) {
            String[] lines = loreLine.split(LINE_SEPARATOR);
            if (lines.length == 1) {
                loreList.add(lines[0]);
                continue;
            }

            String formatting = ChatColor.getLastColors(lines[0]);
            for (int i = 0; i < lines.length; i++) {
                if (i == 0) {
                    loreList.add(lines[i]);
                    continue;
                }

                loreList.add(formatting + lines[i]);
            }
        }

        this.lore = loreList;
        return this;
    }

    public Material getMat() {
        return this.mat;
    }

    public String getTitle() {
        return this.title;
    }

    public ItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    public ItemBuilder setUnbreakable(boolean value) {
        this.unbreakable = value;
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public ItemBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public List<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public ItemBuilder setPotionEffects(List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
        return this;
    }
}
