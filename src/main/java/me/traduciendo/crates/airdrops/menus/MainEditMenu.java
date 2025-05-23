package me.traduciendo.crates.airdrops.menus;

import me.traduciendo.crates.airdrops.rewards.Airdrop;
import me.traduciendo.crates.airdrops.util.item.ItemBuilder;
import me.traduciendo.crates.airdrops.util.menu.Button;
import me.traduciendo.crates.airdrops.util.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MainEditMenu extends Menu {
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer,Button> buttons = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, Airdrop> entry : Airdrop.getAirdropMap().entrySet()) {
            String key = entry.getKey();
            Airdrop value = entry.getValue();
            buttons.put(i,new AirdropEditButton(value));
            i++;
        }
        return buttons;
    }

    @Override
    public int size(final Map<Integer, Button> buttons){
        return 9;
    }

    @Override
    public String getTitle(final Player player) {
        return "Airdrops";
    }


    private class AirdropEditButton extends Button {

        private Airdrop airdrop;
        public AirdropEditButton(Airdrop airdrop){
            this.airdrop = airdrop;
        }

        @Override
        public ItemStack getButtonItem(final Player player){
            return new ItemBuilder(airdrop.getMaterial()).setName(airdrop.getDisplayname()).setLore(airdrop.getLore()).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new AirdropEditMenu(airdrop).openMenu(player);
        }

    }
}
