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
package net.freechoice.service;

import net.freechoice.application.security.IEncryptor;
import net.freechoice.application.security.IValidator;
import net.freechoice.application.security.impl.PasswordEncryptor;

/**
 * 
 * @author BowenCai
 *
 */
public final class EncryptionService {

	public EncryptionService(){}
	
	/**
	 * PasswordEncryptor is thread-safe
	 */
	private static IEncryptor encryptor = new PasswordEncryptor();
	private static IValidator validator = new PasswordEncryptor();
	
	public static final String transformPassword(final String password) {

		return encryptor.transformPassword(password);
	}
	
	/**
	 * do not miss place the two parameters
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 */
	public static final boolean validatePasswords(final String newPassword, final String oldPassword) {
		return validator.validatePasswords(newPassword, oldPassword);
	}

}





