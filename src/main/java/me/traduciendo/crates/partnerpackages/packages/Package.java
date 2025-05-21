package me.traduciendo.crates.partnerpackages.packages;

import com.google.common.collect.Lists;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Package {
   private String name;
   private String displayName;
   private List<String> lore;
   private int prizeAmount;
   private Material material;
   private int data;
   private ItemStack[] items;

   public List<String> getNormalLore() {
      return this.lore;
   }

   public List<String> getLore() {
      List<String> lore = Lists.newArrayList();
      this.lore.forEach((lines) -> {
         lore.add(lines);
         if (lines.contains("%REWARDS%")) {
            lore.addAll(this.getRewards());
         }

      });
      return (List)lore.stream().map((key) -> {
         return key.replace("%REWARDS%", "");
      }).collect(Collectors.toList());
   }

   public List<String> getRewards() {
      List<String> rewards = Lists.newArrayList();
      ItemStack[] var2 = (ItemStack[])this.getItems().clone();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack itemStack = var2[var4];
         if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
            String displayName = itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : ChatUtil.toReadable(itemStack.getType());
            int amount = itemStack.getAmount();
            rewards.add(" &fx" + amount + " " + displayName);
         }
      }

      return rewards;
   }

   public ItemStack getPackageItem(int amount) {
      return (new ItemBuilder(this.getMaterial())).amount(amount).data(this.getData()).name(this.getDisplayName()).lore(this.getLore()).build();
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public int getPrizeAmount() {
      return this.prizeAmount;
   }

   public Material getMaterial() {
      return this.material;
   }

   public int getData() {
      return this.data;
   }

   public ItemStack[] getItems() {
      return this.items;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public void setLore(List<String> lore) {
      this.lore = lore;
   }

   public void setPrizeAmount(int prizeAmount) {
      this.prizeAmount = prizeAmount;
   }

   public void setMaterial(Material material) {
      this.material = material;
   }

   public void setData(int data) {
      this.data = data;
   }

   public void setItems(ItemStack[] items) {
      this.items = items;
   }

   @ConstructorProperties({"name", "displayName", "lore", "prizeAmount", "material", "data", "items"})
   public Package(String name, String displayName, List<String> lore, int prizeAmount, Material material, int data, ItemStack[] items) {
      this.name = name;
      this.displayName = displayName;
      this.lore = lore;
      this.prizeAmount = prizeAmount;
      this.material = material;
      this.data = data;
      this.items = items;
   }
}
