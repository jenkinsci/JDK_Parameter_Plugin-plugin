package com.datalex.jdkparameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.JDK;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class JavaParameterValueTest {

    private static final JDK JDK_11 = new JDK("jdk-11", "/my/jdk/11");
    private static final JDK JDK_17 = new JDK("jdk-17", "/my/jdk/17");
    private static final JDK JDK_21 = new JDK("jdk-21", "/my/jdk/21");
    private static final JDK CURRENT = new JDK("current-jdk", System.getProperty("java.home"));

    private FreeStyleProject project;
    private FreeStyleBuild build;
    private JDK projectJDK;

    private JenkinsRule j;

    @BeforeEach
    void setUp(JenkinsRule rule) throws Exception {
        j = rule;
        j.jenkins.setJDKs(List.of(JDK_11, JDK_17, JDK_21, CURRENT));
        project = j.createFreeStyleProject();
        projectJDK = JDK_21;
        project.setJDK(projectJDK);
        build = j.buildAndAssertSuccess(project);
    }

    @Test
    void testGetSelectedJDK() {
        String selectedJDK = projectJDK.getName();
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "JDK description", selectedJDK);
        assertThat(value.getSelectedJDK(), is(selectedJDK));
    }

    @Test
    void testSetSelectedJDK() {
        String selectedJDK = JDK_17.getName();
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "JDK 17 description", selectedJDK);
        value.setSelectedJDK(selectedJDK);
        assertThat(value.getSelectedJDK(), is(selectedJDK));
    }

    @Test
    void testCreateBuildWrapper() {
        String selectedJDK = JDK_11.getName();
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "JDK 11 description", selectedJDK);
        JavaParameterBuildWrapper wrapper = (JavaParameterBuildWrapper) value.createBuildWrapper(build);
        assertTrue(wrapper.isJdkIsAvailable());
        assertThat(wrapper.getOriginalJDK(), is(projectJDK.getName()));
    }

    @Test
    void testCreateBuildWrapperWithDefaultJDK() {
        String selectedJDK = JavaParameterDefinition.DEFAULT_JDK;
        JavaParameterValue value = new JavaParameterValue("JDK_NAME", "Default JDK description", selectedJDK);
        JavaParameterBuildWrapper wrapper = (JavaParameterBuildWrapper) value.createBuildWrapper(build);
        assertThat(wrapper.getOriginalJDK(), is(projectJDK.getName()));
    }
}
