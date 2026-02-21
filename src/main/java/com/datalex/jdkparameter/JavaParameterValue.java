package com.datalex.jdkparameter;

import hudson.model.AbstractBuild;
import hudson.model.JDK;
import hudson.model.ParameterValue;
import hudson.tasks.BuildWrapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: barisbatiege
 * Date: 6/14/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class JavaParameterValue extends ParameterValue {

    private static final Logger LOGGER = Logger.getLogger(JavaParameterValue.class.getName());

    private String selectedJDK;

    @DataBoundConstructor
    public JavaParameterValue(String name, String description, String selectedJDK) {
        super(name, description);
        this.selectedJDK = selectedJDK;
    }

    public String getSelectedJDK() {
        return selectedJDK;
    }

    public void setSelectedJDK(String selectedJDK) {
        this.selectedJDK = selectedJDK;
    }

    @Override
    public BuildWrapper createBuildWrapper(AbstractBuild<?, ?> build) {
        JDK selected = null;
        String originalJDK = null;
        boolean jdkIsAvailable = false;
        selected = new JDK(JavaParameterDefinition.DEFAULT_JDK, null);

        for (JDK jdk : jenkins.model.Jenkins.getInstance().getJDKs()) {
            if (jdk.getName().equalsIgnoreCase(selectedJDK)) {
                selected = jdk;
                jdkIsAvailable = true;
                break;
            }
        }

        if (selectedJDK.equals(JavaParameterDefinition.DEFAULT_JDK)) {
            jdkIsAvailable = true;
        }

        try {
            originalJDK = build.getProject().getJDK() == null
                    ? JavaParameterDefinition.DEFAULT_JDK
                    : build.getProject().getJDK().getName();
            build.getProject().setJDK(selected);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[JDK Parameter]: Could not set the JDK", e);
        }
        JavaParameterBuildWrapper wrapper = new JavaParameterBuildWrapper();
        wrapper.setOriginalJDK(originalJDK);
        wrapper.setJdkIsAvailable(jdkIsAvailable);
        return wrapper;
    }
}
