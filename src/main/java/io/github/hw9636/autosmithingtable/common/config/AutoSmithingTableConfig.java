/*
    This file is part of 9636Dev's AutoSmithingTableMod.

    AutoSmithingTableMod is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AutoSmithingTableMod is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with AutoSmithingTableMod.  If not, see <https://www.gnu.org/licenses/>.
*/
package io.github.hw9636.autosmithingtable.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class AutoSmithingTableConfig {
    public static class Common
    {
        private static final int defaultMaxEnergyStored = 100000;
        private static final int defaultTicksPerCraft = 20;
        private static final int defaultEnergyPerTick = 40;

        public final ForgeConfigSpec.ConfigValue<Integer> maxEnergyStored;
        public final ForgeConfigSpec.ConfigValue<Integer> ticksPerCraft;
        public final ForgeConfigSpec.ConfigValue<Integer> energyPerTick;



        public Common(ForgeConfigSpec.Builder builder)
        {

            this.maxEnergyStored = builder.comment("Max Energy Stored, range 100 - 1000000, default 100000")
                    .defineInRange("maxEnergyStored", defaultMaxEnergyStored, 100,1000000);

            this.ticksPerCraft = builder.comment("Ticks per craft in ticks, range 1 - 2048, default 20")
                    .defineInRange("ticksPerCraft", defaultTicksPerCraft, 1, 2048);

            this.energyPerTick = builder.comment("Energy per tick of crafting, range 1 - 8192, default 40")
                    .defineInRange("energyPerTick", defaultEnergyPerTick, 1, 8192);
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static //constructor
    {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }

}
