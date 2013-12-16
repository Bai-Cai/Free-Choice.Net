/*******************************************************************************
 * Copyright (c) 2013 BowenCai.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     BowenCai - initial API and implementation
 ******************************************************************************/
package net.freechoice.util;


/**
 * 
 * @author BowenCai
 *
 */

public abstract class HashUtil {
	
	public static int hashIntegers(int...ii) {
		int hash = 1;
		for (int i : ii) {
			hash = 31*hash + i;
		}
		return hash;
	}
	
	public static int hash64To32(long lg) {
		
		return (int)(lg ^ (lg >>> 32));
	}
	
    public static int hashIntArray(int seed, int[] data, int offset, int len) {
        int h1 = seed;

        int off = offset;
        int end = offset + len;

        // body
        while (off < end) {
            int k1 = data[off++];

            k1 *= 0xcc9e2d51;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= 0x1b873593;

            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;
        }

        // tail (always empty, as body is always 32-bit chunks)

        // finalization

        h1 ^= len * (Integer.SIZE / Byte.SIZE);

        // finalization mix force all bits of a hash block to avalanche
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        return h1;
    }
    
    public static int hashCharArray(int seed, char[] data, int offset, int len) {
        int h1 = seed;

        int off = offset;
        int count = len;

        // body
        while (count >= 2) {
            int k1 = (data[off++] & 0xFFFF) | (data[off++] << 16);

            count -= 2;

            k1 *= 0xcc9e2d51;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= 0x1b873593;

            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;
        }
        // last one
        if (count > 0) {
            int k1 = data[off];

            k1 *= 0xcc9e2d51;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= 0x1b873593;
            h1 ^= k1;
        }

        // finish it

        h1 ^= len * (Character.SIZE / Byte.SIZE);

        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        return h1;
    }
    
    public static int hashBytes(int seed, byte[] data, int offset, int len) {
        int h1 = seed;
        int count = len;

        // body
        while (count >= 4) {
            int k1 = (data[offset] & 0x0FF)
                    | (data[offset + 1] & 0x0FF) << 8
                    | (data[offset + 2] & 0x0FF) << 16
                    | data[offset + 3] << 24;

            count -= 4;
            offset += 4;

            k1 *= 0xcc9e2d51;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= 0x1b873593;

            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;
        }

        // tail

        if (count > 0) {
            int k1 = 0;

            switch (count) {
                case 3:
                    k1 ^= (data[offset + 2] & 0xff) << 16;
                // fall through
                case 2:
                    k1 ^= (data[offset + 1] & 0xff) << 8;
                // fall through
                case 1:
                    k1 ^= (data[offset] & 0xff);
                // fall through
                default:
                    k1 *= 0xcc9e2d51;
                    k1 = Integer.rotateLeft(k1, 15);
                    k1 *= 0x1b873593;
                    h1 ^= k1;
            }
        }

        // finalization

        h1 ^= len;

        // finalization mix force all bits of a hash block to avalanche
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        return h1;
    }

}
