package com.datalex.jdkparameter;

import hudson.Extension;
import hudson.model.JDK;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: barisbatiege
 * Date: 6/14/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class JavaParameterDefinition extends ParameterDefinition {

    private static final Logger LOGGER = Logger.getLogger(JavaParameterValue.class.getName());

    public static final String DEFAULT_JDK = "(Default)";
    public static final String ALL_JDK = "(All)";
    public final String defaultJDK;
    public final List<String> allowedJDKs;




    @DataBoundConstructor
    public JavaParameterDefinition(String name,String description ,
                                   String defaultJDK, List<String>allowedJDKs ){
        super(name, description);
        this.defaultJDK = defaultJDK;
        if(allowedJDKs.contains(ALL_JDK)) {
            this.allowedJDKs = Arrays.asList(ALL_JDK);
        } else {
            this.allowedJDKs = allowedJDKs;
        }
    }

    //gets the names of configured JDKs
    public static List<String>  getJDKNames(){
        List<String> result = getJDKSasStrings();
        Collections.sort((result));
        result.add(0, DEFAULT_JDK);
        result.add(0, ALL_JDK);
        return result;
    }

    public static List<String> getJDKNamesDefault(){
        List<String> result = getJDKSasStrings();
        result.add(0, DEFAULT_JDK);
        return result;
    }

    protected static List<String> getJDKSasStrings() {
        List<JDK> jdkList = jenkins.model.Jenkins.getInstance().getJDKs();
        List<String> result = new ArrayList<String>();
        for(JDK jdk : jdkList) {
            result.add(jdk.getName());
        }
        return result;
    }

    public List<String> getAllowedJDKs() {
        return allowedJDKs;
    }


    public List<String> getDisplayableJDKs() {
        if (allowedJDKs.contains(ALL_JDK) ) {

            List<String> jdks = new ArrayList<String>();
            jdks.add(DEFAULT_JDK);
            jdks.addAll(getJDKSasStrings());

            if(!jdks.contains(getDefaultJDK()) && getJDKSasStrings().contains(getDefaultJDK())){
             jdks.add(getDefaultJDK());
            }

            Collections.sort((jdks));


            for(String jdk : jdks) {
             if(!getAllowedJDKs().contains(jdk) && !jdk.equalsIgnoreCase(DEFAULT_JDK)) {
                    LOGGER.log(Level.INFO, "[JDK Parameter]: " + jdk + " which was configured has been removed from Jenkins.");
                }
            }

            return jdks;

        } else {
            List<String> jdks2 = new ArrayList<String>();
            jdks2.addAll(allowedJDKs);

            boolean shouldAddDefaultJDK = !jdks2.contains(getDefaultJDK())
                 && (getJDKSasStrings().contains(getDefaultJDK()) || getDefaultJDK().equals(DEFAULT_JDK));

            if (shouldAddDefaultJDK) {
                jdks2.add(getDefaultJDK());
            }

            Collections.sort((jdks2));

            for(String jdk : jdks2) {
                if(!getAllowedJDKs().contains(jdk) && !jdk.equalsIgnoreCase(DEFAULT_JDK)) {
                    LOGGER.log(Level.INFO, "[JDK Parameter]: " + jdk + " which was configured has been removed from Jenkins.");
                }
            }

            return jdks2;
        }
    }


    public String getDefaultJDK() {
        return defaultJDK;
    }


    //gets the list of JDKs to put in "selectable JDKs" array in job config, includes the base JDKs from jenkins
    public List<String>  getSelectableJDKNames(){
        List<String> result = getJDKSasStrings();

        return result;
    }


    @Override
    public ParameterValue createValue(StaplerRequest2 req, JSONObject jo) {
        final String name = jo.getString("name");
        final String selectedJDK = jo.getString("selectedJDK");
        return  new JavaParameterValue(name, getDescription(), selectedJDK);
    }

    @Override
    public ParameterValue createValue(StaplerRequest2 req) {
        String name = (String)req.getAttribute("name");
        String selectedJDK = (String)req.getAttribute("selectedJDK");
        return new JavaParameterValue(name, getDescription(), selectedJDK);
    }

    @Extension
    public static class DescriptorImpl extends ParameterDescriptor {

        @Override
        public String getDisplayName() {
            return "JDK Parameter";
        }
    }
}
