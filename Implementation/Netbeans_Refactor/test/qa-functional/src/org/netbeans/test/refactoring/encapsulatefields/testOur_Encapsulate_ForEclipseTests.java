package org.netbeans.test.refactoring.encapsulatefields;
import java.lang.reflect.Modifier;
import junit.framework.AssertionFailedError;
import junit.textui.TestRunner;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.modules.javacore.internalapi.JavaMetamodel;
import org.netbeans.junit.ide.ProjectSupport;
import java.util.ArrayList;
import java.io.*;

public class testOur_Encapsulate_ForEclipseTests extends EncapsulateTestCase {
    String packages[] = new String[] {"base_in", "invalid", "object_in"} ; //, "static_in", };
    String outPackages[] = new String[] {"base_outNB", "invalid_NB", "object_outNB"}; //, "static_outNB"};
    String fields[] = new String[] {"field", "field", "field"}; //, "x"};
    ArrayList []list = new ArrayList [packages.length];
    
    public testOur_Encapsulate_ForEclipseTests(String name) throws Exception {
        super(name);
        setArgsFromFile(testOur_Encapsulate_ForEclipseTests.class.getSimpleName(), "eclipseTest");
    }
    
    public void prepareProject() {
        try {
            int totalNumOfTests=0;
            for(int i=0; i < packages.length; i++){                
                list[i] = copyFiles(workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + packages[i], refactoringName + "." + subDirectoryName.replace("\\\\", "."));                
                totalNumOfTests +=list[i].size();
                numTotalCompiledTests +=list[i].size();
                numTotalInputTests +=list[i].size(); 
            }                       
            initializeResults(totalNumOfTests, true);            
        } catch (Exception e){
            System.out.println("Error "+ e);
        }
        ProjectSupport.openProject(new File(getDataDir(), "projects/" + projectName));
        classPathWorkDir=new File(getDataDir(), ("projects." + projectName + ".src").replace('.', File.separatorChar));
    }
    
    public static NbTestSuite suite() {
        return new NbTestSuite(testOur_Encapsulate_ForEclipseTests.class);
    }    
    
    public void test_1() throws Exception {
        currentNumOfTest = 0;
        for (int i=0; i < packages.length; i++){               
            System.out.println("PackageName is: "+ packages[i]);
            for (int j=0; j < list[i].size(); j++){
                try { 
                    File f = (File)list[i].get(j);                
                    String name = f.getName().substring(0, f.getName().indexOf(".java"));                                
                    tests[currentNumOfTest]= name;                     
                    System.out.println("currentNumOfTest: " + currentNumOfTest + "   tests[currentNumOfTest]: "+ tests[currentNumOfTest]);                    
                    encapsulateField(refactoringName + "." + subDirectoryName.replace("\\\\", ".") + "." + packages[i] + "." + name, fields[i], null, null, 0x00000000, Modifier.PRIVATE, true, false);

                    System.out.println("Ok j: "+ j + " name: "+ name);
                    currentNumOfTest++;
                } catch (AssertionFailedError e) {
                    errorMessages[currentNumOfTest]=e.toString();
                    System.out.println("fail test ***************************" + i + " error " + e);
                    currentNumOfTest++;
                } catch (Exception e){
                    errorMessages[currentNumOfTest]=e.toString();
                    System.out.println("fail test ***************************" + i + " error " + e);
                    currentNumOfTest++;
                }                
            }            
       }
    }
    
   protected void tearDown() {        
        try {
            int numOfTest=0;    
            for (int j=0; j < packages.length; j++)  {
                for (int i = 0; i < list[j].size(); i++) {
                    File f = (File)list[j].get(i);                                    
                    String srcDir = workspaceDir + File.separator + refactoringName + File.separator + subDirectoryName + File.separator + packages[j];                    
                    String dstDir = workDirectoryForEclipse + File.separator + subDirectoryName + File.separator + outPackages[j];
                    MoveFiles_Or_DeleteFiles (srcDir, dstDir, f.getName(), statusOfTests[numOfTest], errorMessages[numOfTest], 2);
                    numOfTest++;
                }
            }                    
            printSummaryCounters();
        } catch (IOException e){
            System.out.println("error is: "+ e);
        }
    }
    
}
