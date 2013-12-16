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
package net.freechoice.application.security.impl;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import net.freechoice.application.security.IEncryptor;
import net.freechoice.application.security.IValidator;

import net.freechoice.util.Base64;

/**
 * @author BowenCai
 *
 */
public class PasswordEncryptor implements IEncryptor, IValidator {

    /**
     * length of salt bytes
     */
    private static final int SALT_BYTE_LEN = 12;
    
    /**
     * length of hashed password bytes
     */
    private static final int HASH_BYTE_LEN = 24;
    /**
     * length of base64 encoded hashed password
     */
    private static final int HASH_STR_LEN = HASH_BYTE_LEN * 4 / 3;
    private static final int HASH_BIT_LEN = HASH_BYTE_LEN * 8;
    
    // (24 + 12) * 4 / 3 = 48
    private static final int PSW_BYTE_LEN = 
    							(HASH_BYTE_LEN + SALT_BYTE_LEN ) * 4 / 3;
    
    
    SecretKeyFactory secretKeyFactory;
	

	public PasswordEncryptor() {
		try {
			secretKeyFactory  = SecretKeyFactory.getInstance(PBKDF2_ALGO);
		} catch (NoSuchAlgorithmException e) {
			// exception impossible
			e.printStackTrace();
		}
	}
    /**
	 * 
	 * @param password
	 * @return hashed password with salt
	 */
	public final String transformPassword(final String password) {
		
		final byte[] salt = new byte[SALT_BYTE_LEN];
		fillRandomBytes(salt);

//System.err.println("init salt:" + salt);

		byte[] hashedPsw = null;
		
		/**
		 * (24 + 12) * 4 / 3 = 48
		 */
		StringBuilder hashedPswWithSalt = new StringBuilder(PSW_BYTE_LEN);
		
	    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(),
        								 salt,
        								 PBKDF2_LOOP,
        								 HASH_BIT_LEN);

        try {
	        hashedPsw = secretKeyFactory.generateSecret(spec).getEncoded();
		} catch (Exception e) {
			// exception impossible
			e.printStackTrace();
		}

		hashedPswWithSalt.append(Base64.encodeToString(hashedPsw));
		hashedPswWithSalt.append(Base64.encodeToString(salt));
		
		return hashedPswWithSalt.toString();
	}
	
	/**
	 * 
	 * @param incomingPsw password to be validated
	 * @param dbPsw	hashed password with salt, from database
	 * @return
	 */
	public final boolean validatePasswords(final String incomingPsw,
											final String dbPsw) {
		try {
		String strOldPsw = dbPsw.substring(0, HASH_STR_LEN);
		String strSalt = dbPsw.substring(HASH_STR_LEN);
		
		byte[] salt = Base64.decodeFromString(strSalt);
		byte[] oldPsw = Base64.decodeFromString(strOldPsw);

//System.err.println("extracted:" + salt);

	    PBEKeySpec spec = new PBEKeySpec(incomingPsw.toCharArray(),
										salt,
										PBKDF2_LOOP,
										HASH_BIT_LEN);
		
		byte[] hashedPsw = null;

			hashedPsw = secretKeyFactory.generateSecret(spec).getEncoded();

			return isEqualBytes(hashedPsw, oldPsw);
		} catch (Exception e) {
			// NoSualgorithmException 				-> impossible
			// InvalidKeyException			 		-> return false
			//IllegalArgumentException
			//the salt parameter must not be empty 	-> return false
			e.printStackTrace();
			return false;
		}
//System.err.println("strOldPsw:" + strOldPsw);
//System.err.println("hashedPsw:" + Base64.encodeToString(hashedPsw));
	}

	/**
	 * fille byte array with random bytes
	 * @param bytes
	 */
	private final void fillRandomBytes(byte[] bytes) {
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < bytes.length;)
			for (int rnd = random.nextInt(), 
				n = Math.min(bytes.length - i, 4);
									n-- > 0;
									rnd >>= 8){
				
				bytes[i++] = (byte) rnd;
			}
	}
	
    private boolean isEqualBytes(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }
    
}
