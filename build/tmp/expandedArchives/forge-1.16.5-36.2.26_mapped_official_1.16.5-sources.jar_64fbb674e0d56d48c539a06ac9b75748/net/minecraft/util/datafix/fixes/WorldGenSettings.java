package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.datafix.TypeReferences;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public class WorldGenSettings extends DataFix {
   private static final ImmutableMap<String, WorldGenSettings.StructureSeparationSettingsCodec> DEFAULTS = ImmutableMap.<String, WorldGenSettings.StructureSeparationSettingsCodec>builder().put("minecraft:village", new WorldGenSettings.StructureSeparationSettingsCodec(32, 8, 10387312)).put("minecraft:desert_pyramid", new WorldGenSettings.StructureSeparationSettingsCodec(32, 8, 14357617)).put("minecraft:igloo", new WorldGenSettings.StructureSeparationSettingsCodec(32, 8, 14357618)).put("minecraft:jungle_pyramid", new WorldGenSettings.StructureSeparationSettingsCodec(32, 8, 14357619)).put("minecraft:swamp_hut", new WorldGenSettings.StructureSeparationSettingsCodec(32, 8, 14357620)).put("minecraft:pillager_outpost", new WorldGenSettings.StructureSeparationSettingsCodec(32, 8, 165745296)).put("minecraft:monument", new WorldGenSettings.StructureSeparationSettingsCodec(32, 5, 10387313)).put("minecraft:endcity", new WorldGenSettings.StructureSeparationSettingsCodec(20, 11, 10387313)).put("minecraft:mansion", new WorldGenSettings.StructureSeparationSettingsCodec(80, 20, 10387319)).build();

   public WorldGenSettings(Schema p_i231469_1_) {
      super(p_i231469_1_, true);
   }

   protected TypeRewriteRule makeRule() {
      return this.fixTypeEverywhereTyped("WorldGenSettings building", this.getInputSchema().getType(TypeReferences.WORLD_GEN_SETTINGS), (p_233425_0_) -> {
         return p_233425_0_.update(DSL.remainderFinder(), WorldGenSettings::fix);
      });
   }

   private static <T> Dynamic<T> noise(long p_233423_0_, DynamicLike<T> p_233423_2_, Dynamic<T> p_233423_3_, Dynamic<T> p_233423_4_) {
      return p_233423_2_.createMap(ImmutableMap.of(p_233423_2_.createString("type"), p_233423_2_.createString("minecraft:noise"), p_233423_2_.createString("biome_source"), p_233423_4_, p_233423_2_.createString("seed"), p_233423_2_.createLong(p_233423_0_), p_233423_2_.createString("settings"), p_233423_3_));
   }

   private static <T> Dynamic<T> vanillaBiomeSource(Dynamic<T> p_233427_0_, long p_233427_1_, boolean p_233427_3_, boolean p_233427_4_) {
      Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.<Dynamic<T>, Dynamic<T>>builder().put(p_233427_0_.createString("type"), p_233427_0_.createString("minecraft:vanilla_layered")).put(p_233427_0_.createString("seed"), p_233427_0_.createLong(p_233427_1_)).put(p_233427_0_.createString("large_biomes"), p_233427_0_.createBoolean(p_233427_4_));
      if (p_233427_3_) {
         builder.put(p_233427_0_.createString("legacy_biome_init_layer"), p_233427_0_.createBoolean(p_233427_3_));
      }

      return p_233427_0_.createMap(builder.build());
   }

   private static <T> Dynamic<T> fix(Dynamic<T> p_233426_0_) {
      DynamicOps<T> dynamicops = p_233426_0_.getOps();
      long i = p_233426_0_.get("RandomSeed").asLong(0L);
      Optional<String> optional = p_233426_0_.get("generatorName").asString().map((p_233433_0_) -> {
         return p_233433_0_.toLowerCase(Locale.ROOT);
      }).result();
      Optional<String> optional1 = p_233426_0_.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> {
         return optional.equals(Optional.of("customized")) ? p_233426_0_.get("generatorOptions").asString().result() : Optional.empty();
      });
      boolean flag = false;
      Dynamic<T> dynamic;
      if (optional.equals(Optional.of("customized"))) {
         dynamic = defaultOverworld(p_233426_0_, i);
      } else if (!optional.isPresent()) {
         dynamic = defaultOverworld(p_233426_0_, i);
      } else {
         String lvt_8_1_ = optional.get();
         switch(lvt_8_1_) {
         case "flat":
            OptionalDynamic<T> optionaldynamic = p_233426_0_.get("generatorOptions");
            Map<Dynamic<T>, Dynamic<T>> map = fixFlatStructures(dynamicops, optionaldynamic);
            dynamic = p_233426_0_.createMap(ImmutableMap.of(p_233426_0_.createString("type"), p_233426_0_.createString("minecraft:flat"), p_233426_0_.createString("settings"), p_233426_0_.createMap(ImmutableMap.of(p_233426_0_.createString("structures"), p_233426_0_.createMap(map), p_233426_0_.createString("layers"), optionaldynamic.get("layers").result().orElseGet(() -> {
               return p_233426_0_.createList(Stream.of(p_233426_0_.createMap(ImmutableMap.of(p_233426_0_.createString("height"), p_233426_0_.createInt(1), p_233426_0_.createString("block"), p_233426_0_.createString("minecraft:bedrock"))), p_233426_0_.createMap(ImmutableMap.of(p_233426_0_.createString("height"), p_233426_0_.createInt(2), p_233426_0_.createString("block"), p_233426_0_.createString("minecraft:dirt"))), p_233426_0_.createMap(ImmutableMap.of(p_233426_0_.createString("height"), p_233426_0_.createInt(1), p_233426_0_.createString("block"), p_233426_0_.createString("minecraft:grass_block")))));
            }), p_233426_0_.createString("biome"), p_233426_0_.createString(optionaldynamic.get("biome").asString("minecraft:plains"))))));
            break;
         case "debug_all_block_states":
            dynamic = p_233426_0_.createMap(ImmutableMap.of(p_233426_0_.createString("type"), p_233426_0_.createString("minecraft:debug")));
            break;
         case "buffet":
            OptionalDynamic<T> optionaldynamic1 = p_233426_0_.get("generatorOptions");
            OptionalDynamic<?> optionaldynamic2 = optionaldynamic1.get("chunk_generator");
            Optional<String> optional2 = optionaldynamic2.get("type").asString().result();
            Dynamic<T> dynamic1;
            if (Objects.equals(optional2, Optional.of("minecraft:caves"))) {
               dynamic1 = p_233426_0_.createString("minecraft:caves");
               flag = true;
            } else if (Objects.equals(optional2, Optional.of("minecraft:floating_islands"))) {
               dynamic1 = p_233426_0_.createString("minecraft:floating_islands");
            } else {
               dynamic1 = p_233426_0_.createString("minecraft:overworld");
            }

            Dynamic<T> dynamic2 = optionaldynamic1.get("biome_source").result().orElseGet(() -> {
               return p_233426_0_.createMap(ImmutableMap.of(p_233426_0_.createString("type"), p_233426_0_.createString("minecraft:fixed")));
            });
            Dynamic<T> dynamic3;
            if (dynamic2.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) {
               String s1 = dynamic2.get("options").get("biomes").asStream().findFirst().flatMap((p_233440_0_) -> {
                  return p_233440_0_.asString().result();
               }).orElse("minecraft:ocean");
               dynamic3 = dynamic2.remove("options").set("biome", p_233426_0_.createString(s1));
            } else {
               dynamic3 = dynamic2;
            }

            dynamic = noise(i, p_233426_0_, dynamic1, dynamic3);
            break;
         default:
            boolean flag6 = optional.get().equals("default");
            boolean flag1 = optional.get().equals("default_1_1") || flag6 && p_233426_0_.get("generatorVersion").asInt(0) == 0;
            boolean flag2 = optional.get().equals("amplified");
            boolean flag3 = optional.get().equals("largebiomes");
            dynamic = noise(i, p_233426_0_, p_233426_0_.createString(flag2 ? "minecraft:amplified" : "minecraft:overworld"), vanillaBiomeSource(p_233426_0_, i, flag1, flag3));
         }
      }

      boolean flag4 = p_233426_0_.get("MapFeatures").asBoolean(true);
      boolean flag5 = p_233426_0_.get("BonusChest").asBoolean(false);
      Builder<T, T> builder = ImmutableMap.builder();
      builder.put(dynamicops.createString("seed"), dynamicops.createLong(i));
      builder.put(dynamicops.createString("generate_features"), dynamicops.createBoolean(flag4));
      builder.put(dynamicops.createString("bonus_chest"), dynamicops.createBoolean(flag5));
      builder.put(dynamicops.createString("dimensions"), vanillaLevels(p_233426_0_, i, dynamic, flag));
      optional1.ifPresent((p_233424_2_) -> {
         builder.put(dynamicops.createString("legacy_custom_options"), dynamicops.createString(p_233424_2_));
      });
      return new Dynamic<>(dynamicops, dynamicops.createMap(builder.build()));
   }

   protected static <T> Dynamic<T> defaultOverworld(Dynamic<T> p_241322_0_, long p_241322_1_) {
      return noise(p_241322_1_, p_241322_0_, p_241322_0_.createString("minecraft:overworld"), vanillaBiomeSource(p_241322_0_, p_241322_1_, false, false));
   }

   protected static <T> T vanillaLevels(Dynamic<T> p_241323_0_, long p_241323_1_, Dynamic<T> p_241323_3_, boolean p_241323_4_) {
      DynamicOps<T> dynamicops = p_241323_0_.getOps();
      return dynamicops.createMap(ImmutableMap.of(dynamicops.createString("minecraft:overworld"), dynamicops.createMap(ImmutableMap.of(dynamicops.createString("type"), dynamicops.createString("minecraft:overworld" + (p_241323_4_ ? "_caves" : "")), dynamicops.createString("generator"), p_241323_3_.getValue())), dynamicops.createString("minecraft:the_nether"), dynamicops.createMap(ImmutableMap.of(dynamicops.createString("type"), dynamicops.createString("minecraft:the_nether"), dynamicops.createString("generator"), noise(p_241323_1_, p_241323_0_, p_241323_0_.createString("minecraft:nether"), p_241323_0_.createMap(ImmutableMap.of(p_241323_0_.createString("type"), p_241323_0_.createString("minecraft:multi_noise"), p_241323_0_.createString("seed"), p_241323_0_.createLong(p_241323_1_), p_241323_0_.createString("preset"), p_241323_0_.createString("minecraft:nether")))).getValue())), dynamicops.createString("minecraft:the_end"), dynamicops.createMap(ImmutableMap.of(dynamicops.createString("type"), dynamicops.createString("minecraft:the_end"), dynamicops.createString("generator"), noise(p_241323_1_, p_241323_0_, p_241323_0_.createString("minecraft:end"), p_241323_0_.createMap(ImmutableMap.of(p_241323_0_.createString("type"), p_241323_0_.createString("minecraft:the_end"), p_241323_0_.createString("seed"), p_241323_0_.createLong(p_241323_1_)))).getValue()))));
   }

   private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(DynamicOps<T> p_233430_0_, OptionalDynamic<T> p_233430_1_) {
      MutableInt mutableint = new MutableInt(32);
      MutableInt mutableint1 = new MutableInt(3);
      MutableInt mutableint2 = new MutableInt(128);
      MutableBoolean mutableboolean = new MutableBoolean(false);
      Map<String, WorldGenSettings.StructureSeparationSettingsCodec> map = Maps.newHashMap();
      if (!p_233430_1_.result().isPresent()) {
         mutableboolean.setTrue();
         map.put("minecraft:village", DEFAULTS.get("minecraft:village"));
      }

      p_233430_1_.get("structures").flatMap(Dynamic::getMapValues).result().ifPresent((p_233439_5_) -> {
         p_233439_5_.forEach((p_233438_5_, p_233438_6_) -> {
            p_233438_6_.getMapValues().result().ifPresent((p_233429_6_) -> {
               p_233429_6_.forEach((p_233428_6_, p_233428_7_) -> {
                  String s = p_233438_5_.asString("");
                  String s1 = p_233428_6_.asString("");
                  String s2 = p_233428_7_.asString("");
                  if ("stronghold".equals(s)) {
                     mutableboolean.setTrue();
                     switch(s1) {
                     case "distance":
                        mutableint.setValue(getInt(s2, mutableint.getValue(), 1));
                        return;
                     case "spread":
                        mutableint1.setValue(getInt(s2, mutableint1.getValue(), 1));
                        return;
                     case "count":
                        mutableint2.setValue(getInt(s2, mutableint2.getValue(), 1));
                        return;
                     default:
                     }
                  } else {
                     switch(s1) {
                     case "distance":
                        switch(s) {
                        case "village":
                           setSpacing(map, "minecraft:village", s2, 9);
                           return;
                        case "biome_1":
                           setSpacing(map, "minecraft:desert_pyramid", s2, 9);
                           setSpacing(map, "minecraft:igloo", s2, 9);
                           setSpacing(map, "minecraft:jungle_pyramid", s2, 9);
                           setSpacing(map, "minecraft:swamp_hut", s2, 9);
                           setSpacing(map, "minecraft:pillager_outpost", s2, 9);
                           return;
                        case "endcity":
                           setSpacing(map, "minecraft:endcity", s2, 1);
                           return;
                        case "mansion":
                           setSpacing(map, "minecraft:mansion", s2, 1);
                           return;
                        default:
                           return;
                        }
                     case "separation":
                        if ("oceanmonument".equals(s)) {
                           WorldGenSettings.StructureSeparationSettingsCodec worldgensettings$structureseparationsettingscodec = map.getOrDefault("minecraft:monument", DEFAULTS.get("minecraft:monument"));
                           int i = getInt(s2, worldgensettings$structureseparationsettingscodec.separation, 1);
                           map.put("minecraft:monument", new WorldGenSettings.StructureSeparationSettingsCodec(i, worldgensettings$structureseparationsettingscodec.separation, worldgensettings$structureseparationsettingscodec.salt));
                        }

                        return;
                     case "spacing":
                        if ("oceanmonument".equals(s)) {
                           setSpacing(map, "minecraft:monument", s2, 1);
                        }

                        return;
                     default:
                     }
                  }
               });
            });
         });
      });
      Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.builder();
      builder.put(p_233430_1_.createString("structures"), p_233430_1_.createMap(map.entrySet().stream().collect(Collectors.toMap((p_233432_1_) -> {
         return p_233430_1_.createString(p_233432_1_.getKey());
      }, (p_233431_1_) -> {
         return p_233431_1_.getValue().serialize(p_233430_0_);
      }))));
      if (mutableboolean.isTrue()) {
         builder.put(p_233430_1_.createString("stronghold"), p_233430_1_.createMap(ImmutableMap.of(p_233430_1_.createString("distance"), p_233430_1_.createInt(mutableint.getValue()), p_233430_1_.createString("spread"), p_233430_1_.createInt(mutableint1.getValue()), p_233430_1_.createString("count"), p_233430_1_.createInt(mutableint2.getValue()))));
      }

      return builder.build();
   }

   private static int getInt(String p_233434_0_, int p_233434_1_) {
      return NumberUtils.toInt(p_233434_0_, p_233434_1_);
   }

   private static int getInt(String p_233435_0_, int p_233435_1_, int p_233435_2_) {
      return Math.max(p_233435_2_, getInt(p_233435_0_, p_233435_1_));
   }

   private static void setSpacing(Map<String, WorldGenSettings.StructureSeparationSettingsCodec> p_233436_0_, String p_233436_1_, String p_233436_2_, int p_233436_3_) {
      WorldGenSettings.StructureSeparationSettingsCodec worldgensettings$structureseparationsettingscodec = p_233436_0_.getOrDefault(p_233436_1_, DEFAULTS.get(p_233436_1_));
      int i = getInt(p_233436_2_, worldgensettings$structureseparationsettingscodec.spacing, p_233436_3_);
      p_233436_0_.put(p_233436_1_, new WorldGenSettings.StructureSeparationSettingsCodec(i, worldgensettings$structureseparationsettingscodec.separation, worldgensettings$structureseparationsettingscodec.salt));
   }

   static final class StructureSeparationSettingsCodec {
      public static final Codec<WorldGenSettings.StructureSeparationSettingsCodec> CODEC = RecordCodecBuilder.create((p_233448_0_) -> {
         return p_233448_0_.group(Codec.INT.fieldOf("spacing").forGetter((p_233453_0_) -> {
            return p_233453_0_.spacing;
         }), Codec.INT.fieldOf("separation").forGetter((p_233452_0_) -> {
            return p_233452_0_.separation;
         }), Codec.INT.fieldOf("salt").forGetter((p_233451_0_) -> {
            return p_233451_0_.salt;
         })).apply(p_233448_0_, WorldGenSettings.StructureSeparationSettingsCodec::new);
      });
      private final int spacing;
      private final int separation;
      private final int salt;

      public StructureSeparationSettingsCodec(int p_i231470_1_, int p_i231470_2_, int p_i231470_3_) {
         this.spacing = p_i231470_1_;
         this.separation = p_i231470_2_;
         this.salt = p_i231470_3_;
      }

      public <T> Dynamic<T> serialize(DynamicOps<T> p_233447_1_) {
         return new Dynamic<>(p_233447_1_, CODEC.encodeStart(p_233447_1_, this).result().orElse(p_233447_1_.emptyMap()));
      }
   }
}