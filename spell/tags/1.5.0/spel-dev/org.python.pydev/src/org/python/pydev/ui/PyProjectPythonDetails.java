/*
 * Created on Jan 25, 2005
 *
 * @author Fabio Zadrozny
 */
package org.python.pydev.ui;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.dialogs.PropertyPage;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.core.docutils.StringUtils;
import org.python.pydev.plugin.PydevPlugin;
import org.python.pydev.plugin.nature.PythonNature;
import org.python.pydev.utils.ICallback;

/**
 * @author Fabio Zadrozny
 */
public class PyProjectPythonDetails extends PropertyPage{

    /**
     * This  class provides a way to show to the user the options available to configure a project with the
     * correct interpreter and grammar.
     */
    public static class ProjectInterpreterAndGrammarConfig{
        public Button radioPy;
        public Button radioJy;
        public Combo comboGrammarVersion;
        public Label versionLabel;
        public Combo interpretersChoice;
        private Link interpreterNoteText;
        private SelectionListener selectionListener;
        private ICallback onSelectionChanged;
        private Label interpreterLabel;
        
        public ProjectInterpreterAndGrammarConfig() {
            
        }
        
        /**
         * Optionally, a callback may be passed to be called whenever the selection of the project type changes.
         */
        public ProjectInterpreterAndGrammarConfig(ICallback callback) {
            this.onSelectionChanged = callback;
        }


