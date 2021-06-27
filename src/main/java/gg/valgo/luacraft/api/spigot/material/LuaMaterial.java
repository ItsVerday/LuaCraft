package gg.valgo.luacraft.api.spigot.material;

import gg.valgo.luacraft.api.CustomLuaValue;
import gg.valgo.luacraft.api.CustomLuaValuePrototype;
import gg.valgo.luacraft.api.LuaTypes;
import org.bukkit.Material;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.HashMap;

public class LuaMaterial extends CustomLuaValue<LuaMaterial> {
    public static final CustomLuaValuePrototype<LuaMaterial> PROTOTYPE = new CustomLuaValuePrototype<>("material");

    static {
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().getBlastResistance()), "blastResistance", "blastProof", "blastHardness", "explosionResistance", "explosionProof", "explosionHardness");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().getCraftingRemainingItem()), "craftingRemainingItem", "remainingItem", "craftingLeftoverItem", "leftoverItem");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().getHardness()), "hardness", "toughness", "blockHardness", "blockToughness", "blockStrength");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().getKey().getKey()), "name", "id");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().getKey()), "key", "namespacedID", "namespacedKey");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().getMaxDurability()), "durability", "toolDurability", "uses", "toolUses", "damage", "toolDamage");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().getMaxStackSize()), "stackSize", "maxStackSize", "maxAmount");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().hasGravity()), "gravity", "hasGravity", "falls", "canFall");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isAir()), "isAir", "air", "isAirBlock", "airBlock");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isBlock()), "block", "isBlock", "placeable", "isPlaceable");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isBurnable()), "isBurnable", "burnable");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isEdible()), "isEdible", "edible", "isFood", "food");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isFlammable()), "isFlammable", "flammable");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isFuel()), "isFuel", "fuel");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isInteractable()), "isInteractable", "interactable");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isItem()), "isItem", "item");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isOccluding()), "isOccluding", "occluding");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isRecord()), "isRecord", "record", "isMusicDisc", "musicDisc", "isDisc", "disc");
        PROTOTYPE.getter(value -> valueOf(value.getMaterial().isSolid()), "isSolid", "solid");
    }

    private static HashMap<Material, LuaMaterial> luaMaterials = new HashMap<>();

    private Material material;

    private LuaMaterial(Material material) {
        this.material = material;
        usePrototype(PROTOTYPE);
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public LuaValue tostring() {
        return valueOf("material(id = " + getMaterial().getKey().getKey() + ")");
    }

    public static LuaMaterial getLuaMaterial(Material material) {
        return luaMaterials.get(material);
    }

    public static LuaTable createAPI() {
        LuaTable api = LuaTable.tableOf();
        api.rawset("fromName", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                return LuaTypes.spigotTypes(Material.matchMaterial(luaValue.checkjstring()));
            }
        });

        LuaTable allTable = tableOf();
        for (Material spigotMaterial : Material.values()) {
            LuaMaterial luaMaterial = luaMaterials.get(spigotMaterial);
            api.rawset(spigotMaterial.name(), luaMaterial);
            api.rawset(spigotMaterial.getKey().getKey(), luaMaterial);
            allTable.set(allTable.length() + 1, luaMaterial);
        }

        api.rawset("all", allTable);

        return api;
    }

    public static Material checkMaterial(LuaValue luaValue) {
        if (!(luaValue instanceof LuaMaterial)) {
            argerror("material", luaValue);
        }

        return ((LuaMaterial) luaValue).getMaterial();
    }

    static {
        for (Material spigotMaterial : Material.values()) {
            luaMaterials.put(spigotMaterial, new LuaMaterial(spigotMaterial));
        }
    }
}