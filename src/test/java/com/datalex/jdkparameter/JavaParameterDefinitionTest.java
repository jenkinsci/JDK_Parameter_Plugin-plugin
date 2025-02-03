package com.datalex.jdkparameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;

import hudson.model.JDK;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class JavaParameterDefinitionTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    private static final JDK jdk11 = new JDK("jdk-11", "/my/jdk/11");
    private static final JDK jdk17 = new JDK("jdk-17", "/my/jdk/17");
    private static final JDK jdk21 = new JDK("jdk-21", "/my/jdk/21");
    private static final JDK current = new JDK("current-jdk", System.getProperty("java.home"));

    @Before
    public void configureJDKs() throws Exception {
        j.jenkins.setJDKs(List.of(jdk11, jdk17, jdk21, current));
    }

    @Test
    public void testGetJDKNames() {
        assertThat(
                JavaParameterDefinition.getJDKNames(),
                containsInAnyOrder(
                        jdk11.getName(),
                        jdk17.getName(),
                        jdk21.getName(),
                        current.getName(),
                        JavaParameterDefinition.ALL_JDK,
                        JavaParameterDefinition.DEFAULT_JDK));
    }

    @Test
    public void testGetJDKNamesDefault() {
        assertThat(
                JavaParameterDefinition.getJDKNamesDefault(),
                containsInAnyOrder(
                        jdk11.getName(),
                        jdk17.getName(),
                        jdk21.getName(),
                        current.getName(),
                        JavaParameterDefinition.DEFAULT_JDK));
    }

    @Test
    public void testGetJDKSasStrings() {
        assertThat(
                JavaParameterDefinition.getJDKSasStrings(),
                containsInAnyOrder(jdk11.getName(), jdk17.getName(), jdk21.getName(), current.getName()));
    }

    @Test
    public void testGetAllowedJDKs() {
        List<String> allowedJDKs = List.of(jdk11.getName());
        JavaParameterDefinition param =
                new JavaParameterDefinition(jdk11.getName(), "JDK 11", current.getName(), allowedJDKs);
        assertThat(param.getAllowedJDKs(), containsInAnyOrder(jdk11.getName()));
    }

    @Test
    public void testGetAllowedJDKsWithAllJDKs() {
        List<String> allowedJDKs = List.of(jdk17.getName(), JavaParameterDefinition.ALL_JDK);
        JavaParameterDefinition param =
                new JavaParameterDefinition(jdk11.getName(), "JDK 11", current.getName(), allowedJDKs);
        assertThat(param.getAllowedJDKs(), hasItems(JavaParameterDefinition.ALL_JDK));
    }
}