        public Control doCreateContents(Composite p) {
            Composite topComp= new Composite(p, SWT.NONE);
            GridLayout innerLayout= new GridLayout();
            innerLayout.numColumns= 1;
            innerLayout.marginHeight= 0;
            innerLayout.marginWidth= 0;
            topComp.setLayout(innerLayout);
            GridData gd= new GridData(GridData.FILL_BOTH);
            topComp.setLayoutData(gd);

            
            
            //Project type
            Group group = new Group(topComp, SWT.NONE);
            group.setText("Choose the project type");
            GridLayout layout = new GridLayout();
            layout.horizontalSpacing = 8;
            layout.numColumns = 2;
            group.setLayout(layout);
            gd= new GridData(GridData.FILL_HORIZONTAL);
            group.setLayoutData(gd);

            radioPy = new Button(group, SWT.RADIO | SWT.LEFT);
            radioPy.setText("Python");
            
            radioJy = new Button(group, SWT.RADIO | SWT.LEFT);
            radioJy.setText("Jython");
            
            
            
            //Grammar version
            versionLabel = new Label(topComp, 0);
            versionLabel.setText("Grammar Version");
            gd= new GridData(GridData.FILL_HORIZONTAL);
            versionLabel.setLayoutData(gd);
            
            
            
            comboGrammarVersion = new Combo(topComp, SWT.READ_ONLY);
            comboGrammarVersion.add("2.1");
            comboGrammarVersion.add("2.2");
            comboGrammarVersion.add("2.3");
            comboGrammarVersion.add("2.4");
            comboGrammarVersion.add("2.5");
            comboGrammarVersion.add("2.6");
            comboGrammarVersion.add("3.0");
            
            gd= new GridData(GridData.FILL_HORIZONTAL);
            comboGrammarVersion.setLayoutData(gd);

            
            //Interpreter
            interpreterLabel = new Label(topComp, 0);
            interpreterLabel.setText("Interpreter");
            gd= new GridData(GridData.FILL_HORIZONTAL);
            interpreterLabel.setLayoutData(gd);
            

            //interpreter configured in the project
            final String [] idToConfig = new String[]{"org.python.pydev.ui.pythonpathconf.interpreterPreferencesPagePython"};
            interpretersChoice = new Combo(topComp, SWT.READ_ONLY);
            selectionListener = new SelectionListener(){

                public void widgetDefaultSelected(SelectionEvent e) {
                    
                }

                /**
                 * @param e can be null to force an update.
                 */
                public void widgetSelected(SelectionEvent e) {
                    if(e != null && e.getSource() == radioPy){
                        return; //we'll get 2 notifications: selection of one and deselection of the other, so, let's just treat it once.
                    }
                    
                    IInterpreterManager interpreterManager;
                    
                    if(radioPy.getSelection()){
                        interpreterManager = PydevPlugin.getPythonInterpreterManager();
                    }else{
                        interpreterManager = PydevPlugin.getJythonInterpreterManager();
                    }
                    
                    String[] interpreters = interpreterManager.getInterpreters();
                    if(interpreters.length > 0){
                        ArrayList<String> interpretersWithDefault = new ArrayList<String>();
                        interpretersWithDefault.add(IPythonNature.DEFAULT_INTERPRETER);
                        interpretersWithDefault.addAll(Arrays.asList(interpreters));
                        interpretersChoice.setItems(interpretersWithDefault.toArray(new String[0]));
                        
                        interpretersChoice.setVisible(true);
                        interpreterNoteText.setText("<a>Click here to configure an interpreter not listed.</a>");
                        interpretersChoice.setText(IPythonNature.DEFAULT_INTERPRETER);
                        
                    }else{
                        interpretersChoice.setVisible(false);
                        interpreterNoteText.setText("<a>Please configure an interpreter in the related preferences before proceeding.</a>");
                        
                    }
                    //config which preferences page should be opened!
                    if(interpreterManager.isPython()){
                        idToConfig[0] = "org.python.pydev.ui.pythonpathconf.interpreterPreferencesPagePython";
                    }else{
                        idToConfig[0] = "org.python.pydev.ui.pythonpathconf.interpreterPreferencesPageJython";
                    }
                    if(onSelectionChanged != null){
                        try {
                            onSelectionChanged.call(null);
                        } catch (Exception e1) {
                            PydevPlugin.log(e1);
                        }
                    }
                }
            };
            
            gd= new GridData(GridData.FILL_HORIZONTAL);
            interpretersChoice.setLayoutData(gd);
            radioPy.addSelectionListener(selectionListener);
            radioJy.addSelectionListener(selectionListener);
            
            
            interpreterNoteText = new Link(topComp, SWT.LEFT | SWT.WRAP);
            gd= new GridData(GridData.FILL_HORIZONTAL);
            interpreterNoteText.setLayoutData(gd);

            interpreterNoteText.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, idToConfig[0], null, null);
                    dialog.open(); 
                    //just to re-update it again
                    selectionListener.widgetSelected(null);
                }

                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });


            return topComp;
        }
        
        
        /**
         * @return a string as specified in the constants in IPythonNature
         * @see IPythonNature#PYTHON_VERSION_XXX 
         * @see IPythonNature#JYTHON_VERSION_XXX
         */
        public String getSelectedPythonOrJythonAndGrammarVersion() {
            if(radioPy.getSelection()){
                return "python "+comboGrammarVersion.getText();
            }
            if(radioJy.getSelection()){
                return "jython "+comboGrammarVersion.getText();
            }
            throw new RuntimeException("Some radio must be selected");
        }
        
        public String getProjectInterpreter(){
            if(interpretersChoice.isVisible() == false){
                return null;
            }
            return interpretersChoice.getText();
        }

        public void setDefaultSelection() {
            radioPy.setSelection(true);
            comboGrammarVersion.setText("2.6");
            //Just to update things
            this.selectionListener.widgetSelected(null);
        }

    }
    
    /**
     * The element.
     */
    public IAdaptable element;
    public ProjectInterpreterAndGrammarConfig projectConfig = new ProjectInterpreterAndGrammarConfig();

    /*
     *  (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
     */
    public IAdaptable getElement() {
        return element;
    }
    /**
     * Sets the element that owns properties shown on this page.
     * 
     * @param element the element
     */
    public void setElement(IAdaptable element) {
        this.element = element;
    }
    
    public IProject getProject(){
        return (IProject)getElement().getAdapter(IProject.class);
    }

    @Override
    public Control createContents(Composite p) {
        Control contents = projectConfig.doCreateContents(p);
        setSelected();
        return contents;
    }
    
    private void setSelected() {
        PythonNature pythonNature = PythonNature.getPythonNature(getProject());
        try {
            //Set whether it's Python/Jython
            String version = pythonNature.getVersion();
            if(IPythonNature.Versions.ALL_PYTHON_VERSIONS.contains(version)){
                projectConfig.radioPy.setSelection(true);
                
            }else if(IPythonNature.Versions.ALL_JYTHON_VERSIONS.contains(version)){
                projectConfig.radioJy.setSelection(true);
            }
            
            //We must set the grammar version too (that's from a string in the format "Python 2.4" and we only want
            //the version).
            String v = StringUtils.split(version, ' ')[1];
            projectConfig.comboGrammarVersion.setText(v);
            
            //Update interpreter
            projectConfig.selectionListener.widgetSelected(null);
            String configuredInterpreter = pythonNature.getProjectInterpreter(false);
            if(configuredInterpreter != null){
                projectConfig.interpretersChoice.setText(configuredInterpreter);
            }
            
        } catch (CoreException e) {
            PydevPlugin.log(e);
        }
    }

    
    protected void performApply() {
        doIt();
    }
    
    public boolean performOk() {
        return doIt();
    }

    private boolean doIt() {
        IProject project = getProject();
        
        if (project!= null) {
            PythonNature pythonNature = PythonNature.getPythonNature(project);
            
            try {
                String projectInterpreter = projectConfig.getProjectInterpreter();
                if(projectInterpreter == null){
                    return false;
                }
                pythonNature.setVersion(projectConfig.getSelectedPythonOrJythonAndGrammarVersion(), projectInterpreter);
            } catch (CoreException e) {
                PydevPlugin.log(e);
            }
        }
        return true;
    }
}