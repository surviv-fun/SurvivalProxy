/*
 * Copyright (c) LuciferMorningstarDev <contact@lucifer-morningstar.dev>
 * Copyright (c) surviv.fun <contact@surviv.fun>
 * Copyright (C) surviv.fun team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fun.surviv.proxy.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * SurvivalProxy; fun.surviv.proxy.utils:MathUtils
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

    private static final Random random = new Random();

    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    public static int randomMinMax(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min must be greater than max");
        }
        return random.nextInt(max + 1 - min) + min;
    }

    public static double roundDouble2Places(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        double salary = bd.doubleValue();
        return Double.valueOf(salary);
    }

    public static double round2Decimals(double d) {
        return Math.round(d * 100.0) / 100.0;
    }

    public static class RandomProbability {

        private final static int DENOM = (int) Math.pow(2, 24);
        private static final int[] bitCount = new int[]{18, 12, 6, 0};
        private static final int[] cumPow64 = new int[]{
                (int) (Math.pow(64, 3) + Math.pow(64, 2) + Math.pow(64, 1) + Math.pow(64, 0)),
                (int) (Math.pow(64, 2) + Math.pow(64, 1) + Math.pow(64, 0)),
                (int) (Math.pow(64, 1) + Math.pow(64, 0)),
                (int) (Math.pow(64, 0))
        };
        ArrayList[] base64Table = {new ArrayList<Integer>()
                , new ArrayList<Integer>()
                , new ArrayList<Integer>()
                , new ArrayList<Integer>()};
        private int[] t_array = new int[4];
        private int sumOfNumerator;

        public int nextNum() {
            int rand = (int) (random.nextFloat() * sumOfNumerator);

            for (int x = 0; x < 4; x++) {
                if (rand < t_array[x]) {
                    return x == 0 ? (int) base64Table[x].get(rand >> bitCount[x])
                            : (int) base64Table[x].get((rand - t_array[x - 1]) >> bitCount[x]);
                }
            }
            return 0;
        }

        public void setIntProbList(int[] intList, float[] probList) {
            Map<Integer, Float> map = normalizeMap(intList, probList);
            populateBase64Table(map);
        }

        private void clearBase64Table() {
            for (int x = 0; x < 4; x++) {
                base64Table[x].clear();
            }
        }

        private void populateBase64Table(Map<Integer, Float> intProbMap) {
            int startPow, decodedFreq, table_index;
            float rem;

            clearBase64Table();

            for (Map.Entry<Integer, Float> numObj : intProbMap.entrySet()) {
                rem = numObj.getValue();
                table_index = 3;
                for (int x = 0; x < 4; x++) {
                    decodedFreq = (int) (rem % 64);
                    rem /= 64;
                    for (int y = 0; y < decodedFreq; y++) {
                        base64Table[table_index].add(numObj.getKey());
                    }
                    table_index--;
                }
            }

            startPow = 3;
            for (int x = 0; x < 4; x++) {
                t_array[x] = x == 0 ? (int) (Math.pow(64, startPow--) * base64Table[x].size())
                        : ((int) ((Math.pow(64, startPow--) * base64Table[x].size()) + t_array[x - 1]));
            }

        }

        private Map<Integer, Float> normalizeMap(int[] intList, float[] probList) {
            Map<Integer, Float> tmpMap = new HashMap<>();
            Float mappedFloat;
            int numerator;
            float normalizedProb, distSum = 0;

            //Remove duplicates, and calculate the sum of non-repeated keys
            for (int x = 0; x < probList.length; x++) {
                mappedFloat = tmpMap.get(intList[x]);
                if (mappedFloat != null) {
                    distSum -= mappedFloat;
                } else {
                    distSum += probList[x];
                }
                tmpMap.put(intList[x], probList[x]);
            }

            //Normalise the map to key -> corresponding numerator by multiplying with 2^24
            sumOfNumerator = 0;
            for (Map.Entry<Integer, Float> intProb : tmpMap.entrySet()) {
                normalizedProb = intProb.getValue() / distSum;
                numerator = (int) (normalizedProb * DENOM);
                intProb.setValue((float) numerator);
                sumOfNumerator += numerator;
            }

            return tmpMap;
        }

    }

}
