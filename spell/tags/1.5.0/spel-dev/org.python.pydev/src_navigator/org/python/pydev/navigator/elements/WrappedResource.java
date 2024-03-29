package org.python.pydev.navigator.elements;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IContributorResourceAdapter;

/**
 * This class represents a resource that is wrapped for the python model.
 * 
 * @author Fabio
 *
 * @param <X>
 */
public class WrappedResource<X extends IResource> implements IWrappedResource, IContributorResourceAdapter, IAdaptable{

    protected IWrappedResource parentElement;
    protected X actualObject;
    protected PythonSourceFolder pythonSourceFolder;
    protected int rank;

    public WrappedResource(IWrappedResource parentElement, X actualObject, PythonSourceFolder pythonSourceFolder, int rank) {
        this.parentElement = parentElement;
        this.actualObject = actualObject;
        this.pythonSourceFolder = pythonSourceFolder;
        this.pythonSourceFolder.addChild(this);
        this.rank = rank;
    }
    
    public X getActualObject() {
        return actualObject;
    }

    public Object getParentElement() {
        return parentElement;
    }

    public PythonSourceFolder getSourceFolder() {
        return pythonSourceFolder;
    }
    
    public int getRank() {
        return rank;
    }

    public IResource getAdaptedResource(IAdaptable adaptable) {
        return (IResource) getActualObject();
    }

    public boolean equals(Object other) {
        if(other instanceof IWrappedResource){
            if(other == this){
                return true;
            }
            IWrappedResource w = (IWrappedResource) other;
            return this.actualObject.equals(w.getActualObject());
        }
        return false;
        
//now returns always false because it could generate null things in the search page... the reason is that when the
//decorator manager had an update and passed in the search page, it thought that a file/folder was the python file/folder,
//and then, later when it tried to update it with that info, it ended up removing the element because it didn't know how
//to handle it.
//
// -- and this was also not a correct equals, because other.equals(this) would not return true as this was returning
// (basically we can't compare apples to oranges)
//        return actualObject.equals(other);
    }

    @Override
    public int hashCode() {
        return this.getActualObject().hashCode();
    }
    
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if(adapter == IContributorResourceAdapter.class){
            return this;
        }
        Object ret = ((IResource)this.getActualObject()).getAdapter(adapter);
        return ret;
    }


}
