/*
 * Created on Oct 25, 2004
 *
 * @author Fabio Zadrozny
 */
package org.python.pydev.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.python.pydev.builder.pycremover.PycRemoverBuilderVisitor;
import org.python.pydev.builder.syntaxchecker.PySyntaxChecker;
import org.python.pydev.builder.todo.PyTodoVisitor;
import org.python.pydev.core.ExtensionHelper;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.core.IPythonPathNature;
import org.python.pydev.core.REF;
import org.python.pydev.core.structure.FastStringBuffer;
import org.python.pydev.editor.codecompletion.revisited.PyCodeCompletionVisitor;
import org.python.pydev.editor.codecompletion.revisited.PythonPathHelper;
import org.python.pydev.plugin.nature.PythonNature;
import org.python.pydev.utils.PyFileListing;

/**
 * This builder only passes through python files
 * 
 * @author Fabio Zadrozny
 */
public class PyDevBuilder extends IncrementalProjectBuilder {

    private static final boolean DEBUG = false;

    /**
     * 
     * @return a list of visitors for building the application.
     */
    @SuppressWarnings("unchecked")
    public List<PyDevBuilderVisitor> getVisitors() {
        List<PyDevBuilderVisitor> list = new ArrayList<PyDevBuilderVisitor>();
        list.add(new PyTodoVisitor());
        //list.add(new PyLintVisitor());
        list.add(new PyCodeCompletionVisitor());
        list.add(new PycRemoverBuilderVisitor());
        list.add(new PySyntaxChecker());

        list.addAll(ExtensionHelper.getParticipants(ExtensionHelper.PYDEV_BUILDER));
        return list;
    }




    /**
     * Builds the project.
     * 
     * @see org.eclipse.core.internal.events InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
     */
    @SuppressWarnings("unchecked")
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {

        if (PyDevBuilderPrefPage.usePydevBuilders() == false)
            return null;

        if (kind == IncrementalProjectBuilder.FULL_BUILD) {
            // Do a Full Build: Use a ResourceVisitor to process the tree.
            performFullBuild(monitor);

        } else {
            // Build it with a delta
            IResourceDelta delta = getDelta(getProject());

            if (delta == null) {
                //no delta (unspecified changes?... let's do a full build...)
                performFullBuild(monitor);
                
            } else {
                // ok, we have a delta
                // first step is just counting them
                PyDevDeltaCounter counterVisitor = new PyDevDeltaCounter();
                delta.accept(counterVisitor);
                
                List<PyDevBuilderVisitor> visitors = getVisitors();
                
                //sort by priority
                Collections.sort(visitors); 

                PydevGrouperVisitor grouperVisitor = new PydevGrouperVisitor(visitors, monitor, counterVisitor.getNVisited());
                notifyVisitingWillStart(visitors, monitor, false, null);
                try {
					delta.accept(grouperVisitor);
				} finally {
					notifyVisitingEnded(visitors, monitor);
				}
            }
        }
        return null;
    }

    /**
     * Processes all python files.
     * 
     * @param monitor
     */
    private void performFullBuild(IProgressMonitor monitor) throws CoreException {
        IProject project = getProject();

        //we need the project...
        if (project != null) {
            IPythonNature nature = PythonNature.getPythonNature(project);
            
            //and the nature...
            if (nature != null && nature.startRequests()){
                
                try{
                    IPythonPathNature pythonPathNature = nature.getPythonPathNature();
                    pythonPathNature.getProjectSourcePath(); //this is just to update the paths (in case the project name has just changed)
                    
                    List<IFile> resourcesToParse = new ArrayList<IFile>();
        
                    List<PyDevBuilderVisitor> visitors = getVisitors();
                    notifyVisitingWillStart(visitors, monitor, true, nature);
        
                    monitor.beginTask("Building...", (visitors.size() * 100) + 30);
        
                    IResource[] members = project.members();
        
                    if (members != null) {
                        // get all the python files to get information.
                        for (int i = 0; i < members.length; i++) {
                            try {
                                IResource member = members[i];
                                if (member == null) {
                                    continue;
                                }
        
                                if (member.getType() == IResource.FILE) {
                                    addToResourcesToParse(resourcesToParse, (IFile)member, nature);
                                    
                                } else if (member.getType() == IResource.FOLDER) {
                                    //if it is a folder, let's get all python files that are beneath it
                                    //the heuristics to know if we have to analyze them are the same we have
                                    //for a single file
                                    List<IFile> l = PyFileListing.getAllIFilesBelow((IFolder) member);
                                    
                                    for (Iterator<IFile> iter = l.iterator(); iter.hasNext();) {
                                        IFile element = iter.next();
                                        if (element != null) {
                                            addToResourcesToParse(resourcesToParse, element, nature);
                                        }
                                    }
                                } else {
                                    if (DEBUG){
                                        System.out.println("Unknown type: "+member.getType());
                                    }
                                }
                            } catch (Exception e) {
                                // that's ok...
                            }
                        }
                        monitor.worked(30);
                        buildResources(resourcesToParse, monitor, visitors);
                    }
                    notifyVisitingEnded(visitors, monitor);
                }finally{
                    nature.endRequests();
                }
            }
        }
        monitor.done();

    }

