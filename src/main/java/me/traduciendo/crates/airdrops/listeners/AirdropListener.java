package me.traduciendo.crates.airdrops.listeners;

import com.google.common.collect.Lists;
import lombok.Setter;
import me.traduciendo.crates.Crates;
import me.traduciendo.crates.airdrops.rewards.Airdrop;
import me.traduciendo.crates.airdrops.rewards.Reward;
import me.traduciendo.crates.airdrops.menus.AirdropEditMenu;
import me.traduciendo.crates.airdrops.menus.HologramMenu;
import me.traduciendo.crates.airdrops.menus.RewardsInventory;
import me.traduciendo.crates.airdrops.util.chat.CC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AirdropListener implements Listener {

    @Setter
    public static String airdropname;
    @Setter
    public static int slot=0;


    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event){
        if(event.getPlayer()==null){
            return;
        }
        Player player = event.getPlayer();

        if(player.hasMetadata("editdisplayname")){
            event.setCancelled(true);
            String text = event.getMessage();
            Airdrop airdrop = Airdrop.getByName(airdropname);

            airdrop.setDisplayname(text);


            player.removeMetadata("editdisplayname", Crates.getInstance());

            (new BukkitRunnable() {
                public void run() {
                    new AirdropEditMenu(airdrop).openMenu(player);
                }
            }).runTaskLater(Crates.getInstance(), 5L);
            player.sendMessage(CC.translate("&aChanged the name for "+airdrop.getName()));
            return;
        }
        if(player.hasMetadata("editlore")){
            event.setCancelled(true);
            String text = event.getMessage();
            Airdrop airdrop = Airdrop.getByName(airdropname);
            if(text.startsWith("cancel")){
                player.removeMetadata("editlore", Crates.getInstance());
                (new BukkitRunnable() {
                    public void run() {
                        new AirdropEditMenu(airdrop).openMenu(player);
                    }
                }).runTaskLater(Crates.getInstance(), 5L);
                return;
            }

            List<String> lore = Lists.newArrayList();
            if(airdrop.getLore()!=null){
                lore = airdrop.getLore();
            }

            if(text.startsWith("add ")){
                String[] split = text.split(" ");
                String texToInput = "";

                for (int i = 1 ; i<split.length ; i++){
                    texToInput += split[i]+" ";

                }
                texToInput.trim();
                lore.add(texToInput);
                sendLoreText(player, lore);
                return;

            }

            if(text.startsWith("remove ")){
                String[] split = text.split("[ ]",0);
                lore.remove(Integer.parseInt(split[1]));
                sendLoreText(player, lore);

            }
            if(text.startsWith("list")){
                sendLoreText(player, lore);
            }




            airdrop.setLore(lore);

            return;
        }
        if(player.hasMetadata("editchance")){
            String text = event.getMessage();
            event.setCancelled(true);
            if(isNumeric(text)){
                Airdrop airdrop = Airdrop.getByName(airdropname);
                List<Reward> rewardslist = airdrop.getRewards();
                rewardslist.get(slot).setChance(Integer.parseInt(text));
                airdrop.setRewards(rewardslist);

                player.removeMetadata("editchance",Crates.getInstance());
                (new BukkitRunnable() {
                    public void run() {
                        new RewardsInventory(airdrop).openMenu(player);
                    }
                }).runTaskLater(Crates.getInstance(), 5L);

                player.sendMessage(CC.translate("&aChanged the chance of this reward to "+rewardslist.get(slot).getChance()+"%"));
                return;
            }
            player.sendMessage(CC.RED+"Please input a number");
            return;
        }
        if(player.hasMetadata("editdestroytime")){
            String text = event.getMessage();
            event.setCancelled(true);
            Airdrop airdrop = Airdrop.getByName(airdropname);
            if(text.contains("cancel")){
                player.removeMetadata("editdestroytime",Crates.getInstance());
                (new BukkitRunnable() {
                    public void run() {
                       new AirdropEditMenu(airdrop).openMenu(player);
                    }
                }).runTaskLater(Crates.getInstance(), 5L);
                return;
            }
            if(isNumeric(text)){
                airdrop.setDestroytime(Integer.parseInt(text));
                player.removeMetadata("editdestroytime",Crates.getInstance());
                (new BukkitRunnable() {
                    public void run() {
                        new AirdropEditMenu(airdrop).openMenu(player);
                    }
                }).runTaskLater(Crates.getInstance(), 5L);

                player.sendMessage(CC.translate("&aChanged the destroy time to "+text));
                return;
            }
            player.sendMessage(CC.RED+"Please input a number");
            return;
        }

        if(player.hasMetadata("editmainline")){
            String text = event.getMessage();
            event.setCancelled(true);
            Airdrop airdrop = Airdrop.getByName(airdropname);
            airdrop.setMainHoloLine(text);
            (new BukkitRunnable() {
                public void run() {
                    new HologramMenu(airdrop).openMenu(player);
                }
            }).runTaskLater(Crates.getInstance(), 5L);
            player.removeMetadata("editmainline",Crates.getInstance());

        }
        if(player.hasMetadata("editsecondline")){
            String text = event.getMessage();
            event.setCancelled(true);
            Airdrop airdrop = Airdrop.getByName(airdropname);
            airdrop.setSecondHoloLine(text);
            (new BukkitRunnable() {
                public void run() {
                    new HologramMenu(airdrop).openMenu(player);
                }
            }).runTaskLater(Crates.getInstance(), 5L);
            player.removeMetadata("editsecondline",Crates.getInstance());

        }
    }

    public static final List<Location> airdropLocation = Lists.newArrayList();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {

        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location2 = block.getLocation();
        if (airdropLocation.contains(location2)) {
            player.sendMessage(ChatColor.RED + "You can't place blocks here because an airdrop is falling!");
            event.setCancelled(true);
            return;
        }
        if(Airdrop.getByItem(player.getItemInHand())==null){
            return;
        }
        event.setCancelled(true);
        World world = block.getWorld();
        Location temp = location2.clone();
        for (int index = 0; index < 16; index++) {
            if (!world.getBlockAt(temp.add(0.0D, 1.0D, 0.0D)).getType().equals(Material.AIR)) {
                player.sendMessage(ChatColor.RED + "You can't place the airdrop here.");
                return;
            }
        }
        Location check = event.getBlock().getLocation().clone().add(0.0D, -1.0D, 0.0D);
        if (world.getBlockAt(check).getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + "You can't place the airdrop here.");
            return;
        }
        if (player.getItemInHand().getAmount() <= 1) {
            player.getInventory().remove(player.getItemInHand());
        } else {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }
        player.updateInventory();
        airdropLocation.add(location2);
        Crates.getInstance().getAirdropManager().place(player,location2,Airdrop.getByItem(player.getItemInHand()));

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fYou have placed a &dAirdrop&f."));
    }



    public static void sendLoreUsage(Player player){
        player.sendMessage(CC.translate("&3&m=============================="));
        player.sendMessage(CC.translate("&b&lLore Help &7(1/1)"));
        player.sendMessage(" ");
        player.sendMessage(CC.translate("&a● &fadd <text>"));
        player.sendMessage(CC.translate("&a● &fremove <line-number>"));
        player.sendMessage(CC.translate("&a● &flist"));
        player.sendMessage(" ");
        player.sendMessage(CC.translate("&fTo exit this process type &ccancel &fin the chat"));
        player.sendMessage(CC.translate("&3&m=============================="));
        player.sendMessage(" ");
        player.sendMessage(" ");
    }
    public static void sendLoreText(Player player,List<String> lore){
        player.sendMessage(CC.GRAY+CC.STRIKE_THROUGH+"--------------------------");
        player.sendMessage(CC.translate("&b&lLore:"));
        player.sendMessage(" ");
        for (int i = 0 ; i<lore.size();i++){
            String loreline = lore.get(i);

            player.sendMessage(CC.translate("&bLine "+i+": &f"+loreline));
        }
        player.sendMessage(" ");
        player.sendMessage(CC.GRAY+CC.STRIKE_THROUGH+"--------------------------");
        player.sendMessage(" ");
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
