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


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * @author BowenCai
 *
 */
public final class ClassFinder {


    public final static List<Class<?>> find(final String scannedPackage) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final String scannedPath = scannedPackage.replace(".", File.separator);

//System.out.println(scannedPath);
        
        final Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(scannedPath);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(
            		"Unable to get resources from path " + scannedPath 
            		+ "Are you sure the given "+ scannedPackage+ " package exists?"), e);
        }
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        while (resources.hasMoreElements()) {
            final File file = new File(resources.nextElement().getFile());
            classes.addAll(find(file, new String()));
        }
        return classes;
    }

    private final static List<Class<?>> find(final File file, final String scannedPackage) {
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        final String resource = scannedPackage + '.' + file.getName();

//System.out.println("resource " + resource + "  isdir  " + file.isDirectory() );

        if (file.isDirectory()) {
    

            for (File nestedFile : file.listFiles()) {
                classes.addAll(find(nestedFile, resource));
            }
        } else if (resource.endsWith(".class")) {
            final int beginIndex = 1;
            final int endIndex = resource.length() - ".class".length();
            final String className = resource.substring(beginIndex, endIndex);
//System.out.println("className " + className);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            	ignore.printStackTrace();
            }
        }
        return classes;
    }

}
