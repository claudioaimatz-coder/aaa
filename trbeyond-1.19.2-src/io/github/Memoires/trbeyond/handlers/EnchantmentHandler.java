package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.attribute.AttributeModifierHelper;
import com.github.manasmods.manascore.api.skills.event.SkillDamageEvent.PreCalculation;
import com.github.manasmods.manascore.attribute.ManasCoreAttributes;
import com.github.manasmods.tensura.event.EngravingEvent;
import com.github.manasmods.tensura.event.ItemDamageEvent;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.enchantment.InfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.SpatialInfusionEnchantment;
import io.github.Memoires.trbeyond.items.armor.BenimaruArmorItem;
import io.github.Memoires.trbeyond.items.armor.DiabloArmorItem;
import io.github.Memoires.trbeyond.items.armor.GabiruArmorItem;
import io.github.Memoires.trbeyond.items.armor.GeldArmorItem;
import io.github.Memoires.trbeyond.items.armor.MinosArmorItem;
import io.github.Memoires.trbeyond.items.armor.OgreArmorItem;
import io.github.Memoires.trbeyond.items.armor.SerpentArmorItem;
import io.github.Memoires.trbeyond.items.armor.ShionArmorItem;
import io.github.Memoires.trbeyond.items.armor.SoueiArmorItem;
import io.github.Memoires.trbeyond.items.weapons.DiabloClawItem;
import io.github.Memoires.trbeyond.items.weapons.GorikimaruItem;
import io.github.Memoires.trbeyond.items.weapons.GurenItem;
import io.github.Memoires.trbeyond.items.weapons.MeatCleaverItem;
import io.github.Memoires.trbeyond.items.weapons.MinosBardicheItem;
import io.github.Memoires.trbeyond.items.weapons.MinosTridentItem;
import io.github.Memoires.trbeyond.items.weapons.OgreAxeItem;
import io.github.Memoires.trbeyond.items.weapons.ShibkoboItem;
import io.github.Memoires.trbeyond.items.weapons.VortexSpearItem;
import io.github.Memoires.trbeyond.items.weapons.scaleShieldItem;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent.Added;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class EnchantmentHandler {
   public static final UUID EARTH_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("48577585-c015-4b88-8e29-2d136c10666e");
   public static final UUID LAND_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("73456a7f-dc01-4953-87d6-735a2221b9d3");
   public static final UUID WIND_FLYING_SPEED_UUID = UUID.fromString("6324ba8c-f62d-4b18-ada3-4db34fd9eb9b");
   public static final UUID SWIM_SPEED_UUID = UUID.fromString("7d66ee5f-a616-4eff-9768-ce26f673f43c");

   @SubscribeEvent
   public static void onEngraving(EngravingEvent event) {
      ItemStack stack = event.getStack();
      if (stack.getItem() instanceof SerpentArmorItem) {
         if (!event.getEnchantment().equals(TensuraEnchantments.STURDY.get())) {
            event.setCanceled(true);
         }
      } else if (stack.getItem() instanceof MinosArmorItem && !event.getEnchantment().equals(TensuraEnchantments.HOLY_COAT.get())) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void updateEnchantments(LivingEquipmentChangeEvent event) {
      ItemStack toStack = event.getTo();
      ItemStack fromStack = event.getFrom();
      if (!ItemStack.isSame(toStack, fromStack)) {
         LivingEntity entity = event.getEntity();
         OgreArmorItem.applyEffects(toStack, fromStack, entity, event.getSlot());
         SerpentArmorItem.applySturdy(toStack, fromStack, entity, event.getSlot());
         MinosArmorItem.applyHolyCoat(toStack, fromStack, entity, event.getSlot());
         GorikimaruItem.applyEnchants(toStack, fromStack, event.getSlot());
         GurenItem.applyEnchants(toStack, fromStack, event.getSlot());
         DiabloClawItem.applyEnchants(toStack, fromStack, event.getSlot());
         GeldArmorItem.applyEnchants(toStack, fromStack, event.getSlot());
         ShionArmorItem.applyBattleInfusion(toStack, fromStack, entity, event.getSlot());
         BenimaruArmorItem.applyFlameCoat(toStack, fromStack, entity, event.getSlot());
         DiabloArmorItem.applyEnchants(toStack, fromStack, event.getSlot());
         SoueiArmorItem.applyEnchants(toStack, fromStack, event.getSlot());
         GabiruArmorItem.applyEnchants(toStack, fromStack, event.getSlot());
         MinosTridentItem.applyEnchants(entity);
         MinosBardicheItem.applyEnchants(toStack, entity, event.getSlot());
         MeatCleaverItem.applyEnchants(toStack, fromStack, event.getSlot());
         scaleShieldItem.applyEnchants(toStack, fromStack, event.getSlot());
         ShibkoboItem.applyEnchants(toStack, fromStack, event.getSlot());
         VortexSpearItem.applyEnchants(toStack, fromStack, event.getSlot());
         InfusionEnchantment.addInfusionAttribute(
            event, (Enchantment)BeyondEnchantments.EARTH_INFUSION.get(), Attributes.KNOCKBACK_RESISTANCE, EARTH_KNOCKBACK_RESISTANCE_UUID, 0.25F
         );
         InfusionEnchantment.addInfusionAttribute(
            event, (Enchantment)BeyondEnchantments.LAND_INFUSION.get(), Attributes.KNOCKBACK_RESISTANCE, LAND_KNOCKBACK_RESISTANCE_UUID, 0.5F
         );
         InfusionEnchantment.addInfusionAttribute(
            event,
            (Enchantment)BeyondEnchantments.WIND_INFUSION.get(),
            (Attribute)ManasCoreAttributes.FLYING_SPEED_MULTIPLIER.get(),
            WIND_FLYING_SPEED_UUID,
            1.0F
         );
         InfusionEnchantment.addInfusionAttribute(event, (Enchantment)BeyondEnchantments.WIND_INFUSION.get(), Attributes.ATTACK_SPEED, WIND_FLYING_SPEED_UUID, 0.2F);
         if (InfusionEnchantment.hasFullSetEffect(entity, (Enchantment)BeyondEnchantments.WATER_INFUSION.get())) {
            AttributeModifierHelper.setModifier(
               entity, (Attribute)ForgeMod.SWIM_SPEED.get(), new AttributeModifier(SWIM_SPEED_UUID, "Water Infusion", 2.0, Operation.MULTIPLY_TOTAL)
            );
         } else {
            AttributeModifierHelper.removeModifier(entity, (Attribute)ForgeMod.SWIM_SPEED.get(), SWIM_SPEED_UUID);
         }
      }
   }

   @SubscribeEvent
   public static void onGearUseEP(ItemDamageEvent e) {
      ItemStack stack = e.getStack();
      if (stack.getItem() instanceof SerpentArmorItem) {
         ServerPlayer player = e.getPlayer();
         if (player == null) {
            return;
         }

         if (player.hasEffect((MobEffect)TensuraMobEffects.CORROSION.get())) {
            e.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public static void getBreakSpeed(BreakSpeed event) {
      Player player = event.getEntity();
      if (player.isEyeInFluid(FluidTags.WATER)) {
         if (InfusionEnchantment.hasFullSetEffect(player, (Enchantment)BeyondEnchantments.WATER_INFUSION.get())) {
            event.setNewSpeed(event.getOriginalSpeed() * 5.0F);
         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void preCalculation(PreCalculation e) {
      if (OgreAxeItem.canBoostDamage(e.getSource())) {
         e.setAmount(e.getAmount() * 1.2F);
      }
   }

   @SubscribeEvent
   public static void onLivingHurt(LivingHurtEvent event) {
      LivingEntity entity = event.getEntity();
      DamageSource originalSource = event.getSource();
      if (!originalSource.isBypassInvul()) {
         if (!originalSource.getMsgId().equals("physical")) {
            if (entity instanceof Player && InfusionEnchantment.hasFullSetEffect(entity, (Enchantment)BeyondEnchantments.BATTLE_INFUSION.get())) {
               float damage = event.getAmount();
               event.setCanceled(true);
               DamageSource physical = makePhysical(entity);
               entity.hurt(physical, damage);
            }
         }
      }
   }

   private static DamageSource makePhysical(LivingEntity attacker) {
      return new EntityDamageSource("physical", attacker);
   }

   @SubscribeEvent
   public void onEffectAdded(Added event) {
      LivingEntity entity = event.getEntity();
      if (entity instanceof Player) {
         MobEffectInstance effect = event.getEffectInstance();
         if (effect.getEffect() == TensuraMobEffects.SEVERANCE_BLADE.get() && hasSpatialInfusionInArmor(entity)) {
            event.setCanceled(true);
         }
      }
   }

   private static boolean hasSpatialInfusionInArmor(LivingEntity entity) {
      for (EquipmentSlot slot : EquipmentSlot.values()) {
         if (slot.getType() == Type.ARMOR) {
            ItemStack armorItem = entity.getItemBySlot(slot);
            if (!armorItem.isEmpty()) {
               Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(armorItem);
               return enchantments.keySet().stream().anyMatch(e -> e instanceof SpatialInfusionEnchantment);
            }
         }
      }

      return false;
   }
}
