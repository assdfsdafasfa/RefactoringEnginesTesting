/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.test.refactoring;

import java.io.*;
import java.util.ArrayList;
import org.netbeans.junit.NbTestCase;
import org.netbeans.junit.diff.LineDiff;
import org.netbeans.junit.ide.ProjectSupport;




/** LogTestCase
 * @author Jan Becicka
 */
public class LogTestCase extends NbTestCase {

    /**
     * state - true - testing
     *       - false - generating goldenfiles
     */
    public static boolean CREATE_GOLDENFILES=false;

    static {
        if (System.getProperty("create.goldenfiles") != null && System.getProperty("create.goldenfiles").equals("true")) {
           CREATE_GOLDENFILES=true;
        }
    }
    /** directory, where the golden and .diff files resides
     */
    protected File classPathWorkDir;
    /** test will generate this file
     */
    protected File refFile;
    File logFile;

    protected PrintStream log = null;
    protected PrintStream ref = null;
    protected PrintStream golden = null;

    public LogTestCase(java.lang.String testName) {
        super(testName);
        //CREATE_GOLDENFILES=true; //regenerate goldenfiles
    }

    /** sets the PrintStreams
     */
    protected void setUp() {
        prepareProject();
        try {
            //logs and refs
            refFile = new File(getWorkDir(), getName() + ".ref");
            System.out.println("refFile: " + getWorkDir() + " " + getName() + ".ref");
            logFile = new File(getWorkDir(), getName() + ".log");
            System.out.println("logFile: " + getWorkDir()+ " " + getName() + ".log");
            ref = new PrintStream(new BufferedOutputStream(new FileOutputStream(refFile)));
            if (CREATE_GOLDENFILES) { //generates golden files
                File f;
                //generate goldenfile name
                f=getDataDir();
                ArrayList names=new ArrayList();
                names.add("goldenfiles");
                while (!f.getName().equals("test")) {
                    if (!f.getName().equals("sys") && !f.getName().equals("work") &&!f.getName().equals("tests")) {
                        names.add(f.getName());
                    }
                    f=f.getParentFile();
                }
                for (int i=names.size()-1;i > -1;i--) {
                    f=new File(f,(String)(names.get(i)));
                }
                f=new File(f, getClass().getName().replace('.', File.separatorChar));
                f=new File(f, getName()+".pass");
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                golden=new PrintStream(new BufferedOutputStream(new FileOutputStream(f)));
                log("Passive mode: generate golden file into "+f.getAbsolutePath());
            }
            //logFileStructure(classPathWorkDir);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.toString(), false);
        }
    }

    public void prepareProject() {//default - override for another projects
        ProjectSupport.openProject(new File(getDataDir(), "projects/default"));
        classPathWorkDir=new File(getDataDir(), "projects.default.src".replace('.', File.separatorChar));
    }

    public void openProject(String name) {
        ProjectSupport.openProject(new File(getDataDir(),"projects"+File.separator+name));
    }

    public void logFileStructure(File file) {
        File[] files=file.listFiles();
        for (int i=0;i < files.length;i++) {
            if (files[i].isDirectory()) {
                logFileStructure(files[i]);
            } else {
                log(files[i].getAbsolutePath());
                log(files[i]);
            }
        }
    }

    public void log(String s) {
        getLogStream().println(s);
    }

    public void log(Object o) {
        getLogStream().println(o);
    }

    public PrintStream getLogStream() {
        if (log == null) {
            try {
                log = new PrintStream(new BufferedOutputStream(new FileOutputStream(logFile)));
            } catch (Exception ex) {
                ex.printStackTrace();
                assertTrue(ex.toString(), false);
            }
        }
        return log;
    }

    public void log(File file) {
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while ((line=br.readLine()) != null) {
                log(line);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace(getLogStream());
        }
    }

    public void ref(String s) {
        ref.println(s);
        if (CREATE_GOLDENFILES) {
            golden.println(s);
        }
    }

    public void ref(Object o) {
        ref.println(o.toString());
        if (CREATE_GOLDENFILES) {
            golden.println(o.toString());
        }
    }

    public void ref(File file) {
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while ((line=br.readLine()) != null) {
                ref(line);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace(getLogStream());
        }
    }


    /** sets the PrintStreams
     */
    protected void tearDown() {
        ref.close();
        if (log != null) {
            log.close();
        }
        if (CREATE_GOLDENFILES && golden != null) {
            golden.close();
            assertTrue("Passive mode", false);
        } else {
            try {
				// Kely comment this line
                //assertFile("Golden file differs ", refFile, getGoldenFile(), getWorkDir(), new LineDiff());
            } catch (Exception ex) {
                ex.printStackTrace();
                assertTrue(ex.toString(), false);
            }
        }
    }
}

