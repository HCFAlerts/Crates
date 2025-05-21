package me.traduciendo.crates.airdrops.menus;

import com.google.common.collect.Lists;
import me.traduciendo.crates.Crates;
import me.traduciendo.crates.airdrops.rewards.Airdrop;
import me.traduciendo.crates.airdrops.listeners.AirdropListener;
import me.traduciendo.crates.airdrops.util.chat.CC;
import me.traduciendo.crates.airdrops.util.item.CompatibleMaterial;
import me.traduciendo.crates.airdrops.util.item.ItemBuilder;
import me.traduciendo.crates.airdrops.util.menu.Button;
import me.traduciendo.crates.airdrops.util.menu.Menu;
import me.traduciendo.crates.airdrops.util.menu.buttons.BackButton;
import me.traduciendo.crates.airdrops.util.menu.menus.ConfirmMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public class AirdropEditMenu extends Menu {
    private Airdrop airdrop;

    public AirdropEditMenu(Airdrop airdrop){
        this.airdrop = airdrop;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(13,new AirdropDisplayButton(airdrop));
        buttons.put(31,new IconButton(airdrop));
        buttons.put(30,new AirdropDisplayNameButton(airdrop));
        buttons.put(32,new AirdropLoreButton(airdrop));
        buttons.put(29, new AirdropRewardsButton(airdrop));
        buttons.put(45,new BackButton(new MainEditMenu()));
        buttons.put(53, new AirdropDeleteButton(airdrop));
        buttons.put(40,new AirdropDestroyTimeButton(airdrop));
        buttons.put(33,new AirdropVisualsButton(airdrop));


        return buttons;
    }


    @Override
    public String getTitle(final Player player) {
        return airdrop.getName();
    }


    @Override
    public int size(final Map<Integer, Button> buttons){
        return 9*6;
    }


    private class AirdropDisplayButton extends Button {

        private Airdrop airdrop;
        public AirdropDisplayButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(airdrop.getMaterial()).setName(airdrop.getDisplayname()).setLore(airdrop.getLore()).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {

        }

    }
    private class IconButton extends Button{
        private Airdrop airdrop;
        public IconButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            if(airdrop.getMaterial().equals(Material.DISPENSER)){
                return new ItemBuilder(Material.ITEM_FRAME).setName(CC.translate("&aChange Block")).setLore(
                        Lists.newArrayList("&8&m-----------------------------",
                                                    "&b┃ &aDISPENSER",
                                                    "&7DROPPER",
                                                    "&8&m-----------------------------"))
                        .toItemStack();
            }
                return new ItemBuilder(Material.ITEM_FRAME).setName(CC.translate("&aChange Block")).setLore(
                                Lists.newArrayList("&8&m-----------------------------",
                                                            "&7DISPENSER",
                                                            "&b┃ &aDROPPER",
                                                            "&8&m-----------------------------"))
                        .toItemStack();


        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {


            if(airdrop.getMaterial().equals(Material.DISPENSER)){
                airdrop.setMaterial(Material.DROPPER);
                player.sendMessage(CC.translate("&aChanged the airdrop block to "+Material.DROPPER.name()));
                update(player);
                return;
            }
            airdrop.setMaterial(Material.DISPENSER);
            Airdrop.getAirdropMap().remove(airdrop.getName());
            Airdrop.getAirdropMap().put(airdrop.getName(),airdrop);
            update(player);
            player.sendMessage(CC.translate("&aChanged the airdrop block to "+Material.DISPENSER.name()));




        }

    }

    private class AirdropDisplayNameButton extends Button{
        private Airdrop airdrop;

        public AirdropDisplayNameButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(CompatibleMaterial.BOOK_AND_QUILL.getMaterial()).setName(CC.translate("&aChange Name")).setLore(Lists.newArrayList(CC.translate("&8&m-----------------------------"),CC.translate("&7Click the name of the airdrop."),CC.translate(""),CC.translate("&eClick to edit name."),CC.translate("&8&m-----------------------------"))).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {


            player.setMetadata("editdisplayname",new FixedMetadataValue(Crates.getInstance(),airdrop.getName()));
            player.sendMessage(CC.translate("&aType the display name you want to put"));
            AirdropListener.setAirdropname(airdrop.getName());
            player.closeInventory();

        }

    }

    private class AirdropLoreButton extends Button{
        private Airdrop airdrop;

        public AirdropLoreButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(Material.POWERED_RAIL).setName(CC.translate("&aChange Description")).setLore(Lists.newArrayList(CC.translate("&8&m-----------------------------"),CC.translate("&7Click the description of the airdrop."),CC.translate(""),CC.translate("&eClick to edit description."),CC.translate("&8&m-----------------------------"))).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {


            player.setMetadata("editlore",new FixedMetadataValue(Crates.getInstance(),airdrop.getName()));
            AirdropListener.setAirdropname(airdrop.getName());
            AirdropListener.sendLoreUsage(player);
            player.closeInventory();

        }

    }

    private class AirdropRewardsButton extends Button{
        private Airdrop airdrop;
        public AirdropRewardsButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(Material.CHEST).setName(CC.translate("&aChange Loot")).setLore(Lists.newArrayList(CC.translate("&8&m-----------------------------"),CC.translate("&7Change the loot of the airdrop."),CC.translate(""),CC.translate("&eClick to edit loot."),CC.translate("&8&m-----------------------------"))).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {

            player.closeInventory();
            new RewardsInventory(airdrop).openMenu(player);

        }


    }
    private class AirdropDeleteButton extends Button{
        private Airdrop airdrop;
        public AirdropDeleteButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(Material.REDSTONE_BLOCK).setName(CC.translate("&cDelete")).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {

            new ConfirmMenu("Are you sure?", data -> {
                if (data) {
                    player.closeInventory();
                    airdrop.delete();
                    player.sendMessage(CC.GREEN+"Successfully deleted airdrop");
                }else{
                    player.closeInventory();
                    new AirdropEditMenu(airdrop).openMenu(player);
                }
            }, true, this).openMenu(player);


        }


    }

    private class AirdropVisualsButton extends Button{
        private Airdrop airdrop;
        public AirdropVisualsButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(CompatibleMaterial.EXP_BOTTLE.getMaterial()).setName(CC.translate("&aChange Visuals")).setLore(Lists.newArrayList(CC.translate("&8&m-----------------------------"),CC.translate("&7Change the visuals of the airdrop."),CC.translate(""),CC.translate("&eClick to edit particles and hologram."),CC.translate("&8&m-----------------------------"))).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {

            player.closeInventory();
            new VisualsMenu(airdrop).openMenu(player);

        }


    }
    private class AirdropDestroyTimeButton extends Button{
        private Airdrop airdrop;
        public AirdropDestroyTimeButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(CompatibleMaterial.WATCH.getMaterial()).setName(CC.translate("&aChange Destroy Time")).setLore(Lists.newArrayList(CC.translate("&8&m-----------------------------"),CC.translate("&7Change the destroy time of the airdrop."),CC.translate(""),CC.translate("&7Destroy time: &f"+airdrop.getDestroytime()+"s"),CC.translate(""),CC.translate("&eClick to edit destroy time."),CC.translate("&8&m-----------------------------"))).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {

            player.closeInventory();
            player.setMetadata("editdestroytime",new FixedMetadataValue(Crates.getInstance(),airdrop.getName()));
            AirdropListener.setAirdropname(airdrop.getName());
            player.sendMessage(CC.translate("&aPlease type the time in seconds or type &c'Cancel' &ato exit this process"));

        }


    }
}
