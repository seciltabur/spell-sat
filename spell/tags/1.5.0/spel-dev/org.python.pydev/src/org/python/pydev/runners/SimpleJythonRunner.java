/*
 * Created on 05/08/2005
 */
package org.python.pydev.runners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.python.copiedfromeclipsesrc.JDTNotAvailableException;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.core.Tuple;
import org.python.pydev.core.docutils.StringUtils;
import org.python.pydev.core.structure.FastStringBuffer;
import org.python.pydev.plugin.PydevPlugin;
import org.python.pydev.plugin.preferences.PydevPrefs;
import org.python.pydev.ui.pythonpathconf.InterpreterInfo;

public class SimpleJythonRunner extends SimpleRunner{
    
    /**
     * Error risen when java is not available to the jython environment
     * 
     * @author Fabio
     */
    @SuppressWarnings("serial")
    public static class JavaNotConfiguredException extends RuntimeException{

        public JavaNotConfiguredException(String string) {
            super(string);
        }
        
    }
    
    public Tuple<String,String> runAndGetOutputWithJar(String script, String jythonJar, String args, 
            File workingDir, IProject project, IProgressMonitor monitor) {
//        File javaExecutable = JavaVmLocationFinder.findDefaultJavaExecutable();
//        if(javaExecutable == null){
//            throw new JavaNotConfiguredException(
//                    "Error: the java environment must be configured before jython.\n\n" +
//                    "Please make sure that the java executable to be\n" +
//                    "used is correctly configured in the preferences at:\n\n" +
//                    "Java > Installed JREs.");
//        }

        //return runAndGetOutputWithJar(javaExecutable, script, jythonJar, args, workingDir, project, monitor, null);
    	return null;
    }
    
    public Tuple<String,String> runAndGetOutputWithJar(File javaExecutable, String script, String jythonJar, 
            String args, File workingDir, IProject project, IProgressMonitor monitor, String additionalPythonpath) {
        //"C:\Program Files\Java\jdk1.5.0_04\bin\java.exe" "-Dpython.home=C:\bin\jython21" 
        //-classpath "C:\bin\jython21\jython.jar;%CLASSPATH%" org.python.util.jython %ARGS%
        //used just for getting info without any classpath nor pythonpath
        
        try {
            
            String javaLoc = javaExecutable.getCanonicalPath();
            String[] s;
            
            //In Jython 2.5b0, if we don't set python.home, it won't be able to calculate the correct PYTHONPATH
            //(see http://bugs.jython.org/issue1214 )
            
            String pythonHome = new File(jythonJar).getParent().toString();
            
            if(additionalPythonpath != null){
                jythonJar += SimpleRunner.getPythonPathSeparator();
                jythonJar += additionalPythonpath;
                s = new String[]{
                        javaLoc,
                        "-Dpython.path="+ additionalPythonpath,
                        "-Dpython.home="+ pythonHome,
                        "-classpath",
                        jythonJar,
                        "org.python.util.jython" 
                        ,script
                };
            }else{
                s = new String[]{
                    javaLoc,
                    "-Dpython.home="+ pythonHome,
                    "-classpath",
                    jythonJar,
                    "org.python.util.jython" 
                    ,script
                };
            }
            
            return runAndGetOutput(s, workingDir, project, monitor);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }

    public static String[] makeExecutableCommandStr(String jythonJar, String script, String basePythonPath, String ... args) throws IOException, JDTNotAvailableException {
       return makeExecutableCommandStrWithVMArgs(jythonJar, script, basePythonPath, "", args); 
    }
    
    /**
     * @param script
     * @return
     * @throws IOException
     */
    public static String[] makeExecutableCommandStrWithVMArgs(String jythonJar, String script, String basePythonPath, String vmArgs, 
            String ... args) throws IOException, JDTNotAvailableException {
        
//        IInterpreterManager interpreterManager = PydevPlugin.getJythonInterpreterManager();
//        String javaLoc = JavaVmLocationFinder.findDefaultJavaExecutable().getCanonicalPath();
//        
//        File file = new File(javaLoc);
//        if(file.exists() == false ){
//            throw new RuntimeException("The java location found does not exist. "+javaLoc);
//        }
//        if(file.isDirectory() == true){
//            throw new RuntimeException("The java location found is a directory. "+javaLoc);
//        }
//
//        
//        
//        if(!new File(jythonJar).exists()){
//            throw new RuntimeException(StringUtils.format("Error. The default configured interpreter: %s is does not exist!", jythonJar));
//        }
//        InterpreterInfo info = (InterpreterInfo) interpreterManager.getInterpreterInfo(jythonJar, new NullProgressMonitor());
//
//        //pythonpath is: base path + libs path.
//        String libs = SimpleRunner.makePythonPathEnvFromPaths(info.libs);
//        FastStringBuffer jythonPath = new FastStringBuffer(basePythonPath, 128);
//        String pathSeparator = SimpleRunner.getPythonPathSeparator();
//        if(jythonPath.length() != 0){
//            jythonPath.append(pathSeparator); 
//        }
//        jythonPath.append(libs);
//        
//        
//        //may have the dir or be null
//        String cacheDir = null;
//        try{
//            cacheDir = PydevPrefs.getChainedPrefStore().getString(IInterpreterManager.JYTHON_CACHE_DIR);
//        }catch(NullPointerException e){
//            //this may happen while running the tests... it should be ok.
//            cacheDir = null;
//        }
//        if(cacheDir != null && cacheDir.trim().length()==0){
//            cacheDir = null;
//        }
//        if(cacheDir != null){
//            cacheDir = "-Dpython.cachedir="+ cacheDir.trim();
//        }
//        
//        String[] s;
//        if(cacheDir != null){
//            s = new String[]{
//                javaLoc ,
//                cacheDir,
//                "-Dpython.path="+ jythonPath.toString(),
//                "-classpath",
//                jythonJar+pathSeparator+jythonPath,
//                vmArgs,
//                "org.python.util.jython",
//                script
//            };
//        }else{
//            s = new String[]{
//                javaLoc ,
//                //cacheDir, no cache dir if it's not available
//                "-Dpython.path="+ jythonPath.toString(),
//                "-classpath",
//                jythonJar+pathSeparator+jythonPath,
//                vmArgs,
//                "org.python.util.jython",
//                script
//            };
//            
//        }
//        
//        
        List<String> asList = new ArrayList<String>(/*Arrays.asList(s)*/);
        //asList.addAll(Arrays.asList(args));
        return asList.toArray(new String[0]);
    }

}