    private void notifyVisitingWillStart(List<PyDevBuilderVisitor> visitors, IProgressMonitor monitor, boolean isFullBuild, IPythonNature nature) {
        for (PyDevBuilderVisitor visitor : visitors) {
            visitor.visitingWillStart(monitor, isFullBuild, nature);
        }
    }

    private void notifyVisitingEnded(List<PyDevBuilderVisitor> visitors, IProgressMonitor monitor) {
        for (PyDevBuilderVisitor visitor : visitors) {
            visitor.visitingEnded(monitor);
        }
    }
    



    /**
     * @param resourcesToParse the list where the resource may be added
     * @param member the resource we are adding
     * @param nature the nature associated to the resource
     */
    private void addToResourcesToParse(List<IFile> resourcesToParse, IFile member, IPythonNature nature) {
        //analyze it only if it is a valid source file 
        String fileExtension = member.getFileExtension();
        if(DEBUG){
            System.out.println("Checking name:'"+member.getName()+"' projPath:'"+member.getProjectRelativePath()+ "' ext:'"+fileExtension+"'");
            System.out.println("loc:'"+member.getLocation()+"' rawLoc:'"+member.getRawLocation()+"'");
            
        }
        if (fileExtension != null && PythonPathHelper.isValidSourceFile("."+fileExtension)) {
            if(DEBUG){
                System.out.println("Adding resource to parse:"+member.getProjectRelativePath());
            }
            resourcesToParse.add(member);
        }
    }


    /**
     * Default implementation. Visits each resource once at a time. May be overriden if a better implementation is needed.
     * 
     * @param resourcesToParse list of resources from project that are python files.
     * @param monitor
     * @param visitors
     */
    public void buildResources(List<IFile> resourcesToParse, IProgressMonitor monitor, List<PyDevBuilderVisitor> visitors) {

        // we have 100 units here
        double inc = (visitors.size() * 100) / (double) resourcesToParse.size();

        double total = 0;
        int totalResources = resourcesToParse.size();
        int i = 0;
        
        FastStringBuffer bufferToCreateString = new FastStringBuffer();

        for (Iterator<IFile> iter = resourcesToParse.iterator(); iter.hasNext() && monitor.isCanceled() == false;) {
            i += 1;
            total += inc;
            IFile r = iter.next();
            IPythonNature nature = PythonNature.getPythonNature(r);
            if (nature == null){
                continue;
            }
            if(!nature.startRequests()){
                continue;
            }
            try{
                if(!nature.isResourceInPythonpath(r)){
                    continue; // we only analyze resources that are in the pythonpath
                }
                IDocument doc = REF.getDocFromResource(r);
                
                HashMap<String, Object> memo = new HashMap<String, Object>();
                memo.put(PyDevBuilderVisitor.IS_FULL_BUILD, true); //mark it as full build
                
                if(doc != null){ //might be out of synch
                    for (Iterator<PyDevBuilderVisitor> it = visitors.iterator(); it.hasNext() && monitor.isCanceled() == false;) {
    
                        PyDevBuilderVisitor visitor = it.next();
                        visitor.memo = memo; //setting the memo must be the first thing.
        
                        communicateProgress(monitor, totalResources, i, r, visitor, bufferToCreateString);
                        
                        //on a full build, all visits are as some add...
                        visitor.visitAddedResource(r, doc, monitor);
                    }
        
                    if (total > 1) {
                        monitor.worked((int) total);
                        total -= (int) total;
                    }
                }
            }finally{
                nature.endRequests();
            }
        }
    }

    /**
     * Used so that we can communicate the progress to the user
     * 
     * @param bufferToCreateString: this is a buffer that's emptied and used to create the string to be shown to the
     * user with the progress.
     */
    public static void communicateProgress(IProgressMonitor monitor, int totalResources, int i, IResource r, 
            PyDevBuilderVisitor visitor, FastStringBuffer bufferToCreateString) {
        if(monitor != null){
            bufferToCreateString.clear();
            bufferToCreateString.append("Pydev: Analyzing ");
            bufferToCreateString.append(i);
            bufferToCreateString.append(" of ");
            bufferToCreateString.append(totalResources);
            bufferToCreateString.append(" (");
            bufferToCreateString.append(r.getName());
            bufferToCreateString.append(")");
       
            //in this case the visitor does not have the progress and therefore does not communicate the progress
            String name = bufferToCreateString.toString();
            monitor.subTask(name);
        }
    }

}
