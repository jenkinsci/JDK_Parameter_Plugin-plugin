package com.datalex.jdkparameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.JDK;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class JavaParameterValueTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    private FreeStyleProject project;
    private FreeStyleBuild build;
    private JDK projectJDK;

    private static final JDK jdk11 = new JDK("jdk-11", "/my/jdk/11");
    private static final JDK jdk17 = new JDK("jdk-17", "/my/jdk/17");
    private static final JDK jdk21 = new JDK("jdk-21", "/my/jdk/21");
    private static final JDK current = new JDK("current-jdk", System.getProperty("java.home"));

    @Before
    public void configureJDKsAndrunOneJob() throws Exception {
        j.jenkins.setJDKs(List.of(jdk11, jdk17, jdk21, current));
        project = j.createFreeStyleProject();
        projectJDK = jdk21;
        project.setJDK(projectJDK);
        build = j.buildAndAssertSuccess(project);
    }

    @Test
    public void testGetSelectedJDK() {
        String selectedJDK = projectJDK.getName();
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "JDK description", selectedJDK);
        assertThat(value.getSelectedJDK(), is(selectedJDK));
    }

    @Test
    public void testSetSelectedJDK() {
        String selectedJDK = jdk17.getName();
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "JDK 17 description", selectedJDK);
        value.setSelectedJDK(selectedJDK);
        assertThat(value.getSelectedJDK(), is(selectedJDK));
    }

    @Test
    public void testCreateBuildWrapper() {
        String selectedJDK = jdk11.getName();
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "JDK 11 description", selectedJDK);
        JavaParameterBuildWrapper wrapper = (JavaParameterBuildWrapper) value.createBuildWrapper(build);
        assertTrue(wrapper.isJdkIsAvailable());
        assertThat(wrapper.getOriginalJDK(), is(projectJDK.getName()));
    }

    @Test
    public void testCreateBuildWrapperWithDefaultJDK() {
        String selectedJDK = JavaParameterDefinition.DEFAULT_JDK;
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "Default JDK description", selectedJDK);
        JavaParameterBuildWrapper wrapper = (JavaParameterBuildWrapper) value.createBuildWrapper(build);
        assertThat(wrapper.getOriginalJDK(), is(projectJDK.getName()));
    }
}
