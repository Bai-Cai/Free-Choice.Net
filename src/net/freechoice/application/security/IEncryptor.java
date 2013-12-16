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
package net.freechoice.application.security;




/**
 * @author BowenCai
 *
 */

public interface IEncryptor {

    static final String PBKDF2_ALGO = "PBKDF2WithHmacSHA1";

    static final int PBKDF2_LOOP = 1024;
    
	public String transformPassword(final String password);

}
