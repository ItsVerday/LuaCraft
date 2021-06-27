package gg.valgo.luacraft.api.spigot.inventory.itemstack;

import gg.valgo.luacraft.api.CustomLuaValue;
import gg.valgo.luacraft.api.CustomLuaValuePrototype;
import gg.valgo.luacraft.api.LuaTypes;
import gg.valgo.luacraft.api.spigot.material.LuaMaterial;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaValue;

public class LuaItemStack extends CustomLuaValue<LuaItemStack> {
    public static final CustomLuaValuePrototype<LuaItemStack> PROTOTYPE = new CustomLuaValuePrototype<>("itemstack");

    static {
        PROTOTYPE.getterSetter(value -> LuaTypes.valueOf(value.getItemStack().getType()), (value, material) -> {
            ItemStack itemStackToEdit = value.getItemStack();
            itemStackToEdit.setType(LuaMaterial.checkMaterial(material));
            value.setItemStack(itemStackToEdit);
        }, "item", "itemType", "material", "materialType", "type", "id");
        PROTOTYPE.getterSetter(value -> LuaTypes.valueOf(value.getItemStack().getAmount()), (value, amount) -> {
            ItemStack itemStackToEdit = value.getItemStack();
            itemStackToEdit.setAmount(amount.checkint());
            value.setItemStack(itemStackToEdit);
        }, "count", "itemCount", "amount", "itemAmount", "num", "number");
    }

    private ItemStack itemStack;

    public LuaItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public LuaItemStack setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public LuaValue tostring() {
        return valueOf("itemstack(type = " + getItemStack().getType().getKey().getKey() + ", amount = " + getItemStack().getAmount() + ")");
    }
}